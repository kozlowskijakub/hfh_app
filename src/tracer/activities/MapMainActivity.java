package tracer.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.example.hfh_app.shoppingcard.R;
import tracer.view.GpsStatus;

import java.util.ArrayList;
import java.util.Iterator;

public class MapMainActivity extends Activity implements android.location.GpsStatus.Listener {

    public ArrayList<GpsSatellite> satellites = new ArrayList<GpsSatellite>();
    LocationManager locationManager = null;

    boolean draw = true;


    public void drawGps(View v) {
        GpsStatus gpsStatusView = new GpsStatus(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_main);


        if (draw == false)
            ll.removeViewAt(ll.getChildCount() - 1);
        this.draw = false;
        ll.addView(gpsStatusView, params);
    }

    public void showMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Log.e("myException", e.toString());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.addGpsStatusListener(this);
            drawGps(new View(this));
        } catch (Exception e) {

            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void onGpsStatusChanged(int event) {
        android.location.GpsStatus gpsStatus = locationManager.getGpsStatus(null);
        String strGpsStats = null;

        if (gpsStatus != null) {
            Iterable<GpsSatellite> satellites = gpsStatus.getSatellites();
            Iterator<GpsSatellite> sat = satellites.iterator();
            int i = 0;
            while (sat.hasNext()) {
                GpsSatellite satellite = sat.next();
                strGpsStats += (i++) + ": "
                        + satellite.getPrn()
                        + ", " + satellite.usedInFix()
                        + "," + satellite.getSnr()
                        + "," + satellite.getAzimuth()
                        + "," + satellite.getElevation()
                        + "\n\n";
            }
        }
    }
}
