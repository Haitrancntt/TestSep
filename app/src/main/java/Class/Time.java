package Class;

/**
 * Created by Thanh Huy on 7/22/2016.
 */
public class Time {
    private String tagName, taskName;
    private int esHour, esMin, acHour, acMin;
    private String start, end;

    public Time(String tagName, String taskName, int esHour, int esMin, int acHour, int acMin, String start, String end) {
        this.tagName = tagName;
        this.taskName = taskName;
        this.esHour = esHour;
        this.esMin = esMin;
        this.acHour = acHour;
        this.acMin = acMin;
        this.start = start;
        this.end = end;
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

    public int getEsHour() {
        return esHour;
    }

    public void setEsHour(int esHour) {
        this.esHour = esHour;
    }

    public int getEsMin() {
        return esMin;
    }

    public void setEsMin(int esMin) {
        this.esMin = esMin;
    }

    public int getAcHour() {
        return acHour;
    }

    public void setAcHour(int acHour) {
        this.acHour = acHour;
    }

    public int getAcMin() {
        return acMin;
    }

    public void setAcMin(int acMin) {
        this.acMin = acMin;
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