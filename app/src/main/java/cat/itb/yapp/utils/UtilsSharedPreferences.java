package cat.itb.yapp.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import static android.content.Context.MODE_PRIVATE;

public class UtilsSharedPreferences {

    private static String YAPP_PREF_FILE = "YaapPreferencesFile";

    public static String getToken(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(YAPP_PREF_FILE, MODE_PRIVATE);
        return prefs.getString("token", "null");

    }

    public static void saveToken(Activity activity, String token) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(YAPP_PREF_FILE, MODE_PRIVATE).edit();
        editor.putString("token", token);
    }

}

