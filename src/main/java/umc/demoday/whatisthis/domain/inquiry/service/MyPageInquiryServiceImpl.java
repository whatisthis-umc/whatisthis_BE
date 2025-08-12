package umc.demoday.whatisthis.domain.inquiry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.demoday.whatisthis.domain.inquiry.Inquiry;
import umc.demoday.whatisthis.domain.inquiry.repository.InquiryRepository;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.member.repository.MemberRepository;
import umc.demoday.whatisthis.domain.post.code.PostErrorCode;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import static umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageInquiryServiceImpl implements MyPageInquiryService {

    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;

    @Override
    public Page<Inquiry> getMyInquiries(Integer page, Integer size, Member member) {

        if (page < 0 || size <= 0) {
            throw new GeneralException(PostErrorCode.INVALID_PAGE_REQUEST);
        }

        if (member == null) {
            throw new GeneralException(MEMBER_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(page, size);

        Member fetchedMember = memberRepository.findByIdWithProfileImage(member.getId())
                .orElseThrow(() -> new GeneralException(MEMBER_NOT_FOUND));


        return inquiryRepository.findAllByMember(member, pageable);
    }

    @Override
    public Inquiry getInquiryByIdAndMember(Integer id, Member member) {

        Inquiry inquiry = inquiryRepository.findById(id).orElseThrow(() -> new GeneralException(NOT_FOUND_404));

        if(!inquiry.getMember().getId().equals(member.getId())) {
            throw new GeneralException(BAD_REQUEST_400);
        }

        return inquiry;
    }

    @Override
    public void deleteInquiryById(Integer id, Member member) {

        Inquiry inquiry = inquiryRepository.findById(id).orElseThrow(() -> new GeneralException(NOT_FOUND_404));

        if(!inquiry.getMember().getId().equals(member.getId())) {
            throw new GeneralException(BAD_REQUEST_400);
        }

        inquiryRepository.delete(inquiry);
    }

}
