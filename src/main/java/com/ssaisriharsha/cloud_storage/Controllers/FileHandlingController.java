package com.ssaisriharsha.cloud_storage.Controllers;

import com.ssaisriharsha.cloud_storage.DTOs.UserDTO;
import com.ssaisriharsha.cloud_storage.Services.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class FileHandlingController {
    private final FileStorageService service;
    public FileHandlingController(FileStorageService service) {
        this.service=service;
    }
    public ResponseEntity<Map<String, String>> uploadFile(@RequestPart UserDTO userdto, @RequestPart MultipartFile file) throws IOException {
        service.saveFile(userdto, file);
        Map<String, String> rMap=new HashMap<>();
        rMap.put("Status", HttpStatus.OK.toString());
        rMap.put("Message", "Upload Successful!");
        rMap.put("Timestamp", Instant.now().toString());
        return ResponseEntity.ok(rMap);
    }
}
