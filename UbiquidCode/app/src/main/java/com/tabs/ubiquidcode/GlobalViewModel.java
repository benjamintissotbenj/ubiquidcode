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
    private MutableLiveData<Integer> mNumberTested;

    public GlobalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is scan fragment");
        mBarcode = new MutableLiveData<>();
        mTestBarcodes = new MutableLiveData<>(new ArrayList<Barcode>());
        mNumberTested = new MutableLiveData<>(Integer.valueOf(0));
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

    public void setTestBarcodes(ArrayList<Barcode> barcodes){
        mTestBarcodes.setValue(barcodes);
    }
    public ArrayList<Barcode> getTestBarcodes(){
        return mTestBarcodes.getValue();
    }
    public void setNumberTested(Integer n){mNumberTested.setValue(n);}
    public LiveData<Integer> getNumberTested() {
        return mNumberTested;
    }
}
