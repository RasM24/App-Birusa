package endroad.birusa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends BaseActivity {

    public static DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    private FragmentTransaction ft;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_map:
                    // FragmentMap currentFragmentMap = new FragmentMap();
                    FragmentMap currentFragmentMap = new FragmentMap();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content, currentFragmentMap);
                    ft.commit();
                    return true;
                case R.id.navigation_chat:
                    FragmentChat currentFragmentChat = new FragmentChat();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content, currentFragmentChat);
                    ft.commit();
                    return true;
                case R.id.navigation_library:
                    FragmentLibrary currentFragmentB = new FragmentLibrary();
                    ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content, currentFragmentB);
                    ft.commit();
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setSelectedItemId(R.id.navigation_chat);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(MainActivity.this, SetProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
