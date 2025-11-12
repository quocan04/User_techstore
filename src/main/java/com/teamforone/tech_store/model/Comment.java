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
@Table(name = "comment")
public class Comment {
    @Id
    @UuidGenerator
    @Column(name = "comment_id", columnDefinition = "CHAR(36)")
    private String commentID;

    @JoinColumn(name = "product_id", nullable = false, columnDefinition = "CHAR(36)")
    private String product;

    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "CHAR(36)")
    private String user;

    @Column(name = "rating", columnDefinition = "INT CHECK (rating >= 1 AND rating <= 5)")
    private int rating;

    @Column(name = "noidung", columnDefinition = "TEXT", nullable = false)
    private String commentText;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'pending'")
    private Status status;

    @CreationTimestamp
    @Column(name = "ngaybl", nullable = false)
    private Date createdAt;

    @Column(name = "luotthich", columnDefinition = "INT DEFAULT 0")
    private Integer likeCount;


    public enum Status {
        PENDING,
        APPROVED,
        REJECTED;

        private static Status toEnum(String value) {
            for(Status status : Status.values()){
                if (status.toString().equalsIgnoreCase(value)) return status;
            }
            return null;
        }
    }
}
