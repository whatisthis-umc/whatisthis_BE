package umc.demoday.whatisthis.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.post.Post;
import umc.demoday.whatisthis.domain.post.converter.PostConverter;
import umc.demoday.whatisthis.domain.post.dto.PostResponseDTO;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostResponseDTO.GgulPostResponseDTO getGgulPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND_404));
        return PostConverter.to
    }
}
