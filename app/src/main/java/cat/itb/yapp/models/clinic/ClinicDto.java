package cat.itb.yapp.models.clinic;

import lombok.Data;

/**
 * Modelo con los datos necesarios de la clínica
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
@Data
public class ClinicDto {
    private Integer id;
    private String name;
    private String address;
    private String photo;
    private String phoneNumber;
    private String email;
    private String registerDate;

}
