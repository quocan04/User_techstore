package com.teamforone.tech_store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantDTO {
    private List<VariantOption> colors;
    private List<VariantOption> storages;
    private List<VariantOption> sizes;

    /**
     * Inner class đại diện cho một option (màu, bộ nhớ, size)
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VariantOption {
        private String id;           // ID của option (colorId, storageId, sizeId)
        private String name;         // Tên hiển thị (VD: "Đen", "128GB", "6.1 inch")
        private String value;        // Giá trị bổ sung (VD: mã màu hex "#000000")
        private boolean available;   // Còn hàng hay không
    }
}