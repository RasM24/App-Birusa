package endroad.birusa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;

import androidx.fragment.app.Fragment;
import endroad.birusa.model.Event;


public class FragmentMapBase extends Fragment {

    AlertDialog.Builder mDialogBuilder;
    AlertDialog.Builder mDialogBuilderRemoveEvent;
    AlertDialog alertDialog;
    AlertDialog alertDialogRemoveEvent;
    public final static  String DB_EVENT = "events";
    String styleMain;
    String styleMarker;
    EditText mInput;
    Spinner mType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        styleMain = getStringFromAssetFile("GoogleMapStyle");
        styleMarker = getStringFromAssetFile("GoogleMapStyleMarker");


        LayoutInflater li = LayoutInflater.from(getContext());
        View view = li.inflate(R.layout.input_text_alert, null);

        mInput = (EditText) view.findViewById(R.id.input_text);
        mType = (Spinner) view.findViewById(R.id.type_point);

        // Подключаем свой шаблон с разными значками



        mAdapter adapter = new mAdapter(getContext(),
                R.layout.spinner_with_icon, getResources().getStringArray(R.array.type_points));
        // Вызываем адапетр
        mType.setAdapter(adapter);

        mDialogBuilder = new AlertDialog.Builder(getContext());
        mDialogBuilder.setView(view);

        mDialogBuilderRemoveEvent = new AlertDialog.Builder(getContext());
        return null;
    }

    private String[] filter(String[] eventsTypeArray) {

        if (eventsTypeArray == null)
            return null;
        String[] r = new String[eventsTypeArray.length - 3];
        System.arraycopy(eventsTypeArray, 0, r, 0, eventsTypeArray.length - 3);
        return r;
    }

    protected String getStringFromAssetFile(String name) {
        byte[] buffer = null;
        InputStream is;

        try {
            is = getContext().getAssets().open(name);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(buffer);
    }

    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

    }

    public static long getTime() {
        return System.currentTimeMillis() / 1000L;
    }


    protected class mAdapter extends ArrayAdapter<String> {

        String[] typeArray = null;

        public mAdapter(Context context, int textViewResourceId,
                        String[] objects) {
            super(context, textViewResourceId, objects);
            typeArray = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {

            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.spinner_with_icon, parent, false);
            TextView label = (TextView) view.findViewById(R.id.text_name);
            label.setText(typeArray[position]);

            ImageView icon = (ImageView) view.findViewById(R.id.icon);

            icon.setImageResource(Event.drawable[position]);

            return view;
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
