package app.techsol.smartlock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import app.techsol.smartlock.Models.UserModel;
import app.techsol.smartlock.Models.ProductModel;

public class SacnBarCodeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference reference, productRef;
    private UserModel userModel;
    String userid;
    private ArrayList<String> mProductKeys;
    String productkey;
    ImageView mQRImage;
    RotateLoading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sacn_bar_code);
        loading=findViewById(R.id.rotateloading);

        loading.setVisibility(View.VISIBLE);
        loading.start();
        auth = FirebaseAuth.getInstance();
        userid = auth.getCurrentUser().getUid();
//        Toast.makeText(this, userid, Toast.LENGTH_SHORT).show();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        productRef = FirebaseDatabase.getInstance().getReference("Products");

        mQRImage = findViewById(R.id.QRImage);

        getProducyKeyFromUser();
        getProducyKeyFromProduct();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.optLogout) {
            auth.signOut();
            startActivity(new Intent(SacnBarCodeActivity.this, LoginActivity.class));
            return true;

        }


        return super.onOptionsItemSelected(item);
    }

    private void getProducyKeyFromUser() {
        reference.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Toast.makeText(SacnBarCodeActivity.this, ""+dataSnapshot, Toast.LENGTH_SHORT).show();
                Iterable<DataSnapshot> studentNameList = dataSnapshot.getChildren();
                productkey = dataSnapshot.child("productkey").getValue().toString();
//                Toast.makeText(SacnBarCodeActivity.this, productkey, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SacnBarCodeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getProducyKeyFromProduct() {
        mProductKeys = new ArrayList<>();
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Toast.makeText(SacnBarCodeActivity.this, ""+dataSnapshot, Toast.LENGTH_SHORT).show();
                Iterable<DataSnapshot> productDetails = dataSnapshot.getChildren();
                for (DataSnapshot products : productDetails) {
                    ProductModel model = products.getValue(ProductModel.class);
                    if (model.getProductkey().equalsIgnoreCase(productkey)) {
                        Glide.with(SacnBarCodeActivity.this).load(model.getImgurl()).into(mQRImage);
                        loading.setVisibility(View.GONE);
                    }
//                    mProductKeys.add(model.getProductkey());
//                    Toast.makeText(SacnBarCodeActivity.this, "" + mProductKeys, Toast.LENGTH_SHORT).show();

                }
//                String productkey = dataSnapshot.child("productkey").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public void onBackPressed() {

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

}
