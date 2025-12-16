package com.teamforone.tech_store.service.user;

import com.teamforone.tech_store.model.CTProducts;
import java.util.List;

public interface CTProductService {
    List<CTProducts> getVariantsByProductId(String productId);
    CTProducts getCheapestVariant(String productId);
}