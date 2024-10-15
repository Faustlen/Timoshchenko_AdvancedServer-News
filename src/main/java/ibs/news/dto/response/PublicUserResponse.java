package ibs.news.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PublicUserResponse {
    private UUID id;

    private String name;

    private String email;

    private String role;

    private String avatar;
}
