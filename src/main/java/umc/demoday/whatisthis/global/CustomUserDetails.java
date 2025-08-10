package umc.demoday.whatisthis.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Integer id; // Member 혹은 Admin ID 저장
    private final String username;
    private final String password;
    private final String role; // 역할 정보를 저장할 필드
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {// 계정이 만료되지 않았는지 리턴 (true: 만료 안됨)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {// 계정이 잠겨있지 않은지 리턴 (true: 잠기지 않음)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {// 비밀번호가 만료되지 않았는지 리턴 (true: 만료 안됨)
        return true;
    }

    @Override
    public boolean isEnabled() {// 계정이 활성화(사용 가능) 상태인지 리턴 (true: 활성화)
        return true;
    }


}
