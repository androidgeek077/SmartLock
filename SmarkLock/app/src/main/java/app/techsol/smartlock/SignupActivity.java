package app.techsol.smartlock;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.victor.loading.rotate.RotateLoading;

public class SignupActivity extends AppCompatActivity {

    EditText mUsernameET, mEmailET, mPasswordET, mCPasswordET, mProductKeyET, mPhoneET, mAddressET;
    String mUserNameStr, mEmailStr, mPasswordStr, mCPasswordStr, mProductKeyStr, mPhoneStr, mAddressStr;
    Button mRegsiterBtn;

    FirebaseAuth auth;
    RotateLoading loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth=FirebaseAuth.getInstance();
        loading=findViewById(R.id.rotateloading);

        RegisterEditTxt();
        mRegsiterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTxttoString();
                if (mUserNameStr.isEmpty()) {
                    mUsernameET.setError("Enter your name");
                } else if (mEmailStr.isEmpty()) {
                    mEmailET.setError("Enter your Email");
                } else if (mPasswordStr.isEmpty()) {
                    mPasswordET.setError("Enter your password");
                } else if (mCPasswordStr.isEmpty()) {
                    mCPasswordET.setError("Enter your password");
                } else if (mCPasswordStr.equals(mPasswordStr)) {
                    Toast.makeText(SignupActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                } else if (mProductKeyStr.isEmpty()){
                    mProductKeyET.setError("Enter your product key");
                } else if (mPhoneStr.isEmpty()){
                    mPhoneET.setError("Enter your phoneno");
                }  else if (mAddressStr.isEmpty()){
                    mAddressET.setError("Enter your Address");
                } else {
                    loading.setVisibility(View.VISIBLE);
                    loading.start();

                    auth.createUserWithEmailAndPassword(mEmailStr,mPasswordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            loading.stop();
                            loading.setVisibility(View.GONE);
                            Toast.makeText(SignupActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loading.stop();
                            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

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
        } else if (mCPasswordStr.equals(mPasswordStr)) {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
        } else if (mProductKeyStr.isEmpty()){
            mProductKeyET.setError("Enter your product key");
        } else if (mPhoneStr.isEmpty()){
            mPhoneET.setError("Enter your phoneno");
        }  else if (mAddressStr.isEmpty()){
            mPhoneET.setError("Enter your Address");
        }
    }
}
