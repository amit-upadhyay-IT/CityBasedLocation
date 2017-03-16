# CityBasedLocation

Get the city based location in no time :). I have made everything ready for you.

Just get this class - https://github.com/amit-upadhyay-IT/CityBasedLocation/blob/master/app/src/main/java/com/amitupadhyay/citybasedlocation/LocationFinder.java

Now instantiate the LocationFinder class and call the method getCityByLocation().

eg:

                LocationFinder locationFinder = new LocationFinder(MainActivity.this);
                locationFinder.getCityByLocation();
                
                // don't forget to pass the Activity context and don't pass the Application Context.

Make your Activity Implement OnLocationFoundListener and override required method to update the View in your Activity.


-Amit Upadhyay (http://minimumstack.com/)
