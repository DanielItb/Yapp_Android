package cat.itb.yapp.models.report;

import java.io.Serializable;

import lombok.Data;

@Data
public class ReportDto implements Serializable {
    private String id;
    private String diagnosis;
    private String objectives;
    private String date;
    private String patientId;
    private String patientFullName;
    private String specialistId;
    private String specialistFullName;
    private String specialistType;
}
