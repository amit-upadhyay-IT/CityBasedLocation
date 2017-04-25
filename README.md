# CityBasedLocation

Get the city based location in no time :). I have made everything ready for you.

Just get this class - https://github.com/amit-upadhyay-IT/CityBasedLocation/blob/master/app/src/main/java/com/amitupadhyay/citybasedlocation/LocationFinder.java

Now instantiate the LocationFinder class and call the method getCityByLocation().

eg:

                LocationFinder locationFinder = new LocationFinder(MainActivity.this);
                locationFinder.getCityByLocation();
                
                // don't forget to pass the Activity context and don't pass the Application Context.

Make your Activity Implement `LocationFinder.OnLocationNotifiedListener` and override required method to update the View in your Activity.

EG:

    @Override
    public void setOnLocationFoundListener(String cityName, String stateName, String countryName) {
        locationTextview.setText("City : "+cityName+", "+"\nState : "+stateName+", "+"\nCountry : "+countryName);
    }

    @Override
    public void setOnLocationNotFoundListener() {
        locationTextview.setText("Could not found your location, perhaps your connection is not good :(");
    }


# -Amit Upadhyay (http://minimumstack.com/)
