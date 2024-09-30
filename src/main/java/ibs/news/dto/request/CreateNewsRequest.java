package ibs.news.dto.request;

import ibs.news.constrants.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.Set;

@Getter
public class CreateNewsRequest {

    @NotBlank(message = ValidationConstants.NEWS_TITLE_NOT_NULL)
    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_TITLE_SIZE)
    private String title;

    @NotBlank(message = ValidationConstants.NEWS_DESCRIPTION_HAS_TO_BE_PRESENT)
    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_DESCRIPTION_SIZE_NOT_VALID)
    private String description;

    @NotBlank(message = ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT)
    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT)
    private String image;

    private Set<@NotBlank(message = ValidationConstants.TAGS_NOT_VALID) String> tags;
}
