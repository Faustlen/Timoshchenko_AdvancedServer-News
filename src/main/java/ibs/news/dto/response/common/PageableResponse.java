package ibs.news.dto.response.common;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class PageableResponse<T> {
    private List<T> content;

    private Long numberOfElements;

    public PageableResponse(List<T> content, Long numberOfElements) {
        this.content = content;
        this.numberOfElements = numberOfElements;
    }
}
