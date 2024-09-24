package ibs.news.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewsSuccessResponse {

    private Long id;

    private Boolean success = true;

    private Integer statusCode = 1;

}
