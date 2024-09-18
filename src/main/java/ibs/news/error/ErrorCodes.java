package ibs.news.error;

import ibs.news.constrants.ValidationConstants;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum ErrorCodes {
    UNKNOWN(0, "unknown"),
    USERNAME_SIZE_NOT_VALID(1, ValidationConstants.USERNAME_SIZE_NOT_VALID),
    ROLE_SIZE_NOT_VALID(2, ValidationConstants.ROLE_SIZE_NOT_VALID),
    EMAIL_SIZE_NOT_VALID(3, ValidationConstants.EMAIL_SIZE_NOT_VALID),
    MUST_NOT_BE_NULL(4, "must not be null"),
    USER_NOT_FOUND(5, "Could not find user"),
    TOKEN_NOT_PROVIDED(6, ValidationConstants.TOKEN_NOT_PROVIDED),
    UNAUTHORISED(7, "unauthorised"),
    USER_EMAIL_NOT_NULL(8, ValidationConstants.USER_EMAIL_NOT_NULL),
    USER_PASSWORD_NOT_VALID(9, ValidationConstants.USER_PASSWORD_NOT_VALID),
    USER_ROLE_NOT_NULL(10, ValidationConstants.USER_ROLE_NOT_NULL),
    NEWS_DESCRIPTION_SIZE(11, ValidationConstants.NEWS_DESCRIPTION_SIZE_NOT_VALID),
    NEWS_DESCRIPTION_NOT_NULL(12, ValidationConstants.NEWS_DESCRIPTION_HAS_TO_BE_PRESENT),
    NEWS_TITLE_SIZE(13, ValidationConstants.NEWS_TITLE_SIZE),
    NEWS_TITLE_NOT_NULL(14, ValidationConstants.NEWS_TITLE_NOT_NULL),
    PARAM_PAGE_NOT_NULL(15, ValidationConstants.PARAM_PAGE_NOT_NULL),
    PARAM_PER_PAGE_NOT_NULL(16, ValidationConstants.PARAM_PER_PAGE_NOT_NULL),
    USER_EMAIL_NOT_VALID(17, ValidationConstants.USER_EMAIL_NOT_VALID),
    PAGE_SIZE_NOT_VALID(18, ValidationConstants.PAGE_SIZE_NOT_VALID),
    PER_PAGE_MIN_NOT_VALID(19, ValidationConstants.PER_PAGE_MIN_NOT_VALID),
    PER_PAGE_MAX_NOT_VALID(19, ValidationConstants.PER_PAGE_MAX_NOT_VALID),
    CODE_NOT_NULL(20, "Required String parameter 'code' is not present"),
    EXCEPTION_HANDLER_NOT_PROVIDED(21, "Exception handler not provided"),
    REQUEST_IS_NOT_MULTIPART(22, "Current request is not a multipart request"),
    MAX_UPLOAD_SIZE_EXCEEDED(23, ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED),
    USER_AVATAR_NOT_NULL(24, ValidationConstants.USER_AVATAR_NOT_NULL),
    PASSWORD_NOT_VALID(25, ValidationConstants.PASSWORD_NOT_VALID),
    PASSWORD_NOT_NULL(26, ValidationConstants.USER_PASSWORD_NULL),
    NEWS_NOT_FOUND(27, "news not found"),
    ID_MUST_BE_POSITIVE(29, ValidationConstants.ID_MUST_BE_POSITIVE),
    USER_ALREADY_EXISTS(30, "User already exists"),
    TODO_TEXT_NOT_NULL(31, ValidationConstants.TODO_TEXT_NOT_NULL),
    TODO_TEXT_SIZE_NOT_VALID(32, ValidationConstants.TODO_TEXT_SIZE_NOT_VALID),
    TODO_STATUS_NOT_NULL(33, ValidationConstants.TODO_STATUS_NOT_NULL),
    TASK_NOT_FOUND(34, ValidationConstants.TASK_NOT_FOUND),
    TASK_PATCH_UPDATED_NOT_CORRECT_COUNT(35, ValidationConstants.TASK_PATCH_UPDATED_NOT_CORRECT_COUNT),
    TASKS_PAGE_GREATER_OR_EQUAL_1(37, ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1),
    TASKS_PER_PAGE_GREATER_OR_EQUAL_1(38, ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1),
    TASKS_PER_PAGE_LESS_OR_EQUAL_100(39, ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100),
    REQUIRED_INT_PARAM_PAGE_IS_NOT_PRESENT(40, ValidationConstants.REQUIRED_INT_PARAM_PAGE_IS_NOT_PRESENT),
    REQUIRED_INT_PARAM_PER_PAGE_IS_NOT_PRESENT(41, ValidationConstants.REQUIRED_INT_PARAM_PER_PAGE_IS_NOT_PRESENT),
    USER_NAME_HAS_TO_BE_PRESENT(43, ValidationConstants.USER_NAME_HAS_TO_BE_PRESENT),
    TAGS_NOT_VALID(44, ValidationConstants.TAGS_NOT_VALID),
    NEWS_IMAGE_HAS_TO_BE_PRESENT(45, ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT),
    USER_WITH_THIS_EMAIL_ALREADY_EXIST(46, ValidationConstants.USER_WITH_THIS_EMAIL_ALREADY_EXIST),
    HTTP_MESSAGE_NOT_READABLE_EXCEPTION(47, ValidationConstants.HTTP_MESSAGE_NOT_READABLE_EXCEPTION);

    private static final Map<String, Integer> errorCodes = new HashMap<>();

    private final Integer errorCode;

    private final String errorMessage;

    private final LocalDateTime timeStamp;

    ErrorCodes(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.timeStamp = LocalDateTime.now();
    }

    static {
        Arrays.stream(ErrorCodes.values())
                .forEach(error -> errorCodes.put(error.errorMessage, error.errorCode));
    }

    public static Integer getErrorCodeByMessage(String message) {
        return errorCodes.get(message);
    }
}
