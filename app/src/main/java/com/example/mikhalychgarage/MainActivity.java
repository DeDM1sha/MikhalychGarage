package com.example.mikhalychgarage;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mikhalychgarage.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FirebaseUser user = auth.getCurrentUser();

        if(user != null){
            finish();
            startActivity(new Intent(this, dashboardActivity.class));
        }

    }


    public void login (View view)
    {

        startActivity(new Intent(this,LoginActivity.class));
//        String TextClassname = classname.getText().toString();
//        // starting our intent
//        Intent classintent = new Intent(this,SecondActivity.class);
//        classintent.putExtra("Classname",TextClassname);
//        startActivityForResult(classintent,request_code);
    }
    public void register (View view)
    {
        startActivity(new Intent(this,RegisterActivity.class));
    }
}

