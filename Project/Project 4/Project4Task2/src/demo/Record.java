package demo;

/**
 * This class represent the columns in the dashboard
 */
public class Record {
    private String id;
    private String sol;
    private String img_src;
    private String earth_date;
    private String camera;
    private String rover;

    //user request date
    private String request_time;

    //user IP
    private String user_ip;
    private String most_IP;
    private String most_Date;
    private String latency;
    private String local;

    public void setLatency(String latency) {
        this.latency = latency;
    }

    public String getLatency() {
        return latency;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getLocal() {
        return local;
    }

    public void setMost_IP(String mIP) {
        most_IP = mIP;
    }

    public String getMost_IP() {
        return most_IP;
    }

    public void setMost_Date(String mDate) {
        most_Date = mDate;
    }

    public String getMost_Date() {
        return most_Date;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSol() {
        return sol;
    }

    public void setSol(String sol) {
        this.sol = sol;
    }

    public String getImg_src() {
        return img_src;
    }

    public void setImg_src(String img_src) {
        this.img_src = img_src;
    }

    public String getEarth_date() {
        return earth_date;
    }

    public void setEarth_date(String earth_date) {
        this.earth_date = earth_date;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getRover() {
        return rover;
    }

    public void setRover(String rover) {
        this.rover = rover;
    }

    public Record() {
        super();
    }
}
