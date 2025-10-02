package com.ssaisriharsha.cloud_storage.Repos;

import com.ssaisriharsha.cloud_storage.Entities.DataFile;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FileRepo extends JpaRepository<DataFile, Long> {
    DataFile findByFilePath(String filepath);
}
