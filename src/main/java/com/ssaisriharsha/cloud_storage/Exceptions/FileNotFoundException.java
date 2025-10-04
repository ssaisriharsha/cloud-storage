package com.ssaisriharsha.cloud_storage.Exceptions;

import java.nio.file.Path;

public class FileNotFoundException extends FileManagementException {
    public FileNotFoundException(Path fileLocation) {
        super("The file " + fileLocation.toString() + " doesn't exists.");
    }
}
