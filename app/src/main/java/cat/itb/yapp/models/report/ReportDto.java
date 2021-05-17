package cat.itb.yapp.models.report;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReportDto implements Serializable {
    private Integer id;
    private String diagnosis;
    private String objectives;
    private String date;
    private Integer patientId;
    private String patientFullName;
    private Long specialistId;
    private String specialistFullName;
    private String specialistType;
    private Integer treatmentId;
}
