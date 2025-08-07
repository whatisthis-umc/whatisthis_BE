package umc.demoday.whatisthis.domain.post.service;

import org.springframework.data.domain.Page;
import umc.demoday.whatisthis.domain.comment.Comment;
import umc.demoday.whatisthis.domain.hashtag.Hashtag;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.dto.PostRequestDTO;
import umc.demoday.whatisthis.domain.post.enums.Category;
import umc.demoday.whatisthis.domain.post.enums.SortBy;
import umc.demoday.whatisthis.domain.post_image.PostImage;

import java.util.List;

public interface CommunityPostService {

    Page<Post> getAllPosts(Integer page, Integer size, SortBy sort);
    Page<Post> getBestPosts(Integer page, Integer size);
    Page<Post> getAllPostsByCategory(Integer page, Integer size, SortBy sort, Category category);

    Post insertNewPost (PostRequestDTO.NewPostRequestDTO request, List<String> imageUrls, Member member);
    Post updatePost(Integer postId,PostRequestDTO.ModifyPostRequestDTO request, List<String> imageUrls, Member member);

    Post getPost(Integer id);
    Page<Comment> getCommentListByPost(Integer page, Integer size, SortBy sort, Post post);
    List<Hashtag> getHashtagListByPost(Post post);
    List<PostImage> getPostImageListByPost(Post post);
    void plusOneViewCount(Post post);

    void likePost(Post post, Member member);
    void unLikePost(Post post, Member member);
}
