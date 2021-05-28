package cat.itb.yapp.utils;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;

public class ErrorUtils {
    public static String getErrorString(@Nullable ResponseBody responseBody) {
        String response = "Error";
        JsonParser jsonParser = new JsonParser();

        if (responseBody != null) {
            try {
                JsonObject json = jsonParser.parse(responseBody.string()).getAsJsonObject();
                response = json.get("message").getAsString();
                if (response.isEmpty()) response = json.get("error").getAsString();
                if (response.isEmpty()) response = "Error";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
    }
}
