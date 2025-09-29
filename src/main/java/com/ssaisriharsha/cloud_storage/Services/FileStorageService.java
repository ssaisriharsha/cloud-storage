package com.ssaisriharsha.cloud_storage.Services;

import com.ssaisriharsha.cloud_storage.DTOs.UserDTO;
import com.ssaisriharsha.cloud_storage.Entities.DataFile;
import com.ssaisriharsha.cloud_storage.Repos.FileRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;

@Service
public class FileStorageService {
    private final FileRepo repo;
    private final String BASE_DIR;
    public FileStorageService(FileRepo repo, @Value("${linux.storage.location}") String base) {
        this.repo=repo;
        BASE_DIR=base;
    }
    @Transactional
    public void saveFile(UserDTO userdto, MultipartFile file) {
        Path baseLocation=Path.of(BASE_DIR);
        String filename= Paths.get(file.getOriginalFilename()).getFileName().toString();
        Path storageLocation=baseLocation.resolve(userdto.getUsername()).normalize();
        if(!storageLocation.startsWith(BASE_DIR)) {
            throw new RuntimeException("Path traversal detected! Not proceeding.");
        }
        try(var inputStream=file.getInputStream()) {
            Files.createDirectories(storageLocation);
            var fileLocation=storageLocation.resolve(filename).normalize();
            Files.copy(inputStream, fileLocation);
        }
        catch(FileAlreadyExistsException e) {
            return;
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
//        Files
        DataFile file1=new DataFile();
        file1.setFilepath(storageLocation.toString());
        repo.save(file1);
    }
}
