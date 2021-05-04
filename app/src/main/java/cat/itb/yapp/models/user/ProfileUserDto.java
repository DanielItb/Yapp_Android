package cat.itb.yapp.models.user;

import java.util.HashMap;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUserDto {
    private Integer id;
    private String username;
    private String photo;
    private Set<String> roles;
    private String accessToken;
}
