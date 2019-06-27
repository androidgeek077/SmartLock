package app.techsol.smartlock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import app.techsol.smartlock.Fragments.AddProductFragment;
import app.techsol.smartlock.Fragments.ViewProductsFragment;
import app.techsol.smartlock.Fragments.ViewUsersFragment;

public class AdminBottomNavActivity extends AppCompatActivity {
    private TextView mTextMessage;
    FirebaseAuth auth;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.AddProduct:
                    FragmentLoadinManagerWithBackStack(new AddProductFragment());
                return true;
                case R.id.ViewProduct:
                    FragmentLoadinManagerWithBackStack(new ViewProductsFragment());
                    return true;
                case R.id.ViewUsers:
                    FragmentLoadinManagerWithBackStack(new ViewUsersFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bottom_nav);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        auth=FirebaseAuth.getInstance();

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    public void FragmentLoadinManagerWithBackStack(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAndRemoveTask();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.optLogout){
            auth.signOut();
            startActivity(new Intent(AdminBottomNavActivity.this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
