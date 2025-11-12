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
@Table(name = "categories")
public class Categories {
    @Id
    @UuidGenerator
    @Column(name = "category_id", columnDefinition = "CHAR(36)")
    private String categoryID;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Categories parentCategory;
}
