package cat.itb.yapp.models.clinic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Modelo con los atributos necesarios para actualizar la clínica.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateClinicDto {
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
}
