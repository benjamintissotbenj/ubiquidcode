package com.tabs.ubiquidcode;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.vision.barcode.Barcode;

public class GlobalViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Barcode> mBarcode;

    public GlobalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is scan fragment");
        mBarcode = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<Barcode> getBarcode() {
        return mBarcode;
    }
    public void setBarcode(Barcode barcode) {
        mBarcode.setValue(barcode);
    }
}
