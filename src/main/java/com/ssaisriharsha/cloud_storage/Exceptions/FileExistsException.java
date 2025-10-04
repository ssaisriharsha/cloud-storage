package com.ssaisriharsha.cloud_storage.Exceptions;

public class FileExistsException extends FileManagementException {
    public FileExistsException() {
        super("The mentioned file already exists.");
    }
}
