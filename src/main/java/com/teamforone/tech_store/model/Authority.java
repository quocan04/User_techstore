package com.teamforone.tech_store.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "authorities")
public class Authority {
    @Id
    @UuidGenerator
    @Column(name = "authority_id", columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;
}

