package cat.itb.yapp.utils;

import java.util.Set;

/**
 * Clase utils varios para comprobar el rol del usuario conectado a la aplicación.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
public class UtilsAuth {

    private final static String ADMIN_ROLE = "ROLE_ADMIN";
    private final static String USER_ROLE = "ROLE_USER";

    public static boolean getIsAdminRole(Set<String> roles) {
        return roles.contains(ADMIN_ROLE);
    }

    public static boolean getIsUserRole(Set<String> roles) {
        return roles.contains(USER_ROLE);
    }


}
