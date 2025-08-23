package umc.demoday.whatisthis.global.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public List<String> uploadFiles(List<MultipartFile> files, String folder) {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                String key = folder + "/" + fileName;

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());

                amazonS3Client.putObject(bucket, key, file.getInputStream(), metadata);

                String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
                fileUrls.add(fileUrl);

            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패", e);
            }
        }
        return fileUrls;
    }

    public String uploadFile(MultipartFile file, String folder) {

        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String key = folder + "/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, key, file.getInputStream(), metadata);

            return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;

        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 실패", e);
        }
    }

}