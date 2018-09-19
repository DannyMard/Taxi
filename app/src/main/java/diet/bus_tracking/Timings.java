package diet.bus_tracking;

public class Timings {
    String busstop,timings;

    public Timings(String busstop, String timings) {
        this.busstop = busstop;
        this.timings = timings;
    }

    public String getBusstop() {
        return busstop;
    }

    public void setBusstop(String busstop) {
        this.busstop = busstop;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }
}
