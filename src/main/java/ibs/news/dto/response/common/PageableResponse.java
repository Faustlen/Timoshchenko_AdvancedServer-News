package ibs.news.dto.response.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageableResponse<T> {

    private T content;

    private Long numberOfElements;

    public PageableResponse(T content, Long numberOfElements) {
        this.content = content;
        this.numberOfElements = numberOfElements;
    }
}
