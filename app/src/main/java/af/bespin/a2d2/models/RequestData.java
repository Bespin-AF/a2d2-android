package af.bespin.a2d2.models;

import java.io.Serializable;

public class RequestData implements Serializable {

    public String driver;
    public String status;
    public int groupSize;
    public String gender;
    public String name;
    public String phone;
    public String remarks;
    public String timestamp;
    public double lat;
    public double lon;

    public RequestData(){
        driver = "";
        status = "";
        gender = "";
        name = "";
        phone = "";
        remarks = "";
        timestamp = "";
        groupSize = -1;
        lat = 0;
        lon = 0;
    }
}
