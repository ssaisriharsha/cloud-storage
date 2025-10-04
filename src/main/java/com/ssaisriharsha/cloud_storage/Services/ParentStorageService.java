package com.ssaisriharsha.cloud_storage.Services;

import com.ssaisriharsha.cloud_storage.Exceptions.FileExistsException;
import com.ssaisriharsha.cloud_storage.Exceptions.FileNotFoundException;
import com.ssaisriharsha.cloud_storage.Exceptions.NotAFileException;
import com.ssaisriharsha.cloud_storage.Exceptions.PathTraversalException;
import com.ssaisriharsha.cloud_storage.SecurityConfig.SecureUser;
import org.apache.commons.fileupload.InvalidFileNameException;
import org.owasp.esapi.Validator;
import org.owasp.esapi.errors.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
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
    private final Validator validator;

    public ParentStorageService(FileSystemStorageService fsStore,
                                DbStorageService dbStore,
                                @Value("${linux.storage.location}") String base,
                                Validator validator) {
        this.fsStore=fsStore;
        this.dbStore=dbStore;
        this.BASE_LOCATION=base;
        this.validator=validator;
    }

    public void uploadFile(MultipartFile mpFile, String relPath) {
        SecureUser user= (SecureUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Path userDir=Paths.get(BASE_LOCATION).resolve(user.getUsername());
        Path storageLocation= userDir.resolve(relPath).normalize();
        if(!storageLocation.startsWith(userDir)) {
            throw new PathTraversalException();
        }
        String fileName;
        try{
            fileName=validator.getValidFileName("Upload", mpFile.getOriginalFilename(), null, false);
        } catch (ValidationException e) {
            throw new InvalidFileNameException(mpFile.getOriginalFilename(), "Filename contains invalid/dangerous characters.");
        }
        Path filePath=storageLocation.resolve(fileName);
        dbStore.startUploading(mpFile, filePath);
        if (fsStore.saveFile(mpFile, filePath)) dbStore.finishUploading(filePath);
    }

    public void deleteFile(String relPath) {
        SecureUser user=(SecureUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Path userDir=Paths.get(BASE_LOCATION).resolve(user.getUsername());
        Path fileLocation=userDir.resolve(relPath).normalize();
        if(!fileLocation.startsWith(userDir)) {
            throw new PathTraversalException();
        }
        if(!Files.exists(fileLocation)) throw new FileNotFoundException(fileLocation);
        if(Files.isDirectory(fileLocation)) throw new NotAFileException(fileLocation);
        dbStore.startDeleting(fileLocation);
        if(fsStore.deleteFile(fileLocation)) dbStore.finishDeleting(fileLocation);
    }

    public void renameFile(String oldRelPath, String newRelPath) {
        SecureUser user=(SecureUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Path userDir=Paths.get(BASE_LOCATION).resolve(user.getUsername());
        Path newFileLoc=userDir.resolve(newRelPath).normalize();
        Path oldFileLoc=userDir.resolve(oldRelPath).normalize();
        if(!newFileLoc.startsWith(userDir)||!oldFileLoc.startsWith(userDir)) throw new PathTraversalException();
        if(Files.exists(newFileLoc)) throw new FileExistsException();
        if(!Files.exists(oldFileLoc)) throw new FileNotFoundException(oldFileLoc);
        dbStore.startRenaming(oldFileLoc);
        if(fsStore.renameFile(newFileLoc, oldFileLoc)) dbStore.finishRenaming(newFileLoc, oldFileLoc);
    }
}
