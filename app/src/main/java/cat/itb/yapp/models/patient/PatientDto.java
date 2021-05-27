package cat.itb.yapp.models.patient;

import android.util.Log;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

import lombok.Data;
/**
 * Modelo con los atributos necesarios para cargar y setear pacientes.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
@Data
public class PatientDto implements Serializable {
    private Integer id;
    private String name;
    private String surname;
    private String reason;
    private String phoneNumber;
    private String email;
    private String urlPhoto;
    //
    private String dateOfBirth;
    private Float age;
    private String homeAddress;
    private String schoolName;
    private String course;
    private String paymentType;
    //clinic
    private Integer clinicId;

    public float getAge() {
        LocalDate date = LocalDate.parse(dateOfBirth);

        LocalDate today = LocalDate.now();
        LocalDate birthday;
        if (date!= null) {
            birthday = LocalDate.of(
                    date.getYear(),
                    date.getMonth(),
                    date.getDayOfMonth());
        } else {
            birthday = today;
        }


        return Period.between(birthday, today).getYears();
    }

}
