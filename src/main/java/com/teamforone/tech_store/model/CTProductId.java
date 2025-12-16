package com.teamforone.tech_store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.EqualsAndHashCode;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CTProductId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productId;
    private String colorId;
    private String storageId;
    private String sizeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CTProductId that = (CTProductId) o;
        return Objects.equals(productId, that.productId) &&
                Objects.equals(colorId, that.colorId) &&
                Objects.equals(storageId, that.storageId) &&
                Objects.equals(sizeId, that.sizeId);
    }


    @Override
    public int hashCode(){
        return Objects.hash(productId, colorId, storageId, sizeId);
    }
}
