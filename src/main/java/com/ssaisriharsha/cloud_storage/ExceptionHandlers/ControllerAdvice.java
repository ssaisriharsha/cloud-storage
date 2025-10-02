package com.ssaisriharsha.cloud_storage.ExceptionHandlers;

import com.ssaisriharsha.cloud_storage.Exceptions.PathTraversalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(PathTraversalException.class)
    public ResponseEntity<Map<Object, Object>> handlePathTraversalException() {
        Map<Object, Object> map=new HashMap<>();
        map.put("Status", HttpStatus.FORBIDDEN);
        map.put("Timestamp", Instant.now());
        map.put("Message", "Path Traversal detected! File operation failed.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
    }
}
