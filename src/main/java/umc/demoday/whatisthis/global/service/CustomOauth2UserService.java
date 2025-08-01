package umc.demoday.whatisthis.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // registrationId: 어떤 소셜인지 구분 (kakao, google, naver 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth 제공자로부터 받은 정보
        Map<String, Object> attributes = oauth2User.getAttributes();

        // 카카오일 경우 파싱 방식 (여기서 직접 이메일 꺼낼 수 있음)
        if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            String email = (String) kakaoAccount.get("email");
            String nickname = (String) profile.get("nickname");

            // attributes에 이메일, 닉네임 넣어주기
            attributes.put("email", email);
            attributes.put("nickname", nickname);
        }

        // ⚠️ 기본적으로 ROLE_USER로 부여
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email"  // 👉 attributes에서 username으로 쓸 키
        );
    }
}
