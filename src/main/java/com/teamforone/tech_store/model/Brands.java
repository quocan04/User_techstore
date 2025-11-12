package com.teamforone.tech_store.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "brands")
public class Brands {
    @Id
    @UuidGenerator
    @Column(name = "brand_id", columnDefinition = "CHAR(36)")
    private String brandID;

    @Column(name = "brand_name", nullable = false)
    private String brandName;
}
