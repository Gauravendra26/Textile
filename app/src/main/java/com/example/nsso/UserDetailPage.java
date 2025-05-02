package com.example.nsso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UserDetailPage extends AppCompatActivity {

    TextView tvCloseDetails, tvmobileno, tvRCRName, tvRCRID, tvRCRstate, tvRCRcity, tvRCRpincode, tvRCRaddress;
    RelativeLayout rlfillform, rlCall;
    CardView cardMap;
    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;
    LinearLayout llspinnerRc2;
    RelativeLayout rlspinnerRc2;
    Spinner spinnerRc1, spinnerRc2;
    private Spinner spinner1, spinner2;
    private RelativeLayout spinner2Layout;

    FusedLocationProviderClient fusedLocationProviderClient1;
    String country, city , longitude, latitude, sp_sirname, sp_applicant, sp_registration_no, sp_mobile, sp_state,
            sp_address, sp_pin, sp_village, sp_taluk, selectedValue23, selectedValue24, combinedString, check;
    String sr_sirname, sr_applicant, sr_registration_no, sr_mobile, sr_state, sr_address, sr_pin, sr_village,
            sr_district, sr_taluk;

    int verification_id,sp_status,sr_status;
    TextView tvday;

    private final static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_page);
        Utils.blackIconStatusBar(UserDetailPage.this, R.color.white);

        init();
        Intent intent = getIntent();
        sr_sirname = intent.getStringExtra("sr_sirname");
        sr_applicant = intent.getStringExtra("sr_applicant");
        sr_registration_no = intent.getStringExtra("sr_registration_no");
        sr_mobile = intent.getStringExtra("sr_mobile");
        sr_state = intent.getStringExtra("name");
        sr_address = intent.getStringExtra("sr_address");
        sr_pin = intent.getStringExtra("sr_pin");
        sr_village = intent.getStringExtra("sr_village");
        sr_district = intent.getStringExtra("sr_district");
        sr_taluk = intent.getStringExtra("sr_taluk");
        sr_status = intent.getIntExtra("sr_status",0);
        check = intent.getStringExtra("check");
        combinedString = intent.getStringExtra("combinedString");
        sp_sirname = intent.getStringExtra("sp_sirname");
        sp_applicant = intent.getStringExtra("sp_applicant");
        sp_registration_no = intent.getStringExtra("sp_registration_no");
        sp_mobile = intent.getStringExtra("sp_mobile");
        sp_state = intent.getStringExtra("name");
        sp_address = intent.getStringExtra("sp_address");
        sp_pin = intent.getStringExtra("sp_pin");
        sp_village = intent.getStringExtra("sp_village");
        sp_taluk = intent.getStringExtra("sp_taluk");
        sp_status = intent.getIntExtra("sp_status",0);
        verification_id = intent.getIntExtra("verification_id", 0);
//        Toast.makeText(this, ""+verification_id, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "sp_status :"+sp_status+"sr_status : "+sr_status, Toast.LENGTH_SHORT).show();
        Log.e("checkplease1", sp_sirname + " " + sp_applicant + " " + sp_registration_no + " " + sp_mobile + " " + sp_state
                + " " + sp_address + " " + sp_pin + " " + sp_village);
        if (check.equals("12a")) {
            tvRCRName.setText(sp_sirname + " " + sp_applicant);
            tvRCRID.setText(sp_registration_no);
            tvmobileno.setText(sp_mobile);
            tvRCRstate.setText(sp_state);
            tvRCRaddress.setText(sp_taluk + " " + sp_address);
            tvRCRpincode.setText(sp_pin);
            tvRCRcity.setText(sp_village);
            tvday.setText(combinedString);
        } else {
            tvRCRName.setText(sr_sirname + " " + sr_applicant);
            tvRCRID.setText(sr_registration_no);
            tvmobileno.setText(sr_mobile);
            tvRCRstate.setText(sr_state);
            tvRCRaddress.setText(sr_taluk + " " + sr_address);
            tvRCRpincode.setText(sr_pin);
            tvRCRcity.setText(sr_village);
            tvday.setText(combinedString);

        }


        rlCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                if (check.equals("12a")) {
                    i.setData(Uri.parse("tel:" + sp_mobile));
                }else{
                    i.setData(Uri.parse("tel:" + sr_mobile));
                }
                startActivity(i);
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                rlCall.startAnimation(myAnim);

            }
        });


        rlfillform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                rlfillform.startAnimation(myAnim);

                if (check.equals("12b")) {
                    Intent intent = new Intent(getApplicationContext(), Account_details_ChawkiRearer_Activity.class);
                    intent.putExtra("check", check);
                    intent.putExtra("combinedString", combinedString);

                    intent.putExtra("sr_sirname", sr_sirname);
                    intent.putExtra("sr_applicant", sr_applicant);
                    intent.putExtra("sr_registration_no", sr_registration_no);
                    intent.putExtra("sr_mobile", sr_mobile);
                    intent.putExtra("sr_state", sr_state);
                    intent.putExtra("sr_address", sr_address);
                    intent.putExtra("sr_pin", sr_pin);
                    intent.putExtra("sr_village", sr_village);
                    intent.putExtra("sr_district", sr_district);
                    intent.putExtra("sr_taluk", sr_taluk);
                    intent.putExtra("sr_status", sr_status);
                    intent.putExtra("verification_id", verification_id);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(getApplicationContext(), AccontDetailPage.class);
                    intent.putExtra("check", check);
                    intent.putExtra("combinedString", combinedString);

                    intent.putExtra("sp_sirname", sp_sirname);
                    intent.putExtra("sp_applicant", sp_applicant);
                    intent.putExtra("sp_registration_no", sp_registration_no);
                    intent.putExtra("sp_mobile", sp_mobile);
                    intent.putExtra("sp_state", sp_state);
                    intent.putExtra("sp_address", sp_address);
                    intent.putExtra("sp_pin", sp_pin);
                    intent.putExtra("sp_village", sp_village);
                    intent.putExtra("sp_taluk", sp_taluk);
                    intent.putExtra("sp_status", sp_status);
                    intent.putExtra("verification_id", verification_id);
                    startActivity(intent);

                }

            }
        });
        tvCloseDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), SelectRCRList.class);
//                startActivity(i);

                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                tvCloseDetails.startAnimation(myAnim);
                finish();

            }
        });

//        currentLocation();
//        cardMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i =new Intent(getApplicationContext(), MapsActivity.class);
//                i.putExtra("latitude",latitude);
//                i.putExtra("longitude",longitude);
//                startActivity(i);
//
//            }
//        });

        cardMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Address you want to open in Google Maps
                if (check.equals("12a")) {

                    String address = sp_address + " " + sp_taluk +  " " + sp_state + " " + sp_pin;
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps"); // Specify Google Maps package

                    // Verify that Google Maps is installed before starting the intent
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    } else {
                        // If Google Maps is not installed, you can take an alternative action
                        // For example, open a web browser with Google Maps website
                        Uri webUri = Uri.parse("https://www.google.com/maps?q=" + Uri.encode(address));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
                        startActivity(webIntent);
                    }
                }else {
                    String address = sr_address + " " + sr_taluk + " " + sr_state + " " + sr_pin;
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps"); // Specify Google Maps package

                    // Verify that Google Maps is installed before starting the intent
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    } else {
                        // If Google Maps is not installed, you can take an alternative action
                        // For example, open a web browser with Google Maps website
                        Uri webUri = Uri.parse("https://www.google.com/maps?q=" + Uri.encode(address));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
                        startActivity(webIntent);
                    }
                }

                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                cardMap.startAnimation(myAnim);

            }
        });


    }

    void init() {

        tvday = findViewById(R.id.tvday);
        rlfillform = findViewById(R.id.rlfillform);
        rlCall = findViewById(R.id.rlCall);
        tvCloseDetails = findViewById(R.id.tvCloseDetails);
        tvCloseDetails = findViewById(R.id.tvCloseDetails);
        tvmobileno = findViewById(R.id.tvmobileno);
        tvRCRName = findViewById(R.id.tvRCRName);
        tvRCRID = findViewById(R.id.tvRCRID);
        tvRCRaddress = findViewById(R.id.tvRCRaddress);
        tvRCRpincode = findViewById(R.id.tvRCRpincode);
        tvRCRcity = findViewById(R.id.tvRCRcity);
        tvRCRstate = findViewById(R.id.tvRCRstate);
        cardMap = findViewById(R.id.cardMap);


    }

    public void currentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Check gps is enable or not

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Write Function To enable gps

            OnGPS();
        } else {
            //GPS is already On then

            getLocation();
        }

    }

    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(UserDetailPage.this, Manifest.
                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(UserDetailPage.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps != null) {
                double lat = LocationGps.getLatitude();
                double longi = LocationGps.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
            } else if (LocationNetwork != null) {
                double lat = LocationNetwork.getLatitude();
                double longi = LocationNetwork.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

            } else if (LocationPassive != null) {
                double lat = LocationPassive.getLatitude();
                double longi = LocationPassive.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

            } else {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

            //Thats All Run Your App
        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}