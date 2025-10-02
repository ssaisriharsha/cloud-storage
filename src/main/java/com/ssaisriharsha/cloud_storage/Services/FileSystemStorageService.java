package com.ssaisriharsha.cloud_storage.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Every method in this class returns a boolean indicating the result of operation.
 * */

@Service
public class FileSystemStorageService {

    public boolean saveFile(MultipartFile uploadFile, Path path) {
        try {
            uploadFile.transferTo(path);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean renameFile(Path newPath, Path oldPath) {
        try {
            Files.move(oldPath, newPath);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
