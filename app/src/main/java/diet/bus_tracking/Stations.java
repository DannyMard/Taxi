package diet.bus_tracking;

public class Stations {
    String starttime,endtime,delaystarttime,delayendtime,destinaation,distance;

    public Stations(String starttime, String endtime, String delaystarttime, String delayendtime, String destinaation, String distance) {
        this.starttime = starttime;
        this.endtime = endtime;
        this.delaystarttime = delaystarttime;
        this.delayendtime = delayendtime;
        this.destinaation = destinaation;
        this.distance = distance;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getDelaystarttime() {
        return delaystarttime;
    }

    public void setDelaystarttime(String delaystarttime) {
        this.delaystarttime = delaystarttime;
    }

    public String getDelayendtime() {
        return delayendtime;
    }

    public void setDelayendtime(String delayendtime) {
        this.delayendtime = delayendtime;
    }

    public String getDestinaation() {
        return destinaation;
    }

    public void setDestinaation(String destinaation) {
        this.destinaation = destinaation;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
