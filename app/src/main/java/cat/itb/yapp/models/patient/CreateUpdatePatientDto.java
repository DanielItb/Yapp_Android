package cat.itb.yapp.models.patient;

import lombok.Getter;
import lombok.Setter;
/**
 * Modelo con los atributos necesarios para actualizar y crear pacientes.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
@Getter
@Setter
public class CreateUpdatePatientDto {
    private Integer id;
    private String name;
    private String surname;
    private String reason;
    private String phoneNumber;
    private String email;
    private String urlPhoto;
    private String dateOfBirth;
    private String homeAddress;
    private String schoolName;
    private String course;
    private String paymentType;
    private Integer clinicId;
}
