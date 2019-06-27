package app.techsol.smartlock;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

import app.techsol.smartlock.Models.ProductModel;
import app.techsol.smartlock.Models.UserModel;

public class SignupActivity extends AppCompatActivity {

    EditText mUsernameET, mEmailET, mPasswordET, mCPasswordET, mProductKeyET, mPhoneET, mAddressET;
    String mUserNameStr, mEmailStr, mPasswordStr, mCPasswordStr, mProductKeyStr, mPhoneStr, mAddressStr;
    Button mRegsiterBtn;
    String productkey;
    DatabaseReference productRef;
    Boolean isKeyExist;


    FirebaseAuth auth;
    RotateLoading loading;
    UserModel model;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        productRef = FirebaseDatabase.getInstance().getReference("Categories");

        loading = findViewById(R.id.rotateloading);

        RegisterEditTxt();
        mRegsiterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EditTxttoString();
                getProducyKeyFromProduct();
                if (mUserNameStr.isEmpty()) {
                    mUsernameET.setError("Enter your name");
                } else if (mEmailStr.isEmpty()) {
                    mEmailET.setError("Enter your Email");
                } else if (mPasswordStr.isEmpty()) {
                    mPasswordET.setError("Enter your password");
                } else if (mCPasswordStr.isEmpty()) {
                    mCPasswordET.setError("Enter your password");
                } else if (mCPasswordStr == mPasswordStr) {
                    Toast.makeText(SignupActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                } else if (mProductKeyStr.isEmpty()) {
                    mProductKeyET.setError("Enter your product key");
                } else if (mPhoneStr.isEmpty()) {
                    mPhoneET.setError("Enter your phoneno");
                } else if (mAddressStr.isEmpty()) {
                    mAddressET.setError("Enter your Address");
                } else {
                    loading.setVisibility(View.VISIBLE);
                    loading.start();
                    if (isKeyExist==false) {
                        Toast.makeText(SignupActivity.this, "key doesnt match", Toast.LENGTH_SHORT).show();

                        auth.createUserWithEmailAndPassword(mEmailStr, mPasswordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    model = new UserModel(mUserNameStr, mEmailStr, mProductKeyStr, mPhoneStr, mAddressStr);
                                    databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            loading.setVisibility(View.GONE);
                                            loading.stop();
                                            Snackbar snackbar = Snackbar
                                                    .make(v, "User registered successfully", Snackbar.LENGTH_INDEFINITE)
                                                    .setAction("OK", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                            finish();
                                                        }
                                                    });
                                            View v = snackbar.getView();
                                            v.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                            snackbar.show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                loading.stop();
                                Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else {
                        Toast.makeText(SignupActivity.this, "key already exist", Toast.LENGTH_SHORT).show();
                    }

                }
            }


        });

    }

    void RegisterEditTxt() {
        mUsernameET = findViewById(R.id.UserNameET);
        mEmailET = findViewById(R.id.EmailET);
        mPasswordET = findViewById(R.id.PasswordET);
        mCPasswordET = findViewById(R.id.CPasswordET);
        mProductKeyET = findViewById(R.id.ProductKeyET);
        mPhoneET = findViewById(R.id.PhoneET);
        mAddressET = findViewById(R.id.AddressET);
        mRegsiterBtn = findViewById(R.id.RegsiterBtn);

    }

    void EditTxttoString() {
        mUserNameStr = mUsernameET.getText().toString();
        mEmailStr = mEmailET.getText().toString();
        mPasswordStr = mPasswordET.getText().toString();
        mCPasswordStr = mCPasswordET.getText().toString();
        mProductKeyStr = mProductKeyET.getText().toString();
        mPhoneStr = mPhoneET.getText().toString();
        mAddressStr = mAddressET.getText().toString();
    }

    void formValidation() {
        if (mUserNameStr.isEmpty()) {
            mUsernameET.setError("Enter your name");
        } else if (mEmailStr.isEmpty()) {
            mEmailET.setError("Enter your Email");
        } else if (mPasswordStr.isEmpty()) {
            mPasswordET.setError("Enter your password");
        } else if (mCPasswordStr.isEmpty()) {
            mCPasswordET.setError("Enter your password");
        }
        else if (mCPasswordStr==mPasswordStr) {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
        } else if (mProductKeyStr.isEmpty()){
            mProductKeyET.setError("Enter your product key");
        } else if (mPhoneStr.isEmpty()) {
            mPhoneET.setError("Enter your phoneno");
        } else if (mAddressStr.isEmpty()) {
            mPhoneET.setError("Enter your Address");
        }
    }


    private void getProducyKeyFromProduct() {
        isKeyExist = false;

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(SignupActivity.this, "" + dataSnapshot, Toast.LENGTH_SHORT).show();
                Iterable<DataSnapshot> productDetails = dataSnapshot.getChildren();
                for (DataSnapshot products : productDetails) {
                    ProductModel model = products.getValue(ProductModel.class);
                    if (model.getProductkey().equalsIgnoreCase(mProductKeyStr)) {
                        isKeyExist = true;
                    }

//                    mProductKeys.add(model.getProductkey());
                    Toast.makeText(SignupActivity.this, "" + isKeyExist, Toast.LENGTH_SHORT).show();

                }
//                String productkey = dataSnapshot.child("productkey").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
            }
        });

    }

}
