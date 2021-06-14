package com.tabs.ubiquidcode;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;

public class GlobalViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Barcode> mBarcode;
    private MutableLiveData<ArrayList<Barcode>> mTestBarcodes;

    public GlobalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is scan fragment");
        mBarcode = new MutableLiveData<>();
        mTestBarcodes = new MutableLiveData<>(new ArrayList<Barcode>());
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

    public void addTestBarcode(Barcode barcode){
        mTestBarcodes.getValue().add(barcode);
    }
    public ArrayList<Barcode> getTestBarcodes(){
        return mTestBarcodes.getValue();
    }
}
