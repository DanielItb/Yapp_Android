package cat.itb.yapp.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;

public class ErrorUtils {
    public static String getErrorString(ResponseBody responseBody) {
        String response = "";
        JsonParser jsonParser = new JsonParser();
        try {
            JsonObject json = jsonParser.parse(responseBody.string()).getAsJsonObject();
            response = json.get("message").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
