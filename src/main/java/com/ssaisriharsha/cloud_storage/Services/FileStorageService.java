package com.ssaisriharsha.cloud_storage.Services;

import com.ssaisriharsha.cloud_storage.DTOs.UserDTO;
import com.ssaisriharsha.cloud_storage.Entities.DataFile;
import com.ssaisriharsha.cloud_storage.Repos.FileRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
    private final FileRepo repo;
    private final String BASE_DIR="~/Desktop/Storage";
    public FileStorageService(FileRepo repo) {
        this.repo=repo;
    }
    @Transactional
    public void saveFile(UserDTO userdto, MultipartFile file) throws IOException {
        Path baseLocation=Path.of(BASE_DIR);
        Path storageLocation=baseLocation.resolve(userdto.getUsername()).normalize().resolve(file.getOriginalFilename()).normalize();
        if(!storageLocation.startsWith(BASE_DIR)) {
            throw new RuntimeException("Path traversal detected! Not proceeding.");
        }
        Files.createDirectories(storageLocation);
        try(var inputStream=file.getInputStream()) {
            Files.copy(inputStream, storageLocation, StandardCopyOption.REPLACE_EXISTING);
        }
        DataFile file1=new DataFile();
        file1.setFilepath(storageLocation.toString());
        repo.save(file1);
    }
}
