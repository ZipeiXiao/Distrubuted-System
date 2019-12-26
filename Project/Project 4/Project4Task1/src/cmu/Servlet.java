package cmu;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//@WebServlet("/Servlet")
@WebServlet(name = "Servlet", urlPatterns = "/Servlet")
public class Servlet extends HttpServlet {
    private String mars_photo_api = "https://api.nasa.gov/mars-photos/api/v1/rovers/opportunity/photos";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String api_key = request.getParameter("api_key");
        String earth_date = request.getParameter("earth_date");

        if (api_key != null && earth_date != null) {
            String mars_photo_url = String.format("%s?api_key=%s&earth_date=%s&camera=pancam", mars_photo_api, api_key, earth_date);

            String responseBody = FetchPhotos(mars_photo_url);
            response.getWriter().append(responseBody);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    String FetchPhotos(String url) {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection urlCon = (HttpURLConnection) urlObj.openConnection();
            urlCon.setRequestMethod("GET");
            urlCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
            urlCon.setRequestProperty("Accept", "text/html");
            urlCon.setRequestProperty("Accept-Language", "en-US");
            urlCon.setRequestProperty("Connection", "close");

            int responseCode = urlCon.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                PictureModel m = new PictureModel();
                String result = m.getUsefulInfo(response.toString());
                return result;
                // return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"photos\":[]}";//empty JSON string for photos
    }
}
