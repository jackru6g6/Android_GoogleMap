package idv.ron.mapdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    // 儲存著多個新頁面資訊
    private List<DetailPage> detailPages;
    private Button test;


    // 一個DetailPage物件代表一頁，也就是一個範例內容
    private class DetailPage {
        // 新頁面的標題id
        private int titleId;
        // 新頁面屬於哪種類別
        private Class<? extends FragmentActivity> detailActivity;

        public DetailPage(int titleId,
                          Class<? extends FragmentActivity> detailActivity) {
            this.titleId = titleId;
            this.detailActivity = detailActivity;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        // 有多少個範例，就加上多少個DetailPage物件

        test = (Button) findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateIntent = new Intent();
                updateIntent.setClass(MainActivity.this, MarkersActivity.class);
                startActivity(updateIntent);
            }
        });
        detailPages = new ArrayList<>();
        detailPages.add(new DetailPage(R.string.title_BasicMap,
                BasicMapActivity.class));
        detailPages.add(new DetailPage(R.string.title_MapTypeUiSettings,
                MapTypeUiSettingsActivity.class));
//        detailPages.add(new DetailPage(R.string.title_Markers,
//                MarkersActivity.class));
        detailPages.add(new DetailPage(R.string.title_PolylinesPolygons,
                PolylinesPolygonsActivity.class));
        detailPages.add(new DetailPage(R.string.title_Geocoder,
                GeocoderActivity.class));

        List<String> detailTitles = new ArrayList<>();
        for (DetailPage detailPage : detailPages) {
            detailTitles.add(getString(detailPage.titleId));
        }
        ListView listView = (ListView) findViewById(R.id.listView);
        // 將各個範例標題文字存入List後套用在ListView選項列上
        listView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, detailTitles));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailPage detailpage = detailPages.get(position);
                startActivity(new Intent(MainActivity.this, detailpage.detailActivity));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        askPermissions();
    }

    private static final int REQ_PERMISSIONS = 0;

    // New Permission see Appendix A
    private void askPermissions() {
        //因為是群組授權，所以請求ACCESS_COARSE_LOCATION就等同於請求ACCESS_FINE_LOCATION，因為同屬於LOCATION群組
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    REQ_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSIONS:
                String text = "";
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        text += permissions[i] + "\n";
                    }
                }
                if (!text.isEmpty()) {
                    text += getString(R.string.text_NotGranted);
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
