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
import java.util.HashMap;
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
        Map<String, Object> customAttributes = new HashMap<>(attributes);

        // 카카오일 경우
        if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            String email = (String) kakaoAccount.get("email");

            if (email == null) {
                throw new OAuth2AuthenticationException("이메일 제공에 동의하지 않았습니다.");
            }

            String providerId = attributes.get("id").toString();

            customAttributes.put("email", email);
            customAttributes.put("provider", registrationId);
            customAttributes.put("providerId", providerId);
        }

        // 구글일 경우
        if ("google".equals(registrationId)) {
            String email = (String) attributes.get("email");
            String providerId = (String) attributes.get("sub"); // 구글은 "sub"이 고유 ID

            if (email == null) {
                throw new OAuth2AuthenticationException("이메일 제공에 동의하지 않았습니다.");
            }

            customAttributes.put("email", email);
            customAttributes.put("provider", registrationId);
            customAttributes.put("providerId", providerId);
        }


        // 기본적으로 ROLE_USER로 부여
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                customAttributes,
                "email"  // attributes에서 username으로 쓸 키
        );
    }
}
