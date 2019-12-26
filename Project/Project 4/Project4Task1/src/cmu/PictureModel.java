package cmu;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureModel {
    String getUsefulInfo(String input) {
        JsonObject returnData = new JsonParser().parse(input).getAsJsonObject();
        JsonArray photos = returnData.get("photos").getAsJsonArray();
        JsonArray newPhotos = new JsonArray();
        JsonObject result = new JsonObject();

        for (int i = 0; i < photos.size(); i++) {
            JsonObject photo = photos.get(i).getAsJsonObject();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date earth_date_time = new Date();
            Date landing_date_time = new Date();
            try {
                earth_date_time = format.parse(photo.get("earth_date").getAsString());//"2015-06-03"

                JsonObject rover = photo.get("rover").getAsJsonObject();
                landing_date_time = format.parse(rover.get("landing_date").getAsString());//"2004-01-25"
            } catch (Exception e) {
                e.printStackTrace();
            }

            long daysNumber = (earth_date_time.getTime() - landing_date_time.getTime()) / (24 * 3600 * 1000L);
            photo.addProperty("DaysOnMars", daysNumber);
            System.out.println(photo.toString());
            newPhotos.add(photo);
            // System.out.println(newPhotos.toString());
        }
        result.add("photos", newPhotos);
        // System.out.println(result.toString());
        return result.toString();
    }
}
