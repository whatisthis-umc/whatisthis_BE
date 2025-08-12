package umc.demoday.whatisthis.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.comment.converter.CommentConverter;
import umc.demoday.whatisthis.domain.comment.dto.CommentRequestDTO;
import umc.demoday.whatisthis.domain.comment.dto.CommentResponseDTO;
import umc.demoday.whatisthis.domain.comment.service.CommentService;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.service.member.MemberCommandService;
import umc.demoday.whatisthis.domain.member.service.member.MemberQueryService;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.MyPagePostResponseDTO;
import umc.demoday.whatisthis.domain.post.dto.PostRequestDTO;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post.service.CommunityPostService;
import umc.demoday.whatisthis.domain.post.service.MyPagePostService;
import umc.demoday.whatisthis.domain.post_image.PostImage;
import umc.demoday.whatisthis.domain.report.Report;
import umc.demoday.whatisthis.domain.report.converter.ReportConverter;
import umc.demoday.whatisthis.domain.report.dto.ReportRequestDTO;
import umc.demoday.whatisthis.domain.report.dto.ReportResponseDTO;
import umc.demoday.whatisthis.domain.report.service.ReportService;
import umc.demoday.whatisthis.global.CustomUserDetails;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode;
import umc.demoday.whatisthis.global.service.S3Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static umc.demoday.whatisthis.domain.post.converter.MyPagePostConverter.toMyPostModifyDTO;
import static umc.demoday.whatisthis.domain.post.converter.PostConverter.*;
import static umc.demoday.whatisthis.domain.report.converter.ReportConverter.toReportCommentResponseDTO;
import static umc.demoday.whatisthis.domain.report.converter.ReportConverter.toReportPostResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommunityPostController {

    private final CommunityPostService communityPostService;
    private final MemberCommandService memberCommandService;
    private final CommentService commentService;
    private final ReportService reportService;
    private final S3Service s3Service;
    private final MyPagePostService myPagePostService;
    private final MemberQueryService memberQueryService;

    @GetMapping("/communities")
    @Operation(summary = "커뮤니티 페이지 조회 API (전체) -by 남성현")
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> communityList
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수")@RequestParam Integer size,
             @Parameter(description = "인기순 = BEST ,최신순 = LATEST ") @RequestParam SortBy sort) {

        Page<Post> postList = communityPostService.getAllPosts(page-1, size, sort);

        Set<Member> members = postList.stream()
                .map(Post::getMember)
                .collect(Collectors.toSet());

        for (Member member : members) {
            memberCommandService.evaluateIsBest(member);
        }

        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toCommunityPostPreviewListDTO(postList));
    }

    @GetMapping("/communities/popular")
    @Operation(summary = "커뮤니티 페이지 조회 API (인기 글) -by 남성현")
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> popCommunityList
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수")@RequestParam Integer size) {

        Page<Post> postList = communityPostService.getBestPosts(page-1, size);

        Set<Member> members = postList.stream()
                .map(Post::getMember)
                .collect(Collectors.toSet());

        for (Member member : members) {
            memberCommandService.evaluateIsBest(member);
        }
        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toCommunityPostPreviewListDTO(postList));
    }

    @GetMapping("/communities/tips")
    @Operation(summary = "커뮤니티 페이지 조회 API (생활 꿀팁) -by 남성현")
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> tipCommunityList
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수")@RequestParam Integer size,
             @Parameter(description = "인기순 = BEST ,최신순 = LATEST ") @RequestParam SortBy sort) {

        Page<Post> postList = communityPostService.getAllPostsByCategory(page-1, size, sort, Category.TIP);

        Set<Member> members = postList.stream()
                .map(Post::getMember)
                .collect(Collectors.toSet());

        for (Member member : members) {
            memberCommandService.evaluateIsBest(member);
        }

        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toCommunityPostPreviewListDTO(postList));
    }

    @GetMapping("/communities/items")
    @Operation(summary = "커뮤니티 페이지 조회 API (꿀템 추천) -by 남성현")
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> itemCommunityList
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수")@RequestParam Integer size,
             @Parameter(description = "인기순 = BEST ,최신순 = LATEST ") @RequestParam SortBy sort) {

        Page<Post> postList = communityPostService.getAllPostsByCategory(page-1, size, sort, Category.ITEM);

        Set<Member> members = postList.stream()
                .map(Post::getMember)
                .collect(Collectors.toSet());

        for (Member member : members) {
            memberCommandService.evaluateIsBest(member);
        }

        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toCommunityPostPreviewListDTO(postList));
    }

    @GetMapping("/communities/should-i-buy")
    @Operation(summary = "커뮤니티 페이지 조회 API (살까 말까?) -by 남성현")
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> buyCommunityList
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수")@RequestParam Integer size,
             @Parameter(description = "인기순 = BEST ,최신순 = LATEST ") @RequestParam SortBy sort) {

        Page<Post> postList = communityPostService.getAllPostsByCategory(page-1, size, sort, Category.SHOULD_I_BUY);

        Set<Member> members = postList.stream()
                .map(Post::getMember)
                .collect(Collectors.toSet());

        for (Member member : members) {
            memberCommandService.evaluateIsBest(member);
        }

        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toCommunityPostPreviewListDTO(postList));
    }

    @GetMapping("/communities/curious")
    @Operation(summary = "커뮤니티 페이지 조회 API (궁금해요!) -by 남성현")
    public CustomResponse<PostResponseDTO.CommunityPostPreviewListDTO> curiousCommunityList
            (@Parameter(description = "페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 게시물 수")@RequestParam Integer size,
             @Parameter(description = "인기순 = BEST ,최신순 = LATEST ") @RequestParam SortBy sort) {

        Page<Post> postList = communityPostService.getAllPostsByCategory(page-1, size, sort, Category.CURIOUS);

        Set<Member> members = postList.stream()
                .map(Post::getMember)
                .collect(Collectors.toSet());

        for (Member member : members) {
            memberCommandService.evaluateIsBest(member);
        }

        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toCommunityPostPreviewListDTO(postList));
    }

    @PostMapping(value = "/communities", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "커뮤니티 글 작성 API -by 남성현, 윤영석", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<PostResponseDTO.NewPostResponseDTO> newPost(
            @RequestPart("request") @Valid PostRequestDTO.NewPostRequestDTO request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        // S3에 이미지 업로드
        List<String> imageUrls = (images != null && !images.isEmpty())
                ? s3Service.uploadFiles(images, "post")
                : Collections.emptyList();

        Member loginUser = memberQueryService.findMemberByDetails(customUserDetails);

        // 게시글 + 이미지 URL 저장
        Post newPost = communityPostService.insertNewPost(request, imageUrls, loginUser);

        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, toNewPostDTO(newPost));
    }

    @GetMapping("/communities/{post-id}")
    @Operation(summary = "커뮤니티 게시물 본문 조회 API -by 남성현")
    public CustomResponse<PostResponseDTO.CommunityPostViewDTO> getCommunityPost
            (@Parameter(description = "게시물 id") @PathVariable(name = "post-id") Integer postId,
             @Parameter(description = "댓글 페이지 번호") @RequestParam Integer page,
             @Parameter(description = "한 페이지 당 댓글 수")@RequestParam Integer size,
             @Parameter(description = "인기순 = BEST ,최신순 = LATEST ") @RequestParam SortBy sort) {
        Post post = communityPostService.getPost(postId);
        Page<Comment> commentList = communityPostService.getCommentListByPost(page-1, size, sort, post);
        List<Hashtag> hashtagList = communityPostService.getHashtagListByPost(post);
        List<PostImage> postImageList = communityPostService.getPostImageListByPost(post);
        communityPostService.plusOneViewCount(post);

        return CustomResponse.ok(toCommunityPostViewDTO(post, commentList,postImageList,hashtagList));
    }

    @DeleteMapping("/communities/{post-id}")
    @Operation(summary = "자신의 게시물 삭제 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<Void> deleteMyPagePosts
            (@Parameter(description = "삭제할 post id") @PathVariable(name = "post-id") Integer id,
             @AuthenticationPrincipal Member loginUser) {

        myPagePostService.deletePost(id, loginUser);

        return CustomResponse.onSuccess(GeneralSuccessCode.NO_CONTENT_204, null);
    }

    @PatchMapping(value = "/communities/{post-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "자신의 게시물 수정 API -by 남성현", description = "내용을 전체 덮어쓰기하는 방식이므로 수정없는 부분도 입력해야합니다.", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<MyPagePostResponseDTO.MyPostModifyDTO> patchMyPagePosts
            (@Parameter(description = "수정할 post id") @PathVariable(name = "post-id") Integer id,
             @RequestPart("request") @Valid PostRequestDTO.ModifyPostRequestDTO request,
             @RequestPart(value = "images", required = false) List<MultipartFile> images,
             @AuthenticationPrincipal Member loginUser) {

        // S3에 이미지 업로드
        List<String> imageUrls = (images != null && !images.isEmpty())
                ? s3Service.uploadFiles(images, "post")
                : Collections.emptyList();

        Post post = communityPostService.updatePost(id,request,imageUrls, loginUser);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK,toMyPostModifyDTO(post));
    }

    @PostMapping("/{post-id}/likes")
    @Operation(summary = "커뮤니티 게시물 좋아요 등록 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<PostResponseDTO.PostLikeCountDTO> postLike
            (@Parameter(description = "게시물 id") @PathVariable(name = "post-id") Integer postId,
             @AuthenticationPrincipal Member loginUser) {

        Post post = communityPostService.getPost(postId);
        communityPostService.likePost(post, loginUser);

        return CustomResponse.onSuccess(GeneralSuccessCode.NO_CONTENT_204,toPostLikeCountDTO(post));
    }

    @DeleteMapping("/{post-id}/likes")
    @Operation(summary = "커뮤니티 게시물 좋아요 해제 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<PostResponseDTO.PostLikeCountDTO> postUnLike
            (@Parameter(description = "게시물 id") @PathVariable(name = "post-id") Integer postId,
             @AuthenticationPrincipal Member loginUser) {

        Post post = communityPostService.getPost(postId);
        communityPostService.unLikePost(post, loginUser);

        return CustomResponse.onSuccess(GeneralSuccessCode.NO_CONTENT_204,toPostLikeCountDTO(post));
    }

    @PostMapping("/{post-id}/comments/{comment-id}")
    @Operation(summary = "커뮤니티 댓글 작성 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<CommentResponseDTO.NewCommentResponseDTO> newCommment
            (@Parameter(description = "게시물 id") @PathVariable(name = "post-id") Integer postId,
             @Valid @RequestBody CommentRequestDTO.NewCommentRequestDTO request,
             @AuthenticationPrincipal Member loginUser) {

        Post post = communityPostService.getPost(postId);
        Comment parent;

        if(request.getParentCommentId() == null) {parent = null;}
        else {parent = commentService.getComment(request.getParentCommentId());}

        Comment newComment = commentService.insertNewComment(CommentConverter.toNewComment(request, post, loginUser, parent));

        return CustomResponse.created(CommentConverter.toNewCommentResponseDTO(newComment));
    }

    @PatchMapping("/{post-id}/comments/{comment-id}")
    @Operation(summary = "커뮤니티 댓글 수정 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<CommentResponseDTO.ModifiedCommentResponseDTO> modifyComment
            (@Parameter(description = "게시물 id") @PathVariable(name = "post-id") Integer postId,
             @Parameter(description = "댓글 id") @PathVariable(name = "comment-id") Integer commentId,
             @Valid @RequestBody CommentRequestDTO.ModifyCommentRequestDTO request,
             @AuthenticationPrincipal Member loginUser) {

        commentService.validateCommentByPostId(commentService.getComment(commentId),postId);
        Comment comment = commentService.updateComment(commentId,postId,request.getContent(),loginUser);

        return CustomResponse.ok(CommentConverter.toModifiedCommentResponseDTO(comment));
    }

    @DeleteMapping("/{post-id}/comments/{comment-id}")
    @Operation(summary = "커뮤니티 댓글 삭제 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<CommentResponseDTO.DeletedCommentResponseDTO> deleteComment
            (@Parameter(description = "게시물 id") @PathVariable(name = "post-id") Integer postId,
             @Parameter(description = "댓글 id") @PathVariable(name = "comment-id") Integer commentId,
             @AuthenticationPrincipal Member loginUser) {

        commentService.validateCommentByPostId(commentService.getComment(commentId),postId);
        Comment comment = commentService.deleteComment(commentId, postId, loginUser);

        return CustomResponse.ok(CommentConverter.toDeletedCommentResponseDTO(comment));
    }

    @PostMapping("/{post-id}/comments/{comment-id}/likes")
    @Operation(summary = "커뮤니티 댓글 좋아요 등록 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<CommentResponseDTO.CommentLikeCountDTO> commmentLike
            (@Parameter(description = "게시물 id") @PathVariable(name = "post-id") Integer postId,
             @Parameter(description = "댓글 id") @PathVariable(name = "comment-id") Integer commentId,
             @AuthenticationPrincipal Member loginUser) {

        Comment comment = commentService.getComment(commentId);
        commentService.validateCommentByPostId(comment, postId);
        commentService.likeComment(comment, loginUser);

        return CustomResponse.onSuccess(GeneralSuccessCode.NO_CONTENT_204,CommentConverter.toCommentLikeCountDTO(comment));
    }

    @DeleteMapping("/{post-id}/comments/{comment-id}/likes")
    @Operation(summary = "커뮤니티 댓글 좋아요 해제 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<CommentResponseDTO.CommentLikeCountDTO> commmentUnLike
            (@Parameter(description = "게시물 id") @PathVariable(name = "post-id") Integer postId,
             @Parameter(description = "댓글 id") @PathVariable(name = "comment-id") Integer commentId,
             @AuthenticationPrincipal Member loginUser) {

        Comment comment = commentService.getComment(commentId);
        commentService.validateCommentByPostId(comment, postId);
        commentService.unLikeComment(comment, loginUser);

        return CustomResponse.onSuccess(GeneralSuccessCode.NO_CONTENT_204,CommentConverter.toCommentLikeCountDTO(comment));
    }

    @PostMapping("/{post-id}/reports")
    @Operation(summary = "게시글 신고 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<ReportResponseDTO.ReportPostResponseDTO> postReport
            (@Parameter(description = "게시물 id") @PathVariable(name = "post-id") Integer postId,
             @Valid @RequestBody ReportRequestDTO.NewReportRequestDTO request,
             @AuthenticationPrincipal Member loginUser) {

        Post post = communityPostService.getPost(postId);
        Report report = reportService.insertNewReport(ReportConverter.toReport(request,loginUser,post,null));

        return CustomResponse.onSuccess(GeneralSuccessCode.NO_CONTENT_204,toReportPostResponseDTO(report));
    }


    @PostMapping("/{post-id}/comments/{comment-id}/reports")
    @Operation(summary = "댓글 신고 API -by 남성현", security = @SecurityRequirement(name = "JWT TOKEN"))
    public CustomResponse<ReportResponseDTO.ReportCommentResponseDTO> commentReport
            (@Parameter(description = "게시물 id") @PathVariable(name = "post-id") Integer postId,
             @Parameter(description = "댓글 id") @PathVariable(name = "comment-id") Integer commentId,
             @Valid @RequestBody ReportRequestDTO.NewReportRequestDTO request,
             @AuthenticationPrincipal Member loginUser) {

        Post post = communityPostService.getPost(postId);
        Comment comment = commentService.getComment(commentId);
        commentService.validateCommentByPostId(comment, postId);
        Report report = reportService.insertNewReport(ReportConverter.toReport(request,loginUser,null,comment));

        return CustomResponse.onSuccess(GeneralSuccessCode.NO_CONTENT_204,toReportCommentResponseDTO(report));
    }

}
