package com.teamforone.tech_store.service.admin;

import com.teamforone.tech_store.dto.request.BrandRequest;
import com.teamforone.tech_store.dto.response.Response;
import com.teamforone.tech_store.model.Brands;

import java.util.List;

public interface BrandService {
    List<Brands> getAllBrands();
    Response addBrand(BrandRequest request);
    Response updateBrand(String brandID, BrandRequest request);
    Response deleteBrand(String brandID);
    Brands findBrandById(String brandID);
}
