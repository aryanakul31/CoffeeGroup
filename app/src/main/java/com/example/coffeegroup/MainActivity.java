package com.example.coffeegroup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.coffeegroup.Services.ConstData;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences =getSharedPreferences(ConstData.sharedPrefName,MODE_PRIVATE);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(sharedPreferences.getBoolean(ConstData.loginStatus,false))
                    startActivity(new Intent(MainActivity.this, CoffeeList.class));
                else
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        },2000);
    }
}