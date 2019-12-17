package endroad.birusa.model;

import com.google.firebase.database.Exclude;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserVK {


    public String uid;
    private String name;
    private String url;
    private String number;


    private String email;
    private String city;
    private String dateB;
    private int gender;

    public UserVK() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("url", url);
        result.put("number", number);
        result.put("email", email);
        result.put("city", city);
        result.put("dateB", dateB);
        result.put("gender", gender);
        return result;
    }

    public void jsonLoad(JSONObject json) throws JSONException {
        name = json.optString("first_name") + " " + json.optString("last_name");
        url = json.optString("id");
        number = json.optString("mobile_phone");
        city = json.getJSONObject("city").optString("title");
        dateB = json.optString("bdate");
        gender = json.optInt("sex");
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getDateB() {
        return dateB;
    }

    public int getGender() {
        return gender;
    }
}

