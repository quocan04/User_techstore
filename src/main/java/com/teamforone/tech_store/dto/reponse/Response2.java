package com.teamforone.tech_store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp Response chung cho các API.
 * Sử dụng kiểu Generic <T> để 'data' có thể là bất cứ thứ gì.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response2<T> {

    // Trạng thái (ví dụ: "success", "error")
    private String status;

    // Thông báo cho client (ví dụ: "Xóa sản phẩm thành công!")
    private String message;

    // Dữ liệu trả về (có thể là null, hoặc một đối tượng bất kỳ)
    private T data;

}