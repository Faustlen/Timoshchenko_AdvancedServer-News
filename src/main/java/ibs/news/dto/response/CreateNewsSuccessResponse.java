package ibs.news.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNewsSuccessResponse {

    private Long id;

    private Boolean success = true;

    private Integer statusCode = 1;

    public CreateNewsSuccessResponse(Long id) {
        this.id = id;
    }

}
