package ibs.news.dto.response.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseSuccessResponse {
    private final Boolean success = true;

    private final Integer statusCode = 1;
}
