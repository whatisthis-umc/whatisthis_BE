package umc.demoday.whatisthis.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.demoday.whatisthis.domain.admin.dto.AdminHomeInfoResDTO;
import umc.demoday.whatisthis.domain.admin.service.AdminHomeService;
import umc.demoday.whatisthis.global.apiPayload.CustomResponse;

@RestController
@RequestMapping("/admin/home")
@RequiredArgsConstructor
public class AdminHomeController {

    private final AdminHomeService adminHomeService;

    @GetMapping
    @Operation(summary = "관리자 메인페이지 대시보드 -by 윤영석")
    public CustomResponse<AdminHomeInfoResDTO> getAdminHomeInfo() {
        AdminHomeInfoResDTO response = adminHomeService.getAdminHomeInfo();
        return CustomResponse.ok(response);
    }
}
