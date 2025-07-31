package umc.demoday.whatisthis.global;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode.IMAGE_UPLOAD_ERROR;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class S3TestController {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Operation(summary = "S3 이미지 업로드 by-윤영석", description = "multipart/form-data 형식의 파일을 업로드합니다.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CustomResponse<List<String>> uploadFiles(
            @RequestPart("files") List<MultipartFile> files) {

        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/test/" + fileName;

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());

                amazonS3Client.putObject(bucket, "test/" + fileName, file.getInputStream(), metadata);

                fileUrls.add(fileUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return CustomResponse.onFailure("IMAGE_UPLOAD_ERROR", "이미지 업로드에 실패했습니다.");

            }
        }

        return CustomResponse.ok(fileUrls);
    }

}