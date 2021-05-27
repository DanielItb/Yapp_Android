package cat.itb.yapp.models.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
/**
 * Modelo con los atributos necesarios para cargar y setear usuarios (especialistas) .
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
@Data
public class UserDto implements Serializable {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String surnames;
    private String photoUrl;
    private String phone;
    private String specialistType;
    private int collegiateNumber;
    private int clinicId;
    private List<String> roles = new ArrayList<>();
}
