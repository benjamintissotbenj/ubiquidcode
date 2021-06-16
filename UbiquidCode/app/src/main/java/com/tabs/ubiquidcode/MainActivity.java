package com.tabs.ubiquidcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GlobalViewModel globalViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_scan, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        //getActionBar().setIcon(scaleImage(getDrawable(R.drawable.logo),(float) 0.5));
        globalViewModel =
                ViewModelProviders.of(this).get(GlobalViewModel.class);


        globalViewModel.getBarcode().observe(this, new Observer<Barcode>() {
            @Override
            public void onChanged(Barcode barcode) {

            }
        });
    }
    private Drawable scaleImage (Drawable image, float scaleFactor) {

        if ((image == null) || !(image instanceof BitmapDrawable)) {
            return image;
        }

        Bitmap b = ((BitmapDrawable)image).getBitmap();

        int sizeX = Math.round(image.getIntrinsicWidth() * scaleFactor);
        int sizeY = Math.round(image.getIntrinsicHeight() * scaleFactor);

        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);

        image = new BitmapDrawable(getResources(), bitmapResized);

        return image;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==0){
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null){
                    Toast.makeText(this, "Barcode found", Toast.LENGTH_SHORT).show();
                    globalViewModel.setBarcode((Barcode) data.getParcelableExtra("barcode"));
                }

            }
        }
        else if(requestCode==100){
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null){
                    Toast.makeText(this, "Test Barcodes found", Toast.LENGTH_SHORT).show();
                    Bundle args = data.getBundleExtra("barcodes");
                    ArrayList<Barcode> barcodes = (ArrayList<Barcode>) args.getSerializable("serializableBarcodes");
                    globalViewModel.setTestBarcodes(barcodes);
                    globalViewModel.setNumberTested(Integer.valueOf(barcodes.size()));
                }

            }
        }
        else{
            Toast.makeText(this, "No Barcode found", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}