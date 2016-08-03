package Class;

/**
 * Created by Thanh Huy on 8/3/2016.
 */
public class PieChartData {
    String name;
    int time;

    public PieChartData(String name, int time) {
        this.name = name;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
