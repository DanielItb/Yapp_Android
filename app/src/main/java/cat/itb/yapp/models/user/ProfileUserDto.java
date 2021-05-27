package cat.itb.yapp.models.user;

import java.util.HashMap;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
/**
 * Modelo con los atributos necesarios para almacenar los datos de seguridad que devuelve el backend al hacer login.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
@Getter
@Setter
public class ProfileUserDto {
    private Integer id;
    private String username;
    private String photo;
    private Set<String> roles;
    private String accessToken;
    private String email;
}
