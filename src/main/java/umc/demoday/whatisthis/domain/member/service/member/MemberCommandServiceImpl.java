package umc.demoday.whatisthis.domain.member.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.comment.repository.CommentRepository;
import umc.demoday.whatisthis.domain.comment_like.repository.CommentLikeRepository;
import umc.demoday.whatisthis.domain.member.converter.MemberConverter;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.MemberResDTO;
import umc.demoday.whatisthis.domain.member.dto.member.SocialLinkReqDTO;
import umc.demoday.whatisthis.domain.member.dto.member.SocialSignupReqDTO;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.post_like.repository.PostLikeRepository;
import umc.demoday.whatisthis.domain.report.repository.ReportRepository;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate redisTemplate;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ReportRepository reportRepository;

    @Override
    public MemberResDTO.JoinResponseDTO signUp(MemberReqDTO.JoinRequestDTO dto) {

        // 약관 동의 검사
        if (!Boolean.TRUE.equals(dto.getServiceAgreed())) {
            throw new GeneralException(GeneralErrorCode.TERMS_REQUIRED);
        }

        if (!Boolean.TRUE.equals(dto.getPrivacyAgreed())) {
            throw new GeneralException(GeneralErrorCode.TERMS_REQUIRED);
        }

        // 이메일 인증 코드 검사
        String redisAuthCode = redisTemplate.opsForValue()
                .get("EMAIL_AUTH:" + dto.getEmail());

        if (!redisAuthCode.equals(dto.getEmailAuthCode())) {
            throw new GeneralException(GeneralErrorCode.EMAIL_AUTH_CODE_MISMATCH);
        }


        // 아이디 중복 검사
        if (memberRepository.existsByMemberId(dto.getMemberId())) {
            throw new GeneralException(GeneralErrorCode.ALREADY_EXIST_MEMBER_ID);
        }

        // 닉네임 중복 검사
        if (memberRepository.existsByNickname(dto.getNickname())) {
            throw new GeneralException(GeneralErrorCode.ALREADY_EXIST_NICKNAME);
        }

        // DTO -> Entity
        Member newMember = MemberConverter.toMember(dto, passwordEncoder);

        // 회원 저장
        memberRepository.save(newMember);

        // Entity -> ResponseDTO
        return MemberConverter.toJoinResponseDTO(newMember);
    }

    @Override
    public MemberResDTO.JoinResponseDTO signUpSocial(SocialSignupReqDTO dto) {

        if (memberRepository.existsByNickname(dto.getNickname())) {
            throw new GeneralException(GeneralErrorCode.ALREADY_EXIST_NICKNAME);
        }

        if (!Boolean.TRUE.equals(dto.getServiceAgreed())) {
            throw new GeneralException(GeneralErrorCode.TERMS_REQUIRED);
        }

        if (!Boolean.TRUE.equals(dto.getPrivacyAgreed())) {
            throw new GeneralException(GeneralErrorCode.TERMS_REQUIRED);
        }

        Member newMember = Member.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .provider(dto.getProvider())
                .providerId(dto.getProviderId())
                .serviceAgreed(dto.getServiceAgreed())
                .privacyAgreed(dto.getPrivacyAgreed())
                .build();

        memberRepository.save(newMember);
        return new MemberResDTO.JoinResponseDTO(newMember.getNickname());
    }


    @Override
    public void evaluateIsBest(Member member) {
        Integer memberId = member.getId();
        LocalDateTime createdAt = member.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();


        LocalDateTime sixtyDaysAgo = now.minusDays(60);
        Integer recentPosts = postRepository.countByMemberIdAndCreatedAtAfter(memberId, sixtyDaysAgo);
        Integer recentComments = commentRepository.countByMemberIdAndCreatedAtAfter(memberId, sixtyDaysAgo);
        Integer totalLikes = commentLikeRepository.countByMember(member)
                + postLikeRepository.countByMember(member);

        member.setIsBest(false);

        if (createdAt.isAfter(now.minusDays(30))) {
            if ((recentPosts >= 5) && (recentComments >= 10) && (totalLikes >= 30)) {
                member.setIsBest(true);
            }
        }

        else {
            if ((recentPosts >= 10) && (recentComments >= 20) && (totalLikes >= 50)
                && (reportRepository.countReportsByReportedMemberId(memberId) ==0)) {
                member.setIsBest(true);
            }
        }
    }

    public void linkSocial(SocialLinkReqDTO dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.MEMBER_NOT_FOUND));

        if (member.getProvider() != null) {
            throw new GeneralException(GeneralErrorCode.ALREADY_SOCIAL_LINKED);
        }

        member.linkSocial(dto.getProvider(), dto.getProviderId());
        memberRepository.save(member);
    }
}
