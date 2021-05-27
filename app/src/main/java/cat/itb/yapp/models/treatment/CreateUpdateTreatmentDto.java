package cat.itb.yapp.models.treatment;

import lombok.Getter;
import lombok.Setter;
/**
 * Modelo con los atributos necesarios para crear y actualizar tratamientos.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
@Getter @Setter
public class CreateUpdateTreatmentDto {
    // treatment
    private String reason;
    private int sessionsFinished;
    private String startDate;
    private int patientId;
    private Long userId;
}
