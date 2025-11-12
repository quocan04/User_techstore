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
@Table(name = "phone_specs")
public class PhoneSpecs {
    @Id
    @UuidGenerator
    @Column(name = "spec_id", columnDefinition = "CHAR(36)")
    private String phoneSpecsID;

    @JoinColumn(name = "product_id", nullable = false, columnDefinition = "CHAR(36)", unique = true)
    private String productID;

    @OneToOne
    @JoinColumn(name = "sizeID")
    private DisplaySize displaySize;

    @OneToOne
    @JoinColumn(name = "storageID")
    private Storage storage;

    @Column(name = "rear_camera_main")
    private String rearCameraMain;

    @Column(name = "rear_camera_ultrawide")
    private String rearCameraUltraWide;

    @Column(name = "front_camera")
    private String frontCamera;

    @Column(name = "video_recording_rear")
    private String videoRecordingRear;

    @Column(name = "video_recording_front")
    private String videoRecordingFront;

    @Column(name = "chipset")
    private String chipset;

    @Column(name = "cpu")
    private String cpu;

    @Column(name = "gpu")
    private String gpu;

    @Column(name = "sim_type")
    private String simType;

    @Column(name = "network_support")
    private String networkSupport;

    @Column(name = "nfc")
    private Boolean nfc;

    @Column(name = "wifi")
    private String wifi;

    @Column(name = "bluetooth")
    private String bluetooth;

    @Column(name = "gps")
    private String gps;

    @Column(name = "usb_port")
    private String usbPort;

    @Column(name = "battery_capacity")
    private String batteryCapacity;

    @Column(name = "charging_technology")
    private String chargingTechnology;

    @Column(name = "os")
    private String operatingSystem;

    @Column(name = "dimensions")
    private String dimensions;

    @Column(name = "weight")
    private String weight;

    @Column(name = "ip_rating")
    private String ipRating;

    @Column(name = "special_features")
    private String specialFeatures;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;
}
