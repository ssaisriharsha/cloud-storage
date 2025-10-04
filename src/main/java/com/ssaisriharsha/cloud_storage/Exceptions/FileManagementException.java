package com.ssaisriharsha.cloud_storage.Exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FileManagementException extends RuntimeException {
    public FileManagementException(String msg) {
        super(msg);
    }
}
