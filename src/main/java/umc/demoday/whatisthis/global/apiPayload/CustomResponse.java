package umc.demoday.whatisthis.global.apiPayload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import umc.demoday.whatisthis.global.apiPayload.code.BaseSuccessCode;
import umc.demoday.whatisthis.global.apiPayload.code.GeneralSuccessCode;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class CustomResponse<T> {

    @JsonProperty("isSuccess")
    private boolean isSuccess;

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("result")
    private final T result;

    public static <T> CustomResponse<T> ok(T result) {
        return onSuccess(GeneralSuccessCode.OK, result);
    }

    public static <T> CustomResponse<T> created(T result) {
        return onSuccess(GeneralSuccessCode.CREATED, result);
    }

    public static <T> CustomResponse<T> onSuccess(BaseSuccessCode code, T result) {
        return new CustomResponse<>(true, code.getCode(), code.getMessage(), result);
    }

    public static <T> CustomResponse<T> onFailure(String code, String message) {
        return onFailure(code, message, null);
    }

    public static <T> CustomResponse<T> onFailure(String code, String message, T result) {
        return new CustomResponse<>(false, code, message, result);
    }

}