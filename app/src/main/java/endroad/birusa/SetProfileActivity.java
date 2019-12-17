package endroad.birusa;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.widget.Toolbar;
import endroad.birusa.model.User;

public class SetProfileActivity extends BaseActivity implements View.OnClickListener {

    EditText mNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);
        refreshViews();
        mNick = (EditText) findViewById(R.id.edit_nick);
        showProgressDialog();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.bt_submit_nick).setOnClickListener(this);
        findViewById(R.id.bt_logout).setOnClickListener(this);
    }


    private void refreshViews() {
        FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        mNick.setText(user.getNick());
                        hideProgressDialog();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        if (view.getId() == R.id.bt_submit_nick) {
            if (!TextUtils.isEmpty(mNick.getText().toString())) {
                showProgressDialog();
                FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("nick").setValue(mNick.getText().toString());
                refreshViews();
            }
        }


    }

}
