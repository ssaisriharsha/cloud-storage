package com.ssaisriharsha.cloud_storage.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserLoginDTO {
    private String username;
    private String email;
    private String password;
}
