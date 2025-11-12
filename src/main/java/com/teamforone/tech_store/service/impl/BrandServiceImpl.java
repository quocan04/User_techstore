package com.teamforone.tech_store.service.impl;

import com.teamforone.tech_store.dto.request.BrandRequest;
import com.teamforone.tech_store.dto.response.Response;
import com.teamforone.tech_store.model.Brands;
import com.teamforone.tech_store.repository.admin.crud.BrandRepository;
import com.teamforone.tech_store.service.admin.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }
    @Override
    public List<Brands> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Response addBrand(BrandRequest request) {
        String brandName = request.getBrandName();
        Brands brands = Brands.builder()
                .brandName(brandName)
                .build();
        brandRepository.save(brands);
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Brand added successfully")
                .build();
    }

    @Override
    public Response updateBrand(String brandID, BrandRequest request) {
        Brands brand = brandRepository.findById(brandID).orElseThrow(() -> new RuntimeException("Brand not found"));
        String updatedBrandName = request.getBrandName();
        brand.setBrandName(updatedBrandName);
        brandRepository.save(brand);
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Brand updated successfully")
                .build();
    }

    @Override
    public Response deleteBrand(String brandID) {
        Brands existingBrand = brandRepository.findById(brandID).orElse(null);;
        if (existingBrand == null) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Brand not found")
                    .build();
        }

        brandRepository.delete(existingBrand);
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Brand deleted successfully")
                .build();
    }

    @Override
    public Brands findBrandById(String brandID) {
        return brandRepository.findById(brandID).orElse(null);
    }
}
