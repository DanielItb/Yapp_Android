package cat.itb.yapp.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * Almacena el Json Web Token (JWT), necesario para hacer las peticiones. Las almacena en el fichero SharePreferences del dispositivo móvil.
 * @author David Lama, Kenneth Griñan, Daniel Acosta
 */
public class UtilsSharedPreferences {

    private static String YAPP_PREF_FILE = "YaapPreferencesFile";

    public static String KEY_TOKEN_FILE = "accessToken";


    public static String getToken (Activity activity) {

        SharedPreferences prefs = activity.getSharedPreferences(YAPP_PREF_FILE, MODE_PRIVATE);
        return prefs.getString(KEY_TOKEN_FILE, "null");

    }

    public static String getKey(Activity activity, String key) {
        SharedPreferences prefs = activity.getSharedPreferences(YAPP_PREF_FILE, MODE_PRIVATE);
        return prefs.getString(key, "null");
    }

    /**
     * Método que setea el token del usuario
     * @param activity
     * @param token
     */
    public static void setToken(Activity activity, String token) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(YAPP_PREF_FILE, MODE_PRIVATE).edit();
        editor.putString(KEY_TOKEN_FILE, token);
        editor.apply();
        // editor.commit();
        /*Consider using apply() instead; commit writes its data to persistent storage immediately, whereas apply will handle it in the background*/

    }

}



