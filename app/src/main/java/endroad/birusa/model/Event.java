package endroad.birusa.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import endroad.birusa.R;

@IgnoreExtraProperties
public class Event {

    private String icon;
    private String text;
    private LatLng pos;
    private long date;
    private String uid;

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    private String postKey;

    public static int[] drawable = new int[]{
            R.drawable.event_meet,
            R.drawable.event_book,
            R.drawable.event_guitar,
            R.drawable.event_gamepad,
            R.drawable.event_palatka,
            R.drawable.event_flag,
            R.drawable.event_it,
            R.drawable.event_guitar_it,
            R.drawable.event_gamepad_it
    };

    public final static long TIME_ACTUAL = 3600; //30min

    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Event(String icon, String text, LatLng pos, long date, String uid) {
        this.icon = icon;
        this.text = text;
        this.pos = pos;
        this.date = date;
        this.uid = uid;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("text", text);
        result.put("pos", pos);
        result.put("date", date);
        result.put("icon",icon);
        result.put("uid",uid);
        return result;
    }

    public LatLng getPosGMS()
    {
        return new LatLng(pos.latitude,pos.longitude);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LatLng getPos() {
        return pos;
    }

    public void setPos(LatLng pos) {
        this.pos = pos;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}