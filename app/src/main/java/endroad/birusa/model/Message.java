package endroad.birusa.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



@IgnoreExtraProperties
public class Message {

    private String text;
    private String uid;
    private long date;

    public Message() {
    }

    public Message(String text, String uid, long date) {
        this.text = text;
        this.uid = uid;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public String getUid() {
        return uid;
    }

    public long getDate() {
        return date;
    }



    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("text", text);
        result.put("uid", uid);
        result.put("date", date);
        return result;
    }

    public String  getDateText() {
        int a = (int)date/86400;
        int b = (int)(System.currentTimeMillis() / 1000L)/86400;
        String vv ="";
        long dv = Long.valueOf(date)*1000;// its need to be in milisecond
        Date df = new java.util.Date(dv);
        String[] mouth = {"-янв", "-фев", "-март", "-апр", "-май", "-июнь", "-июль", "-авг", "-сен", "-окт", "-ноя", "-дек"};
        if(a!=b)
            vv = new SimpleDateFormat("dd"+ mouth[df.getMonth()] +"\n").format(df);
        vv += new SimpleDateFormat("HH:mm").format(df);
        return vv;
    }
}
