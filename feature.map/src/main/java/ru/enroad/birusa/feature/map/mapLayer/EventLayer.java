package ru.enroad.birusa.feature.map.mapLayer;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ru.enroad.birusa.feature.map.R;
import ru.enroad.birusa.feature.map.model.Event;


public class EventLayer extends BaseLayer implements ValueEventListener {

    private List<Event> events = new ArrayList<>();
    private List<Marker> markers = new ArrayList<>();

    public final static  String DB_EVENT = "events";

    eventChangelistener listener;

    public EventLayer(Context context) {
        super.context = context;
        listener = null;
        loadData();
    }


    private void loadData() {
        Query queryEventTwit = FirebaseDatabase.getInstance().getReference().child(DB_EVENT).orderByChild("date").startAt(getTime() - Event.TIME_ACTUAL);
        queryEventTwit.addValueEventListener(this);
    }

    public void draw(GoogleMap map, String uid) {
        if (events == null || map == null)
            return;
        for(Marker m:markers)
            m.remove();
        
        markers.clear();
        for (Event e : events) {
            MarkerOptions mo = new MarkerOptions().position(e.getPosGMS()).title(e.getText()).icon(BitmapDescriptorFactory.fromResource(getIcon(e.getIcon())));

            if(TextUtils.equals(e.getUid(),uid))
            mo.snippet("Нажмите чтобы удалить");
            Marker m = map.addMarker(mo);
            m.setTag(e);
            markers.add(m);
        }


//        for (Event e : events) {
//            if (!markersContain(e)) {
//                MarkerOptions mo = new MarkerOptions().position(e.getPosGMS()).title(e.getText()).icon(BitmapDescriptorFactory.fromResource(getIcon(e.getIcon())));
//                Marker m = map.addMarker(mo);
//                m.setTag(e);
//                markers.add(m);
//            }
//        }
    }

    public interface eventChangelistener {
        public void onEventChange();
    }

    public void setListener(eventChangelistener listener) {
        this.listener = listener;
    }

    private boolean markersContain(Event e) {
         for (Marker m : markers) {
             if (m.getTag().equals(e))
                 return true;
         }
         return false;
     }

    public static long getTime() {
        return System.currentTimeMillis() / 1000L;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        events.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Event e = snapshot.getValue(Event.class);
            events.add(e);
            e.setPostKey(snapshot.getKey());
        }
        listener.onEventChange();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private int getIcon(String id) {

        String[] array = context.getResources().getStringArray(R.array.type_points);

        for (int i = 0; i < array.length; i++)
            if (TextUtils.equals(id, array[i]))
                return Event.drawable[i];

        return R.drawable.event_flag;
    }

    public boolean deleteEvent(Event event) {
        for (Event e : events)
            if (event.getDate() == e.getDate()) {
                events.remove(e);
                markers.remove(e);

                return true;
            }
        return false;
    }
}