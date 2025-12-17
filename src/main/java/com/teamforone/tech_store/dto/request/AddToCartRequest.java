package com.teamforone.tech_store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {

    private UUID productId;  // ⬅️ ĐỔI THÀNH UUID
    private UUID colorId;    // ⬅️ ĐỔI THÀNH UUID
    private UUID sizeId;     // ⬅️ ĐỔI THÀNH UUID
    private UUID storageId;  // ⬅️ ĐỔI THÀNH UUID
    private int quantity;
}