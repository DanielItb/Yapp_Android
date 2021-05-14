package cat.itb.yapp.models.report;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateReportDto {
    private String diagnosis;
    private String objectives;
    private String date;
    private Integer treatmentId;
}
