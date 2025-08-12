package umc.demoday.whatisthis.global.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {
    List<String> uploadFiles(List<MultipartFile> files, String folder);
    String uploadFile(MultipartFile file, String folder);
}
