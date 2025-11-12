package com.teamforone.tech_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "storage")
public class Storage {
    @Id
    @UuidGenerator
    @Column(name = "storageID", columnDefinition = "CHAR(36)")
    private String storageID;

    @Column(name = "ram", nullable = false)
    private String ram;

    @Column(name = "rom", nullable = false)
    private String rom;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;
}
