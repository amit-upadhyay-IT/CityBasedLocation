package com.amitupadhyay.citybasedlocation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationFinder.OnLocationNotifiedListener {

    Button getCurrentLocation;
    TextView locationTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCurrentLocation = (Button) findViewById(R.id.getCurrentCity);
        locationTextview = (TextView) findViewById(R.id.current_city);

        getCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationFinder locationFinder = new LocationFinder(MainActivity.this);
                locationFinder.getCityByLocation();

            }
        });


    }

    @Override
    public void setOnLocationFoundListener(String cityName, String stateName, String countryName) {
        locationTextview.setText("City : "+cityName+", "+"\nState : "+stateName+", "+"\nCountry : "+countryName);
    }

    @Override
    public void setOnLocationNotFoundListener() {
        locationTextview.setText("Could not found your location, perhaps your connection is not good :(");
    }
}
