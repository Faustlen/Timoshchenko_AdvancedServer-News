package ibs.news.dto.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomSuccessResponse<T> {
    private Long id;

    private Boolean success = true;

    private Integer statusCode = 1;

    private T data;

    private List<Integer> codes;

    private String error;

    public CustomSuccessResponse(Integer statusCode, List<Integer> codes) {
        this.statusCode = statusCode;
        this.codes = codes;
    }

    public CustomSuccessResponse(T data) {
        this.data = data;
    }

    public CustomSuccessResponse(Long id) {
        this.id = id;
    }
}
