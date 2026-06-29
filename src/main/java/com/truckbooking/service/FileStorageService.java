package com.truckbooking.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String uploadTruckImage(MultipartFile file);

}