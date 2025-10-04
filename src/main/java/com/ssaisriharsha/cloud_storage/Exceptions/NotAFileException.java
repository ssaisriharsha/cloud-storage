package com.ssaisriharsha.cloud_storage.Exceptions;

import java.nio.file.Path;

public class NotAFileException extends FileManagementException{
    public NotAFileException(Path fileLocation) {
        super(fileLocation.toString() + " is not a valid file.");
    }
}
