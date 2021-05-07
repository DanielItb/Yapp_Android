package cat.itb.yapp.models.clinic;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ClinicDto {
    private Integer id;
    private String name;
    private String address;
    private String photo;
    private String phoneNumber;
    private LocalDateTime registerDate;
    private boolean active = true;
}
