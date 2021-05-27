package cat.itb.yapp.models.treatment;

import java.io.Serializable;

import lombok.Data;
/**
 * Modelo con los atributos necesarios para cargar y setear tratamientos.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
@Data
public class TreatmentDto implements Serializable {
    private String id;
    private String reason;
    private String sessionsFinished;
    private String startDate;
    private String patientId;
    private String patientFullName;
    private String patientAge;
    private String patientPhone;
    private String patientPhoto;
    private String specialistId;
    private String specialistFullName;
    private String specialistType;
}
