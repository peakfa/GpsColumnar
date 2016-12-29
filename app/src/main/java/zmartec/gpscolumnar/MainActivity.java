package zmartec.gpscolumnar;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private Context mContext;
    GpsView mGpsView;
    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initGpsView();
        InitGpsDevice();
    }

    private void initGpsView() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.drawlayout);
        mGpsView = new GpsView(this);
        mGpsView.setMinimumHeight(180);
        mGpsView.setMinimumWidth(500);
        mGpsView.invalidate();
        layout.addView(mGpsView);
    }
    public void InitGpsDevice() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG,"have no gps permission");
        }else {
            mLocationManager.addGpsStatusListener(mGpsStatuslistener);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
        }
    }
    private LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateView(location);
            Log.i(TAG, "Time：" + location.getTime());
            Log.i(TAG, "Long：" + location.getLongitude());
            Log.i(TAG, "lati：" + location.getLatitude());
            Log.i(TAG, "High：" + location.getAltitude());
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.i(TAG, "AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.i(TAG, "OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.i(TAG, "TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }

        public void onProviderEnabled(String provider) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"have no gps permission");
            }else {
                Location location = mLocationManager.getLastKnownLocation(provider);
                updateView(location);
            }
        }

        public void onProviderDisabled(String provider) {
            updateView(null);
        }
    };

    private GpsStatus.Listener mGpsStatuslistener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.i(TAG, "GPS_EVENT_FIRST_FIX");
                    break;
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    Log.i(TAG, "GPS_EVENT_SATELLITE_STATUS");
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG,"have no gps permission");
                    }else {
                        GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
                        mGpsView.setGpsStatus(gpsStatus);
                        mGpsView.invalidate();
                    }
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.i(TAG, "GPS_EVENT_STARTED");
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.i(TAG, "GPS_EVENT_STOPPED");
                    break;
            }
        }
    };

    public void updateView(Location location) {
        TextView latitudeview = (TextView) findViewById(R.id.Latitudeview);
        TextView longitudeview = (TextView) findViewById(R.id.Longitudeview);
        TextView altitudeview = (TextView) findViewById(R.id.Altitudeview);
        TextView speedview = (TextView) findViewById(R.id.speedview);
        TextView utcview = (TextView) findViewById(R.id.utcview);

        if (location != null) {
            latitudeview.setText(String.valueOf("经度：" + location.getLongitude()));
            longitudeview.setText(String.valueOf("纬度：" + location.getLatitude()));
            altitudeview.setText(String.valueOf("海拔：" + location.getAltitude()));
            speedview.setText(String.valueOf("速度：" + location.getSpeed()));
            utcview.setText(String.valueOf("时间：" + location.getTime()));
        } else {
            latitudeview.setText("经度：");
            longitudeview.setText("纬度：");
            altitudeview.setText("海拔：");
            speedview.setText("速度：");
            utcview.setText("时间：");
        }
    }
}
