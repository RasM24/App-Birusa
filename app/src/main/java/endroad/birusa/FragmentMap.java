package endroad.birusa;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

import endroad.birusa.mapLayer.BuildLayer;
import endroad.birusa.mapLayer.EventLayer;
import endroad.birusa.mapLayer.RoadLayer;
import endroad.birusa.model.Build;
import endroad.birusa.model.Event;
import ru.endroad.birusa.R;

public class FragmentMap extends FragmentMapBase implements OnMapReadyCallback, GoogleMap.OnPolygonClickListener, View.OnClickListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, EventLayer.eventChangelistener, GoogleMap.OnInfoWindowClickListener {


    MapView mMapView;
    BuildLayer buildLayer;
    RoadLayer roadLayer;
    EventLayer eventLayer;

    FloatingActionButton fabAddEvent;

    Polygon targetPolygone;
    Marker polygoneMarker;

    Marker targetMarker;

    float zoom = 17;
    private static final int PERMISSION_REQUEST_CODE = 1000;

    private GoogleMap mMap;

    public FragmentMap() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        buildLayer = new BuildLayer(getContext());
        roadLayer = new RoadLayer(getContext());
        eventLayer = new EventLayer(getContext());
        fabAddEvent = (FloatingActionButton) rootView.findViewById(R.id.fab_add_event);
        fabAddEvent.setOnClickListener(this);
        eventLayer.setListener(this);

        try {
            mMapView = (MapView) rootView.findViewById(R.id.mapView);
            mMapView.onCreate(savedInstanceState);
            mMapView.onResume(); // needed to get the mMap to display immediately
            MapsInitializer.initialize(getActivity().getApplicationContext());
            mMapView.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Принять",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                showProgressDialog();
                                String key = MainActivity.mRef.child(DB_EVENT).push().getKey();
                                String text = mInput.getText().toString();
                                if (TextUtils.isEmpty(text))
                                    text = "Событие..";

                                Event event = new Event(mType.getSelectedItem().toString(), text, targetMarker.getPosition(), getTime(), getUid());

                                Map<String, Object> values = event.toMap();
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put(DB_EVENT + "/" + key, values);

                                MainActivity.mRef.updateChildren(childUpdates);
                                mInput.setText("");
                                cancelAddEvent();

                                hideProgressDialog();

                            }
                        }
                )
                .setNegativeButton("Назад",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                cancelAddEvent();
                            }
                        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();


    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setOnPolygonClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        //mMap.setInfoWindowAdapter(new InfoWindowAdapter());
        //mMap.setMinZoomPreference(16);
        //mMap.setMaxZoomPreference(20);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            requestMultiplePermissions();

        else
            mMap.setMyLocationEnabled(true);
        //mMap.setOnCameraChangeListener(this);
        //Центрирование карты

//        IconGenerator iconFactory = new IconGenerator(getContext());
//        iconFactory.setTextAppearance(R.style.iconGenText);
//        iconFactory.setBackground(getResources().getDrawable(R.drawable.map_bubble));
//
//        MarkerOptions markerOptions = new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("Your text here") ))
//                .position(new LatLng(55.859605, 92.248106))
//                .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
//
//        mMap.addMarker(markerOptions);


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.859605, 92.248106), zoom));
        drawLayers();
    }

    void drawLayers() {
        buildLayer.draw(mMap);
        roadLayer.draw(mMap);
        eventLayer.draw(mMap, getUid());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (targetPolygone != null)
            targetPolygone.setFillColor(getContext().getResources().getColor(R.color.colorPrimary));
        marker.showInfoWindow();
        return false;
    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        Build b = (Build) polygon.getTag();
        clearTargetPolygone();
        polygon.setFillColor(getContext().getResources().getColor(R.color.map_target_fill));
        polygon.setStrokeColor(getContext().getResources().getColor(R.color.map_target_stroke));
        targetPolygone = polygon;

        polygoneMarker = mMap.addMarker(new MarkerOptions().position(Build.center(polygon))
                .alpha(0.0f).infoWindowAnchor(.6f, 1.0f));
        polygoneMarker.setTitle(b.name);
        polygoneMarker.showInfoWindow();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        clearTargetPolygone();
    }

    void clearTargetPolygone() {
        if (targetPolygone != null) {
            Build b = (Build) targetPolygone.getTag();
            targetPolygone.setFillColor(b.getColorFill(getContext()));
            targetPolygone.setStrokeColor(b.getColorStroke(getContext()));
            targetPolygone = null;

            polygoneMarker.remove();
        }
    }

    @Override
    public void onClick(View v) {
        clearTargetPolygone();

        if (v.getId() == R.id.fab_add_event) {
            if (targetMarker != null) {
                if (alertDialog == null)
                    alertDialog = mDialogBuilder.create();
                alertDialog.show();
                mMap.setMapStyle(new MapStyleOptions(styleMain));
                fabAddEvent.setImageResource(R.drawable.ic_plus);
            } else {
                targetMarker = mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target));
                mMap.setMapStyle(new MapStyleOptions(styleMarker));
                fabAddEvent.setImageResource(R.drawable.ic_check);
            }
        }
    }

    private void cancelAddEvent() {
        targetMarker.remove();
        targetMarker = null;
    }

    @Override
    public void onCameraMove() {
        if (targetMarker != null)
            targetMarker.setPosition(mMap.getCameraPosition().target);
    }

    @Override
    public void onEventChange() {
        eventLayer.draw(mMap, getUid());
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {

        final Event event = (Event) marker.getTag();
        if(getUid().compareTo(event.getUid())!=0)
            return;

        mDialogBuilderRemoveEvent.setMessage("Вы хотите удалить событие?")
                .setCancelable(false)
                .setPositiveButton("Да",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(eventLayer.deleteEvent(event))
                                    MainActivity.mRef.child(DB_EVENT).child(event.getPostKey()).removeValue();
                            }})
                .setNegativeButton("Нет",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        alertDialogRemoveEvent = mDialogBuilderRemoveEvent.create();
        alertDialogRemoveEvent.show();
    }

    public void requestMultiplePermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION
            , android.Manifest.permission.ACCESS_FINE_LOCATION
    }, PERMISSION_REQUEST_CODE);
    }
}