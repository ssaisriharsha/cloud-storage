package com.ssaisriharsha.cloud_storage.Exceptions;

public class PathTraversalException extends FileManagementException{
    public PathTraversalException() {
        super("Path Traversal detected! File operation failed.");
    }
}
