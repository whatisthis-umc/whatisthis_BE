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
import umc.demoday.whatisthis.domain.member.dto.member.MyPageAccountDTO;
import umc.demoday.whatisthis.domain.post.repository.PostRepository;
import umc.demoday.whatisthis.domain.post_like.repository.PostLikeRepository;
import umc.demoday.whatisthis.domain.profile_image.ProfileImage;
import umc.demoday.whatisthis.domain.profile_image.repository.ProfileImageRepository;
import umc.demoday.whatisthis.domain.report.repository.ReportRepository;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;

import java.lang.module.FindException;
import java.time.LocalDateTime;

import static umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode.BAD_REQUEST_400;

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
    private final ProfileImageRepository profileImageRepository;

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

    @Override
    public Member updateMember(MyPageAccountDTO.MyPageAccountRequestDTO request, Member member, String url) {

        if (!member.getId().equals(request.getId())) {
            throw new GeneralException(BAD_REQUEST_400);
        }

        if (request.getNickname() != null && !member.getNickname().equals(request.getNickname())) {
            if (memberRepository.existsByNickname(request.getNickname())) {
                throw new GeneralException(GeneralErrorCode.ALREADY_EXIST_NICKNAME);
            }
            member.setNickname(request.getNickname());
        } else if (member.getNickname().equals(request.getNickname())) {
            throw new GeneralException(GeneralErrorCode.NICKNAME_SAME_AS_BEFORE);
        }

        if (request.getPassword() != null) {

            String password = passwordEncoder.encode(request.getPassword());
            if (password.equals(member.getPassword())) {
                throw new GeneralException(GeneralErrorCode.PASSWORD_SAME_AS_BEFORE);
            }
            member.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getModifyProfileImage()) {
            String newUrl = url;

            if (newUrl != null) {
                // 🔸 1. 기존 연결 제거
                if (member.getProfileImage() != null) {
                    ProfileImage oldImage = member.getProfileImage();
                    member.setProfileImage(null);
                    memberRepository.save(member);

                    memberRepository.flush();
                    profileImageRepository.delete(oldImage);
                }

                // 🔸 2. 새 이미지 저장
                ProfileImage profileImage = ProfileImage.builder()
                        .imageUrl(newUrl)
                        .build();
                profileImage.setMember(member);
                member.setProfileImage(profileImageRepository.save(profileImage));
            } else {
                // 삭제만 요청된 경우
                if (member.getProfileImage() != null) {
                    ProfileImage oldImage = member.getProfileImage();
                    member.setProfileImage(null);
                    memberRepository.save(member);
                    memberRepository.flush();
                    profileImageRepository.delete(oldImage);
                }
            }
        }

        return memberRepository.save(member);
    }
}
