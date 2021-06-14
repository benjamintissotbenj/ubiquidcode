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

import com.tabs.ubiquidcode.GlobalViewModel;
import com.tabs.ubiquidcode.R;
import com.tabs.ubiquidcode.ScanBarCodeActivity;
import com.tabs.ubiquidcode.TestBarCodeActivity;

public class TestFragment extends Fragment {

    private TestViewModel testViewModel;
    private GlobalViewModel globalViewModel;

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

        ((Button) root.findViewById(R.id.start_test_activity)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), TestBarCodeActivity.class);
                getActivity().startActivityForResult(intent,100);
            }
        });

        return root;
    }
}