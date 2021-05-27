package cat.itb.yapp.models.mts;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
/**
 * Modelo con los atributos necesarios para cargar y setear Mts (citas).
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
@Data
public class MtsDto implements Serializable{
    /// mts
    private Integer id;
    private String date;
    // patient
    private Integer patientId;
    private String patientFullName;
    private String patientAge;
    private String patientPhone;
    private String patientPhoto;
    // specialist
    private Long specialistId;
    private String specialistFullName;
    private String specialistType;
    //treatment
    private Integer treatmentId;
    private String reason;
    //clinic
    private Integer clinicId;

}

