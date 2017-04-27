package com.amitupadhyay.citybasedlocation;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationFinder.OnLocationNotifiedListener, View.OnClickListener {

    Button getCurrentLocation;
    TextView locationTextview;

    Button getLocationTwoBtn;
    TextView setLocationTwoTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCurrentLocation = (Button) findViewById(R.id.getCurrentCity);
        locationTextview = (TextView) findViewById(R.id.current_city);

        setLocationTwoTV = (TextView) findViewById(R.id.showLocationTwo);
        getLocationTwoBtn = (Button) findViewById(R.id.getLocationTwo);

        getCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationFinder locationFinder = new LocationFinder(MainActivity.this);
                locationFinder.getCityByLocation();

            }
        });

        getLocationTwoBtn.setOnClickListener(this);

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                //Got the location!
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);

    }

    @Override
    public void setOnLocationFoundListener(String cityName, String stateName, String countryName) {
        locationTextview.setText("City : "+cityName+", "+"\nState : "+stateName+", "+"\nCountry : "+countryName);
    }

    @Override
    public void setOnLocationNotFoundListener() {
        locationTextview.setText("Could not found your location, perhaps your connection is not good :(");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.getLocationTwo:
            {
                MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
                    @Override
                    public void gotLocation(Location location){
                        //Got the location!
                        setLocationTwoTV.setText(location.toString());
                    }
                };
                MyLocation myLocation = new MyLocation();
                myLocation.getLocation(this, locationResult);
            }
            break;
        }
    }
}
