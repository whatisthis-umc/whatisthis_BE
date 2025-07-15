package umc.demoday.whatisthis.controller.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.demoday.whatisthis.dto.member.MemberReqDTO;
import umc.demoday.whatisthis.dto.member.MemberResDTO;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;
import umc.demoday.whatisthis.service.member.MemberCommandService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/members")
public class MemberController {

    private final MemberCommandService memberCommandService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입 API -by 이정준")
    public CustomResponse<MemberResDTO.JoinResponseDTO> signup(
            @RequestBody @Valid MemberReqDTO.JoinRequestDTO request
    ) {
        MemberResDTO.JoinResponseDTO response = memberCommandService.signUp(request);
        return CustomResponse.created(response);
    }
}
