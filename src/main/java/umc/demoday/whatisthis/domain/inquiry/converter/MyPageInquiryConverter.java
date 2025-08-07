package umc.demoday.whatisthis.domain.inquiry.converter;

import org.springframework.data.domain.Page;
import umc.demoday.whatisthis.domain.inquiry.Inquiry;
import umc.demoday.whatisthis.domain.inquiry.dto.resDTO.MyPageInquiryResponseDTO;
import umc.demoday.whatisthis.domain.member.Member;
import umc.demoday.whatisthis.domain.profile_image.ProfileImage;
import umc.demoday.whatisthis.global.apiPayload.exception.GeneralException;

import java.util.List;
import java.util.Optional;

import static umc.demoday.whatisthis.global.apiPayload.code.GeneralErrorCode.MEMBER_NOT_FOUND;

public class MyPageInquiryConverter {

    public static MyPageInquiryResponseDTO.MyInquiryPageDTO toMyInquiryPageDTO (Page<Inquiry> inquiryList) {

        List<MyPageInquiryResponseDTO.MyInquiryDTO> myInquiryDTOList = inquiryList.stream()
                .map(MyPageInquiryConverter::toMyInquiryDTO).toList();

        Member author = inquiryList.getContent().stream()
                .map(Inquiry::getMember)
                .findFirst()
                .orElseThrow(() -> new GeneralException(MEMBER_NOT_FOUND));

        String profileImageUrl = Optional.ofNullable(author.getProfileImage())
                .map(ProfileImage::getImageUrl)
                .orElse(null);

        return MyPageInquiryResponseDTO.MyInquiryPageDTO.builder()
                .nickname(author.getNickname())
                .profileImageUrl(author.getProfileImage().getImageUrl())
                .email(author.getEmail())
                .inquiries(myInquiryDTOList)
                .build();
    }

    public static MyPageInquiryResponseDTO.MyInquiryDTO toMyInquiryDTO (Inquiry inquiry) {

        return MyPageInquiryResponseDTO.MyInquiryDTO.builder()
                .inquiryId(inquiry.getId())
                .title(inquiry.getTitle())
                .status(inquiry.getStatus())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }

    public static MyPageInquiryResponseDTO.MyInquiryDetailDTO toMyInquiryDetailDTO (Inquiry inquiry) {

        return MyPageInquiryResponseDTO.MyInquiryDetailDTO.builder()
                .inquiryId(inquiry.getId())
                .title(inquiry.getTitle())
                .inquiryContent(inquiry.getContent())
                .answerContent(
                        inquiry.getAnswer() != null ? inquiry.getAnswer().getContent() : null
                )
                .createdAt(inquiry.getCreatedAt())
                .build();
    }

}
