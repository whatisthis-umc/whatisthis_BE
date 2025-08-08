package umc.demoday.whatisthis.domain.post.service;

import org.springframework.data.domain.Page;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.post.Post;

public interface MyPagePostService {

    Page<Post> getMyPosts(Integer page, Integer size, Member member);
    void deletePost(Integer postId, Member member);
}
