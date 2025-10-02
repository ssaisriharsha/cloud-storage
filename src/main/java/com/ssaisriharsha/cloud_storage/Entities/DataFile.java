package com.ssaisriharsha.cloud_storage.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name="files")
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class DataFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, unique = true)
    private String filePath;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdOn;
    @LastModifiedDate
    @Column(nullable = false)
    private Instant modifiedOn;
    @Column(nullable = false)
    private long size;
    private FileStatus status;
    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;
}
