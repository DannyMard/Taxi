package diet.bus_tracking;

public class Bus {
    String busno,status,dateandtime;
    Double lat,longi;

    public Bus(){}
    public Bus(String busno, String dateandtime,Double lat,Double longi, String status) {
        this.busno = busno;
        this.status = status;
        this.dateandtime = dateandtime;this.lat = lat;this.longi=longi;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public String getBusno() {
        return busno;
    }

    public void setBusno(String busno) {
        this.busno = busno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateandtime() {
        return dateandtime;
    }

    public void setDateandtime(String dateandtime) {
        this.dateandtime = dateandtime;
    }
}
