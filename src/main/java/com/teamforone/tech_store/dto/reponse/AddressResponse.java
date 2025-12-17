package com.teamforone.tech_store.dto.response;

import com.teamforone.tech_store.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private Long id;
    private String recipientName;
    private String phone;
    private String fullAddress;
    private boolean isDefault;

    // Constructor từ Entity
    public AddressResponse(Address address) {
        this.id = address.getId();
        this.recipientName = address.getRecipientName();
        this.phone = address.getPhone();
        this.fullAddress = String.format("%s, %s, %s, %s",
                address.getStreet(),
                address.getWard(),
                address.getDistrict(),
                address.getCity());
        this.isDefault = address.isDefault();
    }
}