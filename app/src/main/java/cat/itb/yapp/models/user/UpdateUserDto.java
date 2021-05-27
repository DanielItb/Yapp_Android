package cat.itb.yapp.models.user;

import lombok.Getter;
import lombok.Setter;

/**
 * Modelo con los atributos necesarios para editar un usuario con rol user (básico) .
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
@Getter
@Setter
public class UpdateUserDto {
//    private String username;
//    private String email;
//    private Set<String> role;
//    private String password;
//    private String password2;
    private Boolean isAdminRole = false;//important Boolean no boolean !
    private String name;
    private String surnames;
    private String phone;
    private String specialistType;
    private int collegiateNumber;
    private boolean active;
}