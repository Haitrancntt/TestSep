package Class;

import java.io.Serializable;

/**
 * Created by haitr on 7/26/2016.
 */
public class TimePassData implements Serializable {
    private int id;
    private String tagName, taskName, start, end;
    private int iEsHour, iEsMin, iRun, iDone;

    public TimePassData(int id, String tagName, String taskName, String start, String end, int iEsMin, int iEsHour, int iRun, int iDone) {
        this.id = id;
        this.tagName = tagName;
        this.taskName = taskName;
        this.iEsMin = iEsMin;
        this.iEsHour = iEsHour;
        this.iRun = iRun;
        this.iDone = iDone;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getiEsHour() {
        return iEsHour;
    }

    public void setiEsHour(int iEsHour) {
        this.iEsHour = iEsHour;
    }

    public int getiEsMin() {
        return iEsMin;
    }

    public void setiEsMin(int iEsMin) {
        this.iEsMin = iEsMin;
    }

    public int getiRun() {
        return iRun;
    }

    public void setiRun(int iRun) {
        this.iRun = iRun;
    }

    public int getiDone() {
        return iDone;
    }

    public void setiDone(int iDone) {
        this.iDone = iDone;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
