package Class;

/**
 * Created by haitr on 8/5/2016.
 */
public class BarChartData {
    String sName;
    int iActTime, iEstTime;
    public BarChartData(String sName, int iActTime, int iEstTime) {

        this.sName = sName;
        this.iActTime = iActTime;
        this.iEstTime = iEstTime;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public int getiActTime() {
        return iActTime;
    }

    public void setiActTime(int iActTime) {
        this.iActTime = iActTime;
    }

    public int getiEstTime() {
        return iEstTime;
    }

    public void setiEstTime(int iEstTime) {
        this.iEstTime = iEstTime;
    }


}
