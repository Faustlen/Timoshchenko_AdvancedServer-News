package ibs.news.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LoginUserResponse {
    private UUID id;
    private String name;
    private String email;
    private String role;
    private String avatar;
    private String token;
}
