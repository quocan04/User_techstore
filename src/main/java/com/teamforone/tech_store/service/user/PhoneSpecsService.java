package com.teamforone.tech_store.service.user;

import com.teamforone.tech_store.model.PhoneSpecs;
import com.teamforone.tech_store.repository.admin.crud.UserPhoneSpecsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhoneSpecsService {

    @Autowired
    private UserPhoneSpecsRepository phoneSpecsRepository;

    public PhoneSpecs getSpecsByProductId(String productId) {
        return phoneSpecsRepository.findByProductID(productId).orElse(null);
    }
}
