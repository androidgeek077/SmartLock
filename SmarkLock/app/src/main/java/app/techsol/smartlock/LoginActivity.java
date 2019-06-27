package app.techsol.smartlock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.victor.loading.rotate.RotateLoading;

public class LoginActivity extends AppCompatActivity {


    TextView mRegisterTV;
    FirebaseAuth mAuth;
    EditText mEmailET, mPasswordET;
    Button mLoginBtn;
    String emailStr, passwordStr;
    FirebaseAuth auth;

    RotateLoading loading;

    @Override
    protected void onStart() {
        super.onStart();
        auth=FirebaseAuth.getInstance();

        if (null != auth.getCurrentUser()) {
            startActivity(new Intent(LoginActivity.this, AdminBottomNavActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        mEmailET=findViewById(R.id.emailEd);
        mPasswordET=findViewById(R.id.passwordEd);
        loading=findViewById(R.id.rotateloading);

        mRegisterTV=findViewById(R.id.registration);
        mRegisterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, AdminSignupActivity.class));
            }
        });
        mLoginBtn=findViewById(R.id.loginBtn);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                emailStr=mEmailET.getText().toString();
                passwordStr=mPasswordET.getText().toString();

                if (emailStr.isEmpty()) {
                    mEmailET.setError("Enter your email");
                } else if (passwordStr.isEmpty()) {
                    mPasswordET.setError("Enter your password");
                }
                    else {
                        loading.setVisibility(View.VISIBLE);
                        loading.start();
                    mAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loading.setVisibility(View.GONE);
                                startActivity(new Intent(LoginActivity.this, AdminBottomNavActivity.class));
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loading.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
