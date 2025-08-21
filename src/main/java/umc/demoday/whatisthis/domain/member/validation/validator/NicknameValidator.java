package umc.demoday.whatisthis.domain.member.validation.validator;


import java.util.regex.Pattern;

public final class NicknameValidator {

    private static final Pattern KOR_ENG_NUM = Pattern.compile("^[\\p{IsHangul}A-Za-z0-9]+$");

    private NicknameValidator() {}

    public static boolean isOnlyKorEngNum(String nickname) {
        if (nickname == null) return false;
        String n = nickname.trim();
        if (n.isEmpty()) return false;
        return KOR_ENG_NUM.matcher(n).matches();
    }
}
