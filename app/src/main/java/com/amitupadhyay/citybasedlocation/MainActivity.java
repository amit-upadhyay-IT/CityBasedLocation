package com.amitupadhyay.citybasedlocation;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amitupadhyay.citybasedlocation.locationapi.activity.EampleActiivty;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationFinder.OnLocationNotifiedListener, View.OnClickListener {

    Button getCurrentLocation;
    TextView locationTextview;

    Button getLocationTwoBtn, getLocationThreeBtn;
    TextView setLocationTwoTV, setLocationThreeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCurrentLocation = (Button) findViewById(R.id.getCurrentCity);
        locationTextview = (TextView) findViewById(R.id.current_city);

        setLocationTwoTV = (TextView) findViewById(R.id.showLocationTwo);
        getLocationTwoBtn = (Button) findViewById(R.id.getLocationTwo);

        setLocationThreeTV = (TextView) findViewById(R.id.showLocationThree);
        getLocationThreeBtn = (Button) findViewById(R.id.getLocationThree);

        getCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationFinder locationFinder = new LocationFinder(MainActivity.this);
                locationFinder.getCityByLocation();

            }
        });

        getLocationTwoBtn.setOnClickListener(this);
        getLocationThreeBtn.setOnClickListener(this);
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
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        /*Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(lat, lon, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (addresses != null)
                        {
                            String cityName = addresses.get(0).getAddressLine(0);
                            String stateName = addresses.get(0).getAddressLine(1);
                            String countryName = addresses.get(0).getAddressLine(2);
                            setLocationTwoTV.setText("City : "+cityName+", "+"\nState : "+stateName+", "+"\nCountry : "+countryName);
                        }
                        else
                        {
                            setLocationTwoTV.setText("Address is null");
                        }*/
                        String completeAddress = getCompleteAddressString(lat, lon);
                        setLocationTwoTV.setText(completeAddress);
                    }
                };
                MyLocation myLocation = new MyLocation();
                myLocation.getLocation(this, locationResult);
            }
            break;
            case R.id.getLocationThree:
            {
                startActivity(new Intent(this, EampleActiivty.class));

            }
            break;
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                //Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                //Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }
}
