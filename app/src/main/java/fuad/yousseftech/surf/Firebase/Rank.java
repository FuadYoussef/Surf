package fuad.yousseftech.surf.Firebase;

import java.util.List;
import java.util.Map;

public class Rank {

    private Map<String, String> info;

    public Rank(Object data) {
        info = (Map<String, String>) data;
    }

    public int getCount() {
        String data = info.get("count");
        return Integer.parseInt(data);
    }

    public int getDuration() {
        String data = info.get("duration");
        return Integer.parseInt(data);
    }

    public String getWeb() {
        return info.get("web");
    }

}
