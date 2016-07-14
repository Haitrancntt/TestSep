package Class;

/**
 * Created by Thanh Huy on 7/14/2016.
 */
public class Task {
    private String tag_name;
    private String name;

    public Task(String tag_name, String name) {
        this.tag_name = tag_name;
        this.name = name;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}