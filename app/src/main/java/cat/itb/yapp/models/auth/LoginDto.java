package cat.itb.yapp.models.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Modelo con los atributos necesarios para hacer el login
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
public class LoginDto {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
