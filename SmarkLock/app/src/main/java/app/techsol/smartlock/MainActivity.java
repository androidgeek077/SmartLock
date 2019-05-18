 package app.techsol.smartlock;
 import android.app.Activity;
 import android.content.Intent;
 import android.support.v7.app.AppCompatActivity;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.Button;


 public class MainActivity extends AppCompatActivity{
     private Button Snb;
     private Button Lnb;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         Snb =(Button) findViewById(R.id.Snb);
         Snb.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openActivityS();
             }
         });

         Lnb =(Button) findViewById(R.id.Lnb);
         Lnb.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openActivityL();
             }
         });

     }
     public void openActivityS(){
         Intent intent=new Intent(this,SignupActivity.class);
         startActivity(intent);
     }

     public void openActivityL(){
         Intent intent=new Intent(this,LoginActivity.class);
         startActivity(intent);
     }

 }
