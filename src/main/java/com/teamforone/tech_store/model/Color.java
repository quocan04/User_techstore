package com.teamforone.tech_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "color")
public class Color {
    @Id
    @UuidGenerator
    @Column(name = "colorID", columnDefinition = "CHAR(36)")
    private String colorID;

    @Column(name = "colorname", nullable = false)
    private String colorName;
}
