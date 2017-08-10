package idv.ron.mapdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;

public class MapTypeUiSettingsActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private final static String TAG = "MapTypeUiSettings";

    private GoogleMap map;
    private UiSettings uiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_type_ui_settings_activity);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()//得到FragmentManager
                        .findFragmentById(R.id.fmMap);//取的googleMap的FragmentManager物件
        mapFragment.getMapAsync(this);//取的上面的map圖資，對Google Server提出請求，如果接受到回應，會自動跑onMapReady
    }

    @Override
    public void onMapReady(GoogleMap map) {//implements OnMapReadyCallback
        this.map = map;
        setUpMap();
        setMyMapType();
    }

    private void setUpMap() {
        map.setTrafficEnabled(true);//顯示交通繁忙處
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==//檢查使用者是否同意
                PackageManager.PERMISSION_GRANTED) {//同意
            map.setMyLocationEnabled(true);//顯示取的現在位置的按鈕
        }

        uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
    }

    private void setMyMapType() {
        Spinner spinner = (Spinner) findViewById(R.id.spMapType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.mapTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String mapType = parent.getItemAtPosition(position).toString();
                if (mapType.equals(getString(R.string.normal))) {
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else if (mapType.equals(getString(R.string.hybrid))) {
                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else if (mapType.equals(getString(R.string.satellite))) {
                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if (mapType.equals(getString(R.string.terrain))) {
                    map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                } else {
                    Log.e(TAG, mapType + " " + getString(R.string.msg_ErrorSettings));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing.
            }
        });
    }

    public void onTrafficClick(View view) {
        map.setTrafficEnabled(((CheckBox) view).isChecked());
    }

    public void onZoomControlsClick(View view) {
        uiSettings.setZoomControlsEnabled(((CheckBox) view).isChecked());
    }

    public void onCompassClick(View view) {
        uiSettings.setCompassEnabled(((CheckBox) view).isChecked());
    }

    public void onMyLocationButtonClick(View view) {
        uiSettings.setMyLocationButtonEnabled(((CheckBox) view).isChecked());
    }

    public void onMyLocationLayerClick(View view) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(((CheckBox) view).isChecked());
        }
    }

    public void onScrollGesturesClick(View view) {
        uiSettings.setScrollGesturesEnabled(((CheckBox) view).isChecked());
    }

    public void onZoomGesturesClick(View view) {
        uiSettings.setZoomGesturesEnabled(((CheckBox) view).isChecked());
    }

    public void onTiltGesturesClick(View view) {
        uiSettings.setTiltGesturesEnabled(((CheckBox) view).isChecked());
    }

    public void onRotateGesturesClick(View view) {
        uiSettings.setRotateGesturesEnabled(((CheckBox) view).isChecked());
    }
}
