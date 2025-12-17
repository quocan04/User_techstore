package com.teamforone.tech_store.service.user.impl;

import com.teamforone.tech_store.model.Payment;
import com.teamforone.tech_store.repository.admin.crud.user.PaymentRepository;
import com.teamforone.tech_store.service.user.VnPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class VnPayServiceImpl implements VnPayService {

    @Value("${vnpay.url}")
    private String vnp_PayUrl;

    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.returnUrl}")
    private String vnp_ReturnUrl;

    private final PaymentRepository paymentRepository;
    private final HttpServletRequest request;

    @Autowired
    public VnPayServiceImpl(PaymentRepository paymentRepository, HttpServletRequest request) {
        this.paymentRepository = paymentRepository;
        this.request = request;
    }

    @Override
    public String createVnPayPaymentUrl(String orderId, long amount, String paymentMethod) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = orderId;
        String vnp_OrderInfo = "Thanh toan don hang " + orderId;
        String vnp_OrderType = "other";
        String vnp_Locale = "vn";
        String vnp_IpAddr = getIpAddress(request);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Sắp xếp các tham số
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext(); ) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String queryUrl = query.toString();
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        String paymentUrl = vnp_PayUrl + "?" + queryUrl;
        log.info("🔥 Created VNPAY URL: {}", paymentUrl);

        return paymentUrl;
    }

    @Override
    public boolean processVnPayPayment(Map<String, String> vnp_Params) {
        String vnp_SecureHash = vnp_Params.get("vnp_SecureHash");
        vnp_Params.remove("vnp_SecureHash");
        vnp_Params.remove("vnp_SecureHashType");

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();

        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext(); ) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }

        String calculatedHash = hmacSHA512(vnp_HashSecret, hashData.toString());

        if (!calculatedHash.equalsIgnoreCase(vnp_SecureHash)) {
            log.error("❌ Invalid VNPAY Signature!");
            return false;
        }

        String vnp_ResponseCode = vnp_Params.get("vnp_ResponseCode");
        String vnp_TxnRef = vnp_Params.get("vnp_TxnRef");

        if ("00".equals(vnp_ResponseCode)) {
            Payment payment = paymentRepository.findByOrder_OrderId(vnp_TxnRef).orElse(null);
            if (payment != null) {
                payment.setPaymentStatus("PAID");
                payment.setVnpTransactionNo(vnp_Params.get("vnp_TransactionNo"));
                payment.setBankCode(vnp_Params.get("vnp_BankCode"));
                payment.setPaymentDate(LocalDateTime.now());
                paymentRepository.save(payment);
                return true;
            }
        }
        return false;
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmacSha512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmacSha512.init(secretKey);
            byte[] hash = hmacSha512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo chữ ký VNPAY", e);
        }
    }

    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                try {
                    ipAddress = InetAddress.getLocalHost().getHostAddress();
                } catch (Exception e) {
                    ipAddress = "127.0.0.1";
                }
            }
        }
        return ipAddress;
    }
}