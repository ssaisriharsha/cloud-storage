package com.ssaisriharsha.cloud_storage.DTOs;

import com.ssaisriharsha.cloud_storage.Entities.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDTO {
    private String username;
    private String email;
    private Instant createdOn;
    public UserDTO(User user) {
        this.username=user.getUsername();
        this.email=user.getEmail();
        this.createdOn=user.getCreatedOn();
    }
}
