package ibs.news.dto.request;

import ibs.news.constrants.ValidationConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.util.Set;

@Data
public class NewsRequest {
    @NotNull(message = ValidationConstants.PARAM_PAGE_NOT_NULL)
    @Positive(message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1)
    private Integer page;

    @NotNull(message = ValidationConstants.PARAM_PER_PAGE_NOT_NULL)
    @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100)
    @Positive(message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
    private Integer perPage;

    private String author;

    private String keywords;

    private Set<String> tags;
}
