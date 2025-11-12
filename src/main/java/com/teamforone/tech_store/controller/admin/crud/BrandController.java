package com.teamforone.tech_store.controller.admin.crud;

import com.teamforone.tech_store.dto.request.BrandRequest;
import com.teamforone.tech_store.dto.response.Response;
import com.teamforone.tech_store.model.Brands;
import com.teamforone.tech_store.service.admin.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class BrandController {
    @Autowired
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/brands")
    public List<Brands> getAllBrands() {
        return brandService.getAllBrands();
    }

    @PostMapping("/brands/add")
    public ResponseEntity<Response> addBrand(@RequestBody BrandRequest brand) {
        Response response = brandService.addBrand(brand);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/brands/update/{id}")
    public ResponseEntity<Response> updateBrand(@PathVariable String id , @RequestBody BrandRequest brand) {
        Response response = brandService.updateBrand(id, brand);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/brands/delete/{id}")
    public ResponseEntity<Response> deleteBrand(@PathVariable String id) {
        Response response = brandService.deleteBrand(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/brands/{id}")
    public Brands findBrandById(@PathVariable String id) {
        return brandService.findBrandById(id);
    }

}
