package com.ssaisriharsha.cloud_storage.Services;

import com.ssaisriharsha.cloud_storage.Entities.DataFile;
import com.ssaisriharsha.cloud_storage.Entities.FileStatus;
import com.ssaisriharsha.cloud_storage.Entities.User;
import com.ssaisriharsha.cloud_storage.Repos.FileRepo;
import com.ssaisriharsha.cloud_storage.SecurityConfig.SecureUser;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;


/**
 * This class takes care of maintaining the file's metadata.
 * Most/all the methods of this class are transactional.
 * */

@Service
public class DbStorageService {
    private final FileRepo repo;

    public DbStorageService(FileRepo repo) {
        this.repo=repo;
    }

    @Transactional
    public void startUploading(MultipartFile uploadFile, Path filePath) {
        DataFile file=new DataFile();
        SecureUser authUser= (SecureUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user=authUser.getUser();
        file.setFilePath(filePath.toString());
        file.setSize(uploadFile.getSize());
        file.setStatus(FileStatus.UPLOADING);
        file.setUser(user);
    }

    @Transactional
    public void finishUploading(Path filePath) {
        DataFile file=repo.findByFilePath(filePath.toString());
        file.setStatus(FileStatus.READY);
    }

    @Transactional
    public void startDeleting(Path filePath) {
        DataFile file=repo.findByFilePath(filePath.toString());
        file.setStatus(FileStatus.DELETING);
    }

    @Transactional
    public void finishDeleting(Path filePath) {
        DataFile file=repo.findByFilePath(filePath.toString());
        repo.delete(file);
    }

    @Transactional
    public void startRenaming(Path filePath) {
        DataFile file=repo.findByFilePath(filePath.toString());
        file.setStatus(FileStatus.RENAMING);
    }

    @Transactional
    public void finishRenaming(Path newFilePath, Path oldFilePath) {
        DataFile file=repo.findByFilePath(oldFilePath.toString());
        file.setFilePath(newFilePath.toString());
        file.setStatus(FileStatus.READY);
    }
}
