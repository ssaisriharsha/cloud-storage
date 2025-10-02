package com.ssaisriharsha.cloud_storage.Services;

import com.ssaisriharsha.cloud_storage.Exceptions.PathTraversalException;
import com.ssaisriharsha.cloud_storage.SecurityConfig.SecureUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
* This storage service uses DB as the source of truth strategy.
* This class takes care of reading and writing files in high level.
* Everytime a file operation needs to be performed, it should be logged to the DB first,
* perform the operation on file system and
* log its status again.
* Cron jobs should be created to take care of orphan file removal from FS and removal of redundant entries from DB.
* This way, we can maintain the consistency of DB with FS.
* */

@Service
public class ParentStorageService {
    private final FileSystemStorageService fsStore;
    private final DbStorageService dbStore;
    private final String BASE_LOCATION;

    public ParentStorageService(FileSystemStorageService fsStore,
                                DbStorageService dbStore,
                                @Value("${linux.storage.location}") String base) {
        this.fsStore=fsStore;
        this.dbStore=dbStore;
        this.BASE_LOCATION=base;
    }

    public void uploadFile(MultipartFile mpFile, String relPath) {
        SecureUser user= (SecureUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Path userDir=Paths.get(BASE_LOCATION).resolve(user.getUsername());
        Path storageLocation= userDir.resolve(relPath).normalize();
        if(!storageLocation.startsWith(userDir)) {
            throw new PathTraversalException();
        }
        Path fileLocation=storageLocation.resolve(Paths.get(mpFile.getOriginalFilename()).getFileName());

    }
}
