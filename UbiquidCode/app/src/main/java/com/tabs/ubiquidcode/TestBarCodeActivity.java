package com.tabs.ubiquidcode;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.tabs.ubiquidcode.ui.scan.ScanFragment;
import com.tabs.ubiquidcode.ui.scan.ScanViewModel;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class TestBarCodeActivity extends Activity {


    private SurfaceView cameraPreview;
    private TextView count;
    private Button startTest;
    private boolean testing = false;
    private ArrayList<Barcode> detectedCodes= new ArrayList<Barcode>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        cameraPreview = (SurfaceView) findViewById(R.id.camera_preview);
        count = findViewById(R.id.compteur);
        startTest = findViewById(R.id.start_test);
        startTest.setText("Start the test");
        createCameraSource();
        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testing = true;
                startTest.setText("Test Running");
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        exit();
                    }
                }, 15000);



            }
        });

        //SharedPreferences sharedPreferences = getSharedPreferences("name",MODE_PRIVATE);

        //when asyncTask is ended :
        /*
        intent.putExtra("barcode",barcodes.valueAt(0)); //last barcode from array
        setResult(CommonStatusCodes.SUCCESS,intent);
        finish();
        */


    }

    private void exit(){
        testing = false;
        Intent intent = new Intent();
        Bundle args = new Bundle();
        args.putSerializable("serializableBarcodes",(Serializable) detectedCodes);
        intent.putExtra("barcodes", args); //all different detected barcodes
        setResult(CommonStatusCodes.SUCCESS, intent);
        finish();
    }


    private void createCameraSource() {
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600, 1024)
                .build();
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                ActivityCompat.requestPermissions(TestBarCodeActivity.this,new String[] {Manifest.permission.CAMERA},0);
                if (ActivityCompat.checkSelfPermission(TestBarCodeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(TestBarCodeActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                if (testing) {
                    final SparseArray<Barcode> barcodes= detections.getDetectedItems();
                    if (barcodes.size()>0) {
                        Barcode barcode = barcodes.valueAt(0);
                        if (!alreadyDetected(barcode)) {
                            detectedCodes.add(barcode);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {count.setText(String.valueOf(detectedCodes.size()));}
                            });

                        }
                    }
                }
            }
        });

    }


    protected boolean alreadyDetected(Barcode barcode){
        String displayValue = barcode.displayValue;
        for (Barcode barcode1 : detectedCodes){
            if (barcode1.displayValue.equals(displayValue)) return true;
        }
        return false;
    }
}
