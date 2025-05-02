package com.example.nsso;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MapViewPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view_page);
        Utils.blackIconStatusBar(MapViewPage.this, R.color.white);

    }

}