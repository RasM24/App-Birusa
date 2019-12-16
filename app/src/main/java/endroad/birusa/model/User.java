package endroad.birusa.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {

    String uid;
    String nick;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String nick) {
        this.uid = uid;
        this.nick = nick;
    }


    public String getNick() {
        return nick;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nick", nick);
        return result;
    }

}
