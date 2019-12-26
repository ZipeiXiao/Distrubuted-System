package demo;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.BSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Model {

    static MongoDatabase database;
    static MongoClient mongoClient;
    /**
     * table
     */
    static MongoCollection<DBObject> photos;


    /**
     * Given a URL sending request to API, return response as JSON formatted String
     * https://www.programcreek.com/java-api-examples/?class=java.net.URLConnection&method=connect
     * @param url the request url
     * @return response string
     */
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

            // 200 OK
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // close the connection
                in.close();
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // empty JSON string for photos
        return "{\"photos\":[]}";
    }

    /**
     * Connect to MongoDB database and get the collection back
     * @param mongodb_uri the mongoDB connection URL
     * @return the photo collection
     */
    public static MongoCollection<DBObject> getCollection(String mongodb_uri) {
        try {
            MongoClientURI uri = new MongoClientURI(mongodb_uri);
            if (uri != null) {
                mongoClient = new MongoClient(uri);

                //Database
                database = mongoClient.getDatabase("mars");
                if (database != null) {

                    //table
                    photos = database.getCollection("photos", DBObject.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photos;
    }

    /**
     * Insert one row of data to the database
     * https://www.programcreek.com/java-api-examples/?api=com.mongodb.MongoClient
     * @param responseObj response in JSON format
     */
    void mongoDBCloudInsert(BSONObject responseObj, String mongodb_uri) {
        try {
            photos = getCollection(mongodb_uri);
            photos.insertOne((DBObject) responseObj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mongoClient.close();
        }
    }

    /**
     * https://www.programcreek.com/java-api-examples/index.php?api=com.mongodb.client.MongoCursor
     * fetch all photos from db
     * @param mongodb_uri the mongoDB connection URL
     * @return list of logs
     */
    public static List<demo.Record> mongoDBCloudFetch(String mongodb_uri) {
        try {
            photos = getCollection(mongodb_uri);

            // prepare ways to traverse all rows we get
            FindIterable<DBObject> findIterable = photos.find();
            MongoCursor<DBObject> mongoCursor = findIterable.iterator();

            // add all log into a list to show in the dash board
            List<demo.Record> records = new ArrayList<demo.Record>();

            // prepare a map to count each ip and get the most frequent one
            Map<String, Integer> mapIp = new HashMap<String, Integer>();

            // prepare a map to count each date search term and get the most frequent one
            Map<String, Integer> mapDate = new HashMap<String, Integer>();

            // traverse all data record
            while (mongoCursor.hasNext()) {
                DBObject record = mongoCursor.next();
                //{"_id": {"$oid": "5db05cdb87bf025b0cc99faa"}, "photos": [{"id": 102685, "sol": 1004, "camera": {"id": 20, "name": "FHAZ", "rover_id": 5, "full_name": "Front Hazard Avoidance Camera"}, "img_src": "http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/fcam/FLB_486615455EDR_F0481570FHAZ00323M_.JPG", "earth_date": "2015-06-03", "rover": {"id": 5, "name": "Curiosity", "landing_date": "2012-08-06", "launch_date": "2011-11-26", "status": "active", "max_sol": 2540, "max_date": "2019-09-28", "total_photos": 366206, "cameras": [{"name": "FHAZ", "full_name": "Front Hazard Avoidance Camera"}, {"name": "NAVCAM", "full_name": "Navigation Camera"}, {"name": "MAST", "full_name": "Mast Camera"}, {"name": "CHEMCAM", "full_name": "Chemistry and Camera Complex"}, {"name": "MAHLI", "full_name": "Mars Hand Lens Imager"}, {"name": "MARDI", "full_name": "Mars Descent Imager"}, {"name": "RHAZ", "full_name": "Rear Hazard Avoidance Camera"}]}}, {"id": 102686, "sol": 1004, "camera": {"id": 20, "name": "FHAZ", "rover_id": 5, "full_name": "Front Hazard Avoidance Camera"}, "img_src": "http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/fcam/FRB_486615455EDR_F0481570FHAZ00323M_.JPG", "earth_date": "2015-06-03", "rover": {"id": 5, "name": "Curiosity", "landing_date": "2012-08-06", "launch_date": "2011-11-26", "status": "active", "max_sol": 2540, "max_date": "2019-09-28", "total_photos": 366206, "cameras": [{"name": "FHAZ", "full_name": "Front Hazard Avoidance Camera"}, {"name": "NAVCAM", "full_name": "Navigation Camera"}, {"name": "MAST", "full_name": "Mast Camera"}, {"name": "CHEMCAM", "full_name": "Chemistry and Camera Complex"}, {"name": "MAHLI", "full_name": "Mars Hand Lens Imager"}, {"name": "MARDI", "full_name": "Mars Descent Imager"}, {"name": "RHAZ", "full_name": "Rear Hazard Avoidance Camera"}]}}, {"id": 102842, "sol": 1004, "camera": {"id": 21, "name": "RHAZ", "rover_id": 5, "full_name": "Rear Hazard Avoidance Camera"}, "img_src": "http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/rcam/RLB_486615482EDR_F0481570RHAZ00323M_.JPG", "earth_date": "2015-06-03", "rover": {"id": 5, "name": "Curiosity", "landing_date": "2012-08-06", "launch_date": "2011-11-26", "status": "active", "max_sol": 2540, "max_date": "2019-09-28", "total_photos": 366206, "cameras": [{"name": "FHAZ", "full_name": "Front Hazard Avoidance Camera"}, {"name": "NAVCAM", "full_name": "Navigation Camera"}, {"name": "MAST", "full_name": "Mast Camera"}, {"name": "CHEMCAM", "full_name": "Chemistry and Camera Complex"}, {"name": "MAHLI", "full_name": "Mars Hand Lens Imager"}, {"name": "MARDI", "full_name": "Mars Descent Imager"}, {"name": "RHAZ", "full_name": "Rear Hazard Avoidance Camera"}]}}, {"id": 102843, "sol": 1004, "camera": {"id": 21, "name": "RHAZ", "rover_id": 5, "full_name": "Rear Hazard Avoidance Camera"}, "img_src": "http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01004/opgs/edr/rcam/RRB_486615482EDR_F0481570RHAZ00323M_.JPG", "earth_date": "2015-06-03", "rover": {"id": 5, "name": "Curiosity", "landing_date": "2012-08-06", "launch_date": "2011-11-26", "status": "active", "max_sol": 2540, "max_date": "2019-09-28", "total_photos": 366206, "cameras": [{"name": "FHAZ", "full_name": "Front Hazard Avoidance Camera"}, {"name": "NAVCAM", "full_name": "Navigation Camera"}, {"name": "MAST", "full_name": "Mast Camera"}, {"name": "CHEMCAM", "full_name": "Chemistry and Camera Complex"}, {"name": "MAHLI", "full_name": "Mars Hand Lens Imager"}, {"name": "MARDI", "full_name": "Mars Descent Imager"}, {"name": "RHAZ", "full_name": "Rear Hazard Avoidance Camera"}]}}]}
                BasicDBList arrayPhotos = (BasicDBList) record.get("photos");

                String user_ip = "";
                String earth_date = "";

                for (int i = 0; i < arrayPhotos.size(); i++) {

                    // get the photos JSONArray
                    DBObject dbobj = (DBObject) arrayPhotos.get(i);

                    // create a record object to hold all kinds of record
                    Record rec = new Record();

                    // get the data id
                    rec.setId(dbobj.get("id").toString());

                    // get the sequential number
                    rec.setSol(dbobj.get("sol").toString());

                    // get the camera BSONObject
                    BSONObject camera = (BSONObject) dbobj.get("camera");

                    // get the name of the camera
                    String cameraName = camera.get("full_name").toString();
                    rec.setCamera(cameraName);

                    // get the rover BSONObject
                    BSONObject rover = (BSONObject) dbobj.get("rover");

                    // get the name of the rover
                    String roverName = rover.get("name").toString();
                    rec.setRover(roverName);

                    // get the image url
                    rec.setImg_src(dbobj.get("img_src").toString());

                    // the search term
                    rec.setEarth_date(dbobj.get("earth_date").toString());

                    //user info
                    rec.setRequest_time(record.get("request_time").toString());
                    rec.setUser_ip(record.get("user_ip").toString());

                    // get the latency
                    rec.setLatency(record.get("latency") == null ? "360" : record.get("latency").toString());

                    // get the user location
                    rec.setLocal(record.get("local") == null ? "Pittsburgh" : record.get("local").toString());
                    user_ip = record.get("user_ip").toString();
                    earth_date = record.get("earth_date").toString();

                    records.add(rec);
                }

                if (!user_ip.isEmpty() && !earth_date.isEmpty()) {

                    // get statistic parameters
                    if (mapDate.containsKey(earth_date)) {
                        // count the frequency
                        mapDate.put(earth_date, mapDate.get(earth_date) + 1);
                    } else {
                        // if it appears the first time
                        mapDate.put(earth_date, 1);
                    }

                    if (mapIp.containsKey(user_ip)) {
                        // count the frequency
                        mapIp.put(user_ip, mapIp.get(user_ip) + 1);
                    } else {
                        // if it appears the first time
                        mapIp.put(user_ip, 1);
                    }
                }
            }

            // sort the time that an IP appears in desc order
            mapIp = sortDescend(mapIp);

            // sort the time that an search date term appears in desc order
            mapDate = sortDescend(mapDate);
            for (Map.Entry<String, Integer> entry : mapIp.entrySet()) {

                // get the most frequent IP
                records.get(0).setMost_IP(entry.getKey() + " count: (" + entry.getValue() + ")");
                break;
            }
            for (Map.Entry<String, Integer> entry : mapDate.entrySet()) {

                // get the most frequent data search term
                records.get(0).setMost_Date(entry.getKey() + " count: (" + entry.getValue() + ")");
                break;
            }

            return records;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sort the Map by the values in the desc order
     *
     * https://blog.csdn.net/weixin_33446857/article/details/85123772
     * @param map the map
     * @param <K> key
     * @param <V> value
     * @return sorted map
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortDescend(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> returnMap = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            returnMap.put(entry.getKey(), entry.getValue());
        }
        return returnMap;
    }
}
