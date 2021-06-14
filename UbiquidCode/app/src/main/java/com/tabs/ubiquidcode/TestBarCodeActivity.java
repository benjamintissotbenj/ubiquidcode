package com.tabs.ubiquidcode;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
        createCameraSource();
        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: create an async task that waits 15 seconds and then sets this to false
                //début du test ici, pendant 15 secondes
                //createCameraSource();

                testing = true;
            }
        });

        //when asyncTask is ended :
        /*
        intent.putExtra("barcode",barcodes.valueAt(0)); //last barcode from array
        setResult(CommonStatusCodes.SUCCESS,intent);
        finish();
        */


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
                    Barcode barcode = barcodes.valueAt(0);
                    if (!detectedCodes.contains(barcode)){
                        detectedCodes.add(barcode);
                        //Toast.makeText(TestBarCodeActivity.this, "Detected a New Code", Toast.LENGTH_SHORT).show();
                        count.setText(String.valueOf(detectedCodes.size()));
                        if(detectedCodes.size()>3){
                            Intent intent = new Intent();
                            intent.putExtra("barcode",barcodes.valueAt(0)); //last barcode from array
                            setResult(CommonStatusCodes.SUCCESS,intent);
                            finish();
                        }
                    }
                    /*final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                    if (barcodes.size() > 0 && barcodes.size() <= 10)
                        Toast.makeText(TestBarCodeActivity.this, "Detected barcode number " +barcodes.size(), Toast.LENGTH_SHORT).show();
                    if (barcodes.size() > 10) {
                        Intent intent = new Intent();
                        intent.putExtra("barcode", barcodes.valueAt(0)); //last barcode from array
                        setResult(CommonStatusCodes.SUCCESS, intent);
                        finish();
                    }*/
                }
                //Toast.makeText(TestBarCodeActivity.this, "Currently not testing", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
