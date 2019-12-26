package demo;

import com.mongodb.util.JSON;
import org.bson.BSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.text.SimpleDateFormat;

@WebServlet(name = "Servlet", urlPatterns = "/Servlet")
public class Servlet extends HttpServlet {
    public String mongodb_uri = "mongodb+srv://tina:distributedSystem@cluster0-ggb1k.mongodb.net/test?retryWrites=true&w=majority";
    public String mars_photo_api = "https://api.nasa.gov/mars-photos/api/v1/rovers/opportunity/photos";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // format of time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // the time we get request
        Date requestDate = new Date();

        // format request time
        String requestTime = sdf.format(requestDate);

        // get api key from the client
        String api_key = request.getParameter("api_key");

        // get the date that the user want to see picture
        String earth_date = request.getParameter("earth_date");

        if (api_key != null && earth_date != null) {
            String mars_photo_url = String.format("%s?api_key=%s&earth_date=%s", mars_photo_api, api_key, earth_date);

            // maybe timeout!!!
            Model m = new Model();

            //fetch json data form Api
            String responseBody = m.FetchPhotos(mars_photo_url);

            // put the response into a BSONObject
            BSONObject responseObj = (BSONObject) JSON.parse(responseBody);

            //add more user request information to db
            responseObj.put("request_time", requestTime);
            responseObj.put("user_ip", request.getRemoteAddr());
            responseObj.put("local", request.getLocalName());
            responseObj.put("earth_date", earth_date);

            Date responseDate = new Date();

            // calculate the latency
            long latency = responseDate.getTime() - requestDate.getTime();
            responseObj.put("latency", latency);

            // response json data to app
            response.getWriter().append(responseBody);

            // insert json data to mongodb
            m.mongoDBCloudInsert(responseObj, mongodb_uri);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
