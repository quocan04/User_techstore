package com.teamforone.tech_store.service.user.impl;

// --- Imports ---
import com.teamforone.tech_store.model.Payment;
import com.teamforone.tech_store.repository.admin.crud.user.PaymentRepository;
import com.teamforone.tech_store.service.user.VnPayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service; // <-- QUAN TRỌNG NHẤT

import jakarta.servlet.http.HttpServletRequest; // Cần để lấy IP
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service // <-- ĐÁNH DẤU NÀY SẼ SỬA LỖI AUTOWIRE
public class VnPayServiceImpl implements VnPayService {

    // --- Lấy cấu hình từ application.properties ---
    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.returnUrl}")
    private String vnp_ReturnUrl; // URL VNPAY sẽ gọi về (ví dụ: /api/user/checkout/vnpay-return)

    private final PaymentRepository paymentRepository;
    private final HttpServletRequest request; // Dùng để lấy IP

    @Autowired
    public VnPayServiceImpl(PaymentRepository paymentRepository, HttpServletRequest request) {
        this.paymentRepository = paymentRepository;
        this.request = request;
    }

    /**
     * HÀM 1: TẠO URL THANH TOÁN (ĐỂ BIẾN THÀNH QR)
     */
    @Override
    public String createVnPayPaymentUrl(String orderId, long amount, String paymentMethod) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = orderId; // Mã tham chiếu (chính là orderId)
        String vnp_OrderInfo = "Thanh toan don hang " + orderId;
        String vnp_OrderType = "other"; // Loại hàng hóa
        String vnp_Locale = "vn";
        String vnp_ReturnUrl = this.vnp_ReturnUrl;
        String vnp_IpAddr = getIpAddress(request); // Lấy IP người dùng

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount)); // Số tiền (đã x100)
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        // Thêm thời gian tạo và hết hạn
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15); // Hết hạn sau 15 phút
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Sắp xếp các tham số và tạo Hash
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                hashData.append('&');
                query.append('&');
            }
        }

        String queryUrl = query.substring(0, query.length() - 1); // Bỏ dấu & cuối
        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.substring(0, hashData.length() - 1));

        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        // URL thanh toán đầy đủ
        String paymentUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?" + queryUrl;

        return paymentUrl; // Frontend sẽ dùng link này để tạo QR code
    }

    /**
     * HÀM 2: XỬ LÝ KẾT QUẢ VNPAY TRẢ VỀ
     */
    @Override
    public boolean processVnPayPayment(Map<String, String> vnp_Params) {
        String vnp_SecureHash = vnp_Params.get("vnp_SecureHash");

        // Bỏ 'vnp_SecureHash' ra khỏi map để kiểm tra chữ ký
        vnp_Params.remove("vnp_SecureHash");

        // Sắp xếp lại và tạo hash
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                hashData.append('&');
            }
        }

        String calculatedHash = hmacSHA512(vnp_HashSecret, hashData.substring(0, hashData.length() - 1));

        // 1. Kiểm tra Chữ ký
        if (!calculatedHash.equals(vnp_SecureHash)) {
            return false; // Chữ ký không hợp lệ
        }

        // 2. Kiểm tra Trạng thái Giao dịch
        String vnp_ResponseCode = vnp_Params.get("vnp_ResponseCode");
        String vnp_TxnRef = vnp_Params.get("vnp_TxnRef"); // (chính là orderId)

        if ("00".equals(vnp_ResponseCode)) {
            // Thanh toán thành công
            // Cập nhật trạng thái Payment trong DB (Theo Plan B)
            Payment payment = paymentRepository.findByVnpTxnRef(vnp_TxnRef).orElse(null);
            if (payment != null && "PENDING".equals(payment.getPaymentStatus())) {
                payment.setPaymentStatus("PAID");
                payment.setVnpTransactionNo(vnp_Params.get("vnp_TransactionNo"));
                payment.setBankCode(vnp_Params.get("vnp_BankCode"));
                payment.setPaymentDate(LocalDateTime.now());
                paymentRepository.save(payment);
                return true;
            }
        }

        // Thanh toán thất bại
        return false;
    }

    // --- HÀM HELPER ---

    // Hàm băm HmacSHA512
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
            throw new RuntimeException("Lỗi khi tạo chữ ký HmacSHA512", e);
        }
    }

    // Hàm lấy địa chỉ IP
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