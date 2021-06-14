package com.tabs.ubiquidcode.ui.scan;

import android.Manifest;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.tabs.ubiquidcode.GlobalViewModel;
import com.tabs.ubiquidcode.R;
import com.tabs.ubiquidcode.ScanBarCodeActivity;

import java.io.IOException;

public class ScanFragment extends Fragment {

    private ScanViewModel scanViewModel;
    private GlobalViewModel globalViewModel;
    private TextView result;
    private TextView name;
    private TextView number;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scanViewModel =
                ViewModelProviders.of(this).get(ScanViewModel.class);
        globalViewModel =
                ViewModelProviders.of(getActivity()).get(GlobalViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        final TextView textView = root.findViewById(R.id.text_scan);
        scanViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        result = (TextView) root.findViewById(R.id.scan_barcode_result);
        name = (TextView) root.findViewById(R.id.result_name);
        name.setText("Contains the name of a contact");
        number = (TextView) root.findViewById(R.id.result_number);
        number.setText("Contains the number of a contact");

        globalViewModel.getBarcode().observe(getActivity(), new Observer<Barcode>() {
            @Override
            public void onChanged(Barcode barcode) {
                String returnValue = barcode.displayValue;
                if (barcode.valueFormat == Barcode.CONTACT_INFO){
                    Barcode.ContactInfo info = barcode.contactInfo;
                    name.setText(info.name.first+ " " + info.name.last);
                    number.setText(info.phones[0].number);
                }

                result.setText("Barcode value : " + returnValue);
            }
        });

        ((Button) root.findViewById(R.id.scan_barcode)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Trying to start activity", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ScanBarCodeActivity.class);
                getActivity().startActivityForResult(intent,0);
            }
        });



        return root;
    }


}