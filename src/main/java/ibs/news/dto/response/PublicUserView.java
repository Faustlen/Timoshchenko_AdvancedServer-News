package ibs.news.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicUserView {
    private UUID id;
    private String name;
    private String email;
    private String role;
    private String avatar;
}
