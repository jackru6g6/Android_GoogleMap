package idv.ron.mapdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GeocoderActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private final static String TAG = "GeocoderActivity";
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geocoder_activity);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fmMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        /*//讓使用者點擊地圖做標示，利用點擊經緯度找出地址
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Geocoder geocoder = new Geocoder(GeocoderActivity.this);
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                }catch (Exception e){

                }
            }
        });
        */
        setUpMap();
    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    public void onLocationNameClick(View view) {
        EditText etLocationName = (EditText) findViewById(R.id.etLocationName);
        String locationName = etLocationName.getText().toString().trim();
        if (locationName.length() > 0) {
            locationNameToMarker(locationName);
        } else {
            showToast(R.string.msg_LocationNameIsEmpty);
        }
    }

    private void locationNameToMarker(String locationName) {
        map.clear();
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        int maxResults = 1;
        try {
            addressList = geocoder
                    .getFromLocationName(locationName, maxResults);//.getFromLocationName(地名或地址,希望回傳的筆數)，回傳List物件
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
            showToast(R.string.msg_LocationNameNotFound);
        } else {
            Address address = addressList.get(0);//取得物件，第1筆，index(0)

            LatLng position = new LatLng(address.getLatitude(),//取的經緯度
                    address.getLongitude());

            String snippet = address.getAddressLine(0);//取得地址index(0)

            map.addMarker(new MarkerOptions().position(position)
                    .title(locationName).snippet(snippet));

            //------------拉畫面，將查詢內容傳到畫面正中央--------------------
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(position).zoom(15).build();
            map.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            //-------------------------------------------------------------
        }
    }

    private void showToast(int messageResId) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
