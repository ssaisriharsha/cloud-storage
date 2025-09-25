package com.ssaisriharsha.cloud_storage.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Entity
@Table(name="files")
@NoArgsConstructor
@Data
public class DataFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String filepath;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdOn;
    @LastModifiedDate
    @Column(nullable = false)
    private Instant modifiedOn;
    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;
}
