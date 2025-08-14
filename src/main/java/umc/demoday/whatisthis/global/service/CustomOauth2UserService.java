package umc.demoday.whatisthis.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
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
        String nameAttributeKey = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // kakao:id, google:sub, naver:id

        // OAuth 제공자로부터 받은 정보
        Map<String, Object> attributes = oauth2User.getAttributes();
        Map<String, Object> customAttributes = new HashMap<>(attributes);

        String email = null;
        String providerId = null;

        switch (registrationId) {
            case "kakao": {
                Object accObj = attributes.get("kakao_account");
                if (!(accObj instanceof Map)) {
                    throw oauthError("kakao_account 정보가 없습니다.");
                }
                Map<String, Object> kakaoAccount = (Map<String, Object>) accObj;
                email = (String) kakaoAccount.get("email"); // 동의/검수 안되면 null 가능
                if (email == null) {
                    throw oauthError("이메일 제공에 동의하지 않았습니다.");
                }
                Object idObj = attributes.get("id");
                if (idObj == null) {
                    throw oauthError("Kakao id를 확인할 수 없습니다.");
                }
                providerId = String.valueOf(idObj);
                break;
            }

            case "google": {
                email = (String) attributes.get("email");
                if (email == null) {
                    throw oauthError("이메일 제공에 동의하지 않았습니다.");
                }
                providerId = (String) attributes.get("sub"); // 구글 고유 ID
                if (providerId == null) {
                    throw oauthError("Google sub를 확인할 수 없습니다.");
                }
                break;
            }

            case "naver": {
                Object respObj = attributes.get("response");
                if (!(respObj instanceof Map)) {
                    throw oauthError("네이버 response 정보가 없습니다.");
                }
                Map<String, Object> response = (Map<String, Object>) respObj;
                email = (String) response.get("email");
                if (email == null) {
                    throw oauthError("이메일 제공에 동의하지 않았습니다.");
                }
                Object idObj = response.get("id");
                if (idObj == null) {
                    throw oauthError("Naver id를 확인할 수 없습니다.");
                }
                providerId = String.valueOf(idObj);
                break;
            }

            default:
                throw oauthError("지원하지 않는 provider: " + registrationId);
        }

        // 표준 키 주입
        customAttributes.put("email", email);
        customAttributes.put("provider", registrationId);
        customAttributes.put("providerId", providerId);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                customAttributes,
                nameAttributeKey // 표준 name attribute 사용(보통 id/sub)
        );
    }

    private OAuth2AuthenticationException oauthError(String message) {
        // error code는 팀 규칙에 맞게 지정 가능
        OAuth2Error error = new OAuth2Error("oauth2_validation_error", message, null);
        return new OAuth2AuthenticationException(error, message);
    }
}
