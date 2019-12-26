import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureModel {

    // delete unnecessary JSON fields and add new information by calculation
    public String getUsefulInfo(String input) {

        // convert string into a JSONObject
        JsonObject returnData = new JsonParser().parse(input).getAsJsonObject();

        // get the photos JsonArray
        JsonArray photos = returnData.get("photos").getAsJsonArray();

        // create a new array to hold useful photos
        JsonArray newPhotos = new JsonArray();
        JsonObject result = new JsonObject();

        // traverse all photos in the array
        for (int i = 0; i < photos.size(); i++) {
            JsonObject photo = photos.get(i).getAsJsonObject();

            // format of date
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

            // calculate how many days the rover has spent on the Mars
            long daysNumber = (earth_date_time.getTime() - landing_date_time.getTime()) / (24 * 3600 * 1000L);

            // add new field into the result
            photo.addProperty("DaysOnMars", daysNumber);

            // add new photo JSONObject into Array
            newPhotos.add(photo);
        }
        result.add("photos", newPhotos);
        return result.toString();
    }
}
