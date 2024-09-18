package ibs.news.dto.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomSuccessResponse<T> {

    private Boolean success = true;

    private Integer statusCode = 1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Integer> codes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    public CustomSuccessResponse(Integer statusCode, List<Integer> codes) {
        this.statusCode = statusCode;
        this.codes = codes;
    }

    public CustomSuccessResponse(T data) {
        this.data = data;
    }
}
