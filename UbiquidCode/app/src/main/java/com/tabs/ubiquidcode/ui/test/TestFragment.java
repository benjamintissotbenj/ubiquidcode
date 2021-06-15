package com.tabs.ubiquidcode.ui.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.vision.barcode.Barcode;
import com.tabs.ubiquidcode.GlobalViewModel;
import com.tabs.ubiquidcode.R;
import com.tabs.ubiquidcode.ScanBarCodeActivity;
import com.tabs.ubiquidcode.TestBarCodeActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TestFragment extends Fragment {

    private TestViewModel testViewModel;
    private GlobalViewModel globalViewModel;
    private TextView[] textviews = new TextView[4];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        testViewModel =
                ViewModelProviders.of(this).get(TestViewModel.class);
        globalViewModel =
                ViewModelProviders.of(getActivity()).get(GlobalViewModel.class);
        View root = inflater.inflate(R.layout.fragment_test, container, false);
        final TextView textView = root.findViewById(R.id.text_test);
        testViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        textviews[0] = (TextView) root.findViewById(R.id.test_value_1);
        textviews[1] = (TextView) root.findViewById(R.id.test_value_2);
        textviews[2] = (TextView) root.findViewById(R.id.test_value_3);
        textviews[3] = (TextView) root.findViewById(R.id.test_value_4);

        globalViewModel.getNumberTested().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ArrayList<Barcode> barcodes = globalViewModel.getTestBarcodes();
                for (int i=0;i<min(4,barcodes.size());i++){
                    textviews[i].setText(barcodes.get(i).displayValue);
                }
            }
        });

        ((Button) root.findViewById(R.id.start_test_activity)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), TestBarCodeActivity.class);
                getActivity().startActivityForResult(intent,100);
            }
        });

        return root;
    }

    private int min(int a, int b){
        if (a>b) return b;
        return a;
    }
}