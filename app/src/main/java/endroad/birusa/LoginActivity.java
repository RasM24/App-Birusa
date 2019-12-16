package endroad.birusa;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.util.VKUtil;

import org.json.JSONException;
import org.json.JSONObject;

import endroad.birusa.model.User;
import endroad.birusa.model.UserVK;

public class LoginActivity extends BaseActivity implements View.OnClickListener, OnCompleteListener<AuthResult> {


    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
            VKScope.EMAIL
    };
    private String mName = "anonim";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Check authVK on Activity start
        if (mAuth.getCurrentUser() != null) {
            showProgressDialog();
            onAuthSuccess(mAuth.getCurrentUser());
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_auth_vk) {
            showProgressDialog();
            VKSdk.login(this, sMyScope);
        }
        if (view.getId() == R.id.bt_auth_google) {
        }
        if (view.getId() == R.id.bt_auth_anon) {
            showProgressDialog();
            mAuth.signInAnonymously().addOnCompleteListener(this);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                final UserVK user = new UserVK();
                user.setEmail(res.email);
                final VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,"id,first_name,last_name,sex,bdate,city,contacts"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        try {
                            user.jsonLoad(response.json.getJSONArray("response").optJSONObject(0));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        authVK(user);
                    }
                    @Override
                    public void onError(VKError error) {}
                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {}
                });

//                request.secure = false;
//                request.useSystemLanguage = false;

//

                //startApiCall(request);
            }

            @Override
            public void onError(VKError error) {
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    private void authVK(UserVK user) {
        mName = user.getName();
        mDatabase.child("usersVK").child(user.getUrl()).setValue(user);
        mAuth.signInAnonymously().addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            writeUser(task.getResult().getUser().getUid(), mName);
            onAuthSuccess(task.getResult().getUser());
        }
    }


    private void writeUser(String userId, String name) {
        User user = new User(userId, name);
        mDatabase.child("users").child(userId).setValue(user);
    }

    private void onAuthSuccess(FirebaseUser user) {

        Query queryEvent = mDatabase.child("users").child(user.getUid());
        queryEvent.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressDialog();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}