package tracer.activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jk.hfh_app.R;
import tracer.database.CustomPOI;
import tracer.view.MapLocation;
import tracer.logicObjects.POI;

import java.util.ArrayList;


public class MapActivity extends Activity implements LocationListener {

    public static ArrayList<POI> poiList = new ArrayList<POI>();

    public static double maxNorth;
    public static double maxSouth;
    public static double maxEast;
    public static double maxWest;
    private static Integer numberOfPOI = 0;
    //    private Location location;
//    private Location previousLocation;
//    private CustomPOI poi;
    private POI poi;
    private LocationManager myLocationManager;
    private LocationListener myLocationLister;
    private TextView lattitudeView;
    private TextView longitudeView;
    private TextView distanceView;
    private MapLocation myLocationView;
    private boolean firstPoi = true;
    private static double distanceValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);
        myLocationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        myLocationLister = this;
        myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationLister);

        this.lattitudeView = (TextView) findViewById(R.id.tv_lattitude);
        this.longitudeView = (TextView) findViewById(R.id.tv_longitude);
        this.distanceView = (TextView) findViewById(R.id.tv_distance);

        myLocationView = new MapLocation(this);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_mapView);
        ll.addView(myLocationView, ll.getChildCount());
    }

    @Override
    public void onLocationChanged(Location location) {
        this.poi = new POI(location);
        this.poi.order_id = MapActivity.numberOfPOI;
        MapActivity.numberOfPOI++;

//        try {
//            POI pnull = new POI((Location) null);
//        } catch (Exception e) {
//            Log.e("pnull", e.toString());
//        }
//        try {

//        } catch (Exception e) {
//            Log.e("pempty", e.toString());
//        }
//        try {
//            POI pstringnull = new POI((String) null);
//        } catch (Exception e) {
//            Log.e("pstringnull", e.toString());
//        }
//        try {
//            POI pLocationEmpty = new POI(new Location(""));
//        } catch (Exception e) {
//            Log.e("pLocationEmpty", e.toString());
//        }

        if (firstPoi == false) {
            POI lastPoi = poiList.get(poiList.size() - 1);
//            Location last = new Location(null);

            MapActivity.distanceValue += this.poi.distanceTo(lastPoi);
            this.distanceView.setText(String.format("%.2f km, poi: %d", MapActivity.distanceValue / 1000, poiList.size()));
            checkMaximumDimensions(this.poi);
        } else {
            MapActivity.maxNorth = location.getLatitude();
            MapActivity.maxSouth = location.getLatitude();
            MapActivity.maxEast = location.getLongitude();
            MapActivity.maxWest = location.getLongitude();
        }

        this.firstPoi = false;
        poiList.add(poi);
        this.lattitudeView.setText(String.valueOf(poi.getLatitude()));
        this.longitudeView.setText(String.valueOf(poi.getLongitude()));
        myLocationView.invalidate();
    }


    public void checkMaximumDimensions(POI poi) {
        if (poi.getLongitude() < maxWest) {
            maxWest = poi.getLongitude();
        }
        if (poi.getLongitude() > maxEast) {
            maxEast = poi.getLongitude();
        }
        if (poi.getLatitude() > maxNorth) {
            maxNorth = poi.getLatitude();
        }
        if (poi.getLatitude() < maxSouth) {
            maxSouth = poi.getLatitude();
        }
        Log.i("maxNorth: ", String.valueOf(MapActivity.maxNorth));
        Log.i("maxSouth: ", String.valueOf(MapActivity.maxSouth));
        Log.i("maxEast: ", String.valueOf(MapActivity.maxEast));
        Log.i("maxWest: ", String.valueOf(MapActivity.maxWest));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public void recording(View view) {
        if (((Button) view).getText().toString().equals("Start recording")) {
            ((Button) view).setText("Stop recording");
            // save track in database (set in settings if save automatically)
            //
//            Location loca = poiList.get(0);

        } else {
            ((Button) view).setText("Start recording");

        }
    }

    public void drawGPS(View view) {
        Intent intent = new Intent(this, MapMainActivity.class);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Log.e("mapDrawGPS", e.toString());
        }
    }
}
