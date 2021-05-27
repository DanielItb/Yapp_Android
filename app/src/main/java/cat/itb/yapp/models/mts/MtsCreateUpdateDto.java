package cat.itb.yapp.models.mts;

import lombok.Data;
/**
 * Modelo con los atributos necesarios para actualizar una objeto Mts.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
@Data
public class MtsCreateUpdateDto {
    private int patientId;
    private Long specialistId;
    private int treatmentId;
    private String date;

}
