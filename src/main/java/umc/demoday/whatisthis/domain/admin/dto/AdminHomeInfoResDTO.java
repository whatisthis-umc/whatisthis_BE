package umc.demoday.whatisthis.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import umc.demoday.whatisthis.domain.notice.Notice;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AdminHomeInfoResDTO {
    private Reports reports;
    private Inquiries inquiries;
    private UserStats userStats;
    private ContentStats contentStats;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Reports {
        private int unprocessedCount;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Inquiries {
        private int unansweredCount;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserStats {
        private long totalUserCount;
        private int todaySignupCount;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ContentStats {
        private long postCount;
        private long commentCount;
    }
}
