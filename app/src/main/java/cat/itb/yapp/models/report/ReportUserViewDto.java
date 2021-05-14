package cat.itb.yapp.models.report;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReportUserViewDto {
    private Integer id;
    private String diagnosis;
    private String objectives;
    private String date;
    // patient
    private Integer patientId;
    private String patientFullName;
    //specialist
    private Long specialistId;
    private String specialistFullName;
    private String specialistType;
    //treatment
    private Integer treatmentId;
}
