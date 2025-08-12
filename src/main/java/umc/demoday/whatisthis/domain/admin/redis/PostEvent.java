package umc.demoday.whatisthis.domain.admin.redis;

public record PostEvent(
        Integer postId,
        ActionType action
) {
    public enum ActionType {
        CREATED_OR_UPDATED,
        DELETED
    }
}

