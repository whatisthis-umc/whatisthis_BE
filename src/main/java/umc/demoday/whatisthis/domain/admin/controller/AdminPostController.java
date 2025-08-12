package umc.demoday.whatisthis.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.demoday.whatisthis.domain.admin.Admin;
import umc.demoday.whatisthis.domain.admin.dto.AdminPostReqDTO;
import umc.demoday.whatisthis.domain.admin.dto.AdminPostResDTO;
import umc.demoday.whatisthis.domain.admin.redis.async.InitialDataIndexer;
import umc.demoday.whatisthis.domain.admin.repository.AdminRepository;
import umc.demoday.whatisthis.domain.admin.service.AdminPostService;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.global.CustomUserDetails;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;
import umc.demoday.whatisthis.global.service.S3Service;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final AdminPostService adminPostService;
    private final S3Service s3Service;
    private final AdminRepository adminRepository;
    private final InitialDataIndexer initialDataIndexer;

    @GetMapping("/{post-id}")
    @Operation(summary = "개별 게시글 조회 API -by 천성호, 남성현")
    public CustomResponse<AdminPostResDTO> getPost(@PathVariable("post-id") Integer postId){
        AdminPostResDTO response = adminPostService.getPost(postId);
        return CustomResponse.ok(response);
    }

    @DeleteMapping("/{post-id}")
    @Operation(summary = "게시글 삭제 API -by 천성호")
    public CustomResponse<Integer> deletePost(@PathVariable("post-id") Integer postId){
        adminPostService.deletePost(postId);
        return CustomResponse.ok(postId);
    }

    @PatchMapping("/{post-id}")
    @Operation(summary = "게시글 업데이트 API -by 천성호")
    public CustomResponse<AdminPostResDTO.updatePostResDTO> updatePost(@PathVariable("post-id") Integer postId, @RequestBody AdminPostReqDTO.updatePostReqDTO request){
        AdminPostResDTO.updatePostResDTO response = adminPostService.updatePost(postId, request);
        return CustomResponse.ok(response);
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 작성 API -by 천성호, 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<AdminPostResDTO.createPostResDTO> createPost(@RequestPart("request") AdminPostReqDTO.createPostReqDTO request,
                                                                       @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                                                       @AuthenticationPrincipal CustomUserDetails customUserDetails){

        Admin admin = adminRepository.findById(customUserDetails.getId()).orElseThrow(()->new GeneralException(GeneralErrorCode.NOT_FOUND_404));
        List<String> imageUrls = (images != null && !images.isEmpty())
                ? s3Service.uploadFiles(images, "post")
                : Collections.emptyList();

        AdminPostResDTO.createPostResDTO response = adminPostService.createPost(request, admin, imageUrls);
        return CustomResponse.ok(response);
    }

    @GetMapping("/")
    @Operation(summary = "게시물 목록 조회 API -by 천성호")
    public CustomResponse<AdminPostResDTO.allPostResDTO> getAllPosts(@RequestParam(name = "category", required = false) Category category,
                                                                              @RequestParam("page") Integer page,
                                                                              @RequestParam("size") Integer size){

        AdminPostResDTO.allPostResDTO response = adminPostService.getAllPosts(category, page, size);
        return CustomResponse.ok(response);
    }

    @PostMapping("/reindex")
    public ResponseEntity<String> reindexAllPosts() {
        // 비동기 실행을 위해 별도 스레드에서 리인덱싱 작업을 시작
        new Thread(initialDataIndexer::reindexAllPosts).start();
        // 사용자에게는 즉시 응답을 반환
        return ResponseEntity.ok("전체 리인덱싱 작업이 백그라운드에서 시작되었습니다. 서버 로그를 확인하세요.");
    }
}
