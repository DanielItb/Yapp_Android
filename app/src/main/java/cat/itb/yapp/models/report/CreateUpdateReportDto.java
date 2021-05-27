package cat.itb.yapp.models.report;

import lombok.Getter;
import lombok.Setter;
/**
 * Modelo con los atributos necesarios para crear y actualizar reports (informes).
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
@Getter @Setter
public class CreateUpdateReportDto {
    private String diagnosis;
    private String objectives;
    private String date;
    private Integer treatmentId;
}
