package com.tabs.moneyconversion;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class CurrencyActivity extends AppCompatActivity {


    private static final String TAG = "CurrencyActivity";
    private Toolbar mTopToolbar;
    private JSONObject rates;
    private RadioGroup inputGroup;
    private RadioGroup outputGroup;
    private RadioButton inputEuros;
    private RadioButton inputDollars;
    private RadioButton inputPounds;
    private RadioButton outputEuros;
    private RadioButton outputDollars;
    private RadioButton outputPounds;
    private Button save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final CurrencyActivity thiss = this;
        setContentView(R.layout.activity_currency);
        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        inputGroup = findViewById(R.id.radioInput);
        outputGroup = findViewById(R.id.radioOutput);
        inputDollars = findViewById(R.id.radioInputDollars);
        inputEuros = findViewById(R.id.radioInputEuros);
        inputPounds = findViewById(R.id.radioInputPounds);
        outputDollars = findViewById(R.id.radioOutputDollars);
        outputEuros = findViewById(R.id.radioOutputEuros);
        outputPounds = findViewById(R.id.radioOutputPounds);
        save = findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thiss.saveCurrencies();
            }
        });

        SharedPreferences preferencesCurrency = getSharedPreferences(getString(R.string.currencies),Context.MODE_PRIVATE);
        String inputCurrency = preferencesCurrency.getString(getString(R.string.input_currency),"EUR");
        String outputCurrency = preferencesCurrency.getString(getString(R.string.output_currency),"EUR");
        switch (inputCurrency){
            case "EUR":
                inputEuros.setChecked(true);
                break;
            case "USD":
                inputDollars.setChecked(true);
                break;
            case "GBP":
                inputPounds.setChecked(true);
                break;
            default:
                inputEuros.setChecked(true);
        }

        switch (outputCurrency){
            case "EUR":
                outputEuros.setChecked(true);
                break;
            case "USD":
                outputDollars.setChecked(true);
                break;
            case "GBP":
                outputPounds.setChecked(true);
                break;
            default:
                outputEuros.setChecked(true);
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){

        rates = loadJSONFile();

        super.onResume();

    }


    public void saveCurrencies(){
        String inputCurrency = getInputCurrency();
        String outputCurrency = getOutputCurrency();
        double dollarsToInput = 0. ;
        double dollarsToOutput = 0.;
        SharedPreferences preferences = getSharedPreferences(getString(R.string.currencies),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.input_currency),inputCurrency);
        editor.putString(getString(R.string.output_currency),outputCurrency);
        editor.apply();



        try{
            dollarsToInput = Double.valueOf(rates.get(inputCurrency).toString());
            dollarsToOutput = Double.valueOf(rates.get(outputCurrency).toString());
        }catch(JSONException e){
            Log.d(TAG, "saveCurrencies: no such currency as "+ inputCurrency + " or " + outputCurrency);
        }

        saveRate(dollarsToInput, dollarsToOutput);
    }

    public void saveRate(double dollarsToInput,double dollarsToOutput){

        SharedPreferences preferences = getSharedPreferences(getString(R.string.rate_preference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        double rate = dollarsToOutput/dollarsToInput;
        float newrate = (float) (Math.floor(1000*dollarsToOutput/dollarsToInput)/1000);
        editor.putFloat(getString(R.string.rate), newrate);
        editor.apply();
        Toast.makeText(this, "New Rate: " + (newrate), Toast.LENGTH_LONG).show();
        finish();
    }


    private String getInputCurrency(){
        int id = inputGroup.getCheckedRadioButtonId();
        String result = "EUR";

        switch (id){
            case R.id.radioInputDollars:
                result = "USD";
                break;
            case R.id.radioInputEuros:
                result = "EUR";
                break;
            case R.id.radioInputPounds:
                result = "GBP";
                break;
            default:
                result = "EUR";
        }
        return result;
    }


    private String getOutputCurrency(){
        int id = outputGroup.getCheckedRadioButtonId();
        String result = "USD";

        switch (id){
            case R.id.radioOutputDollars:{
                result = "USD";
                break;
            }
            case R.id.radioOutputEuros:
                result = "EUR";
                break;
            case R.id.radioOutputPounds:
                result = "GBP";
                break;
            default:
                result = "USD";
        }
        return result;
    }




    private JSONObject loadJSONFile(){

        URL exchangeRatesURL = null;
        try {
            exchangeRatesURL = new URL("https://perso.telecom-paristech.fr/eagan/cours/igr201/data/taux_2017_11_02.json");
        } catch(IOException e){
            Log.d(TAG, "loadJSONFile: problem loading the URL");
        }
        DownloadRatesTask task = new DownloadRatesTask();
        task.execute(exchangeRatesURL);
        JSONObject result = null;
        try{
            result = task.get().getJSONObject("rates");
        } catch (ExecutionException e){
            Log.d(TAG, "loadJSONFile: execution exception");
        }catch (InterruptedException e){
            Log.d(TAG, "loadJSONFile: interrupted exception");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private class DownloadRatesTask extends AsyncTask<URL, Integer,JSONObject> {

        protected JSONObject doInBackground(URL... urls){
            int count = urls.length;

            for (int i = 0; i < count; i++) {
                InputStream inputStream = null;
                try {
                 inputStream= urls[i].openStream();
                }catch(IOException e){
                    Log.d(TAG, "doInBackground: IO Exception : could not read from URL");
                }
                StringBuilder stringBuilder = new StringBuilder();
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
                    String line = null;
                    while ((line = reader.readLine()) != null){
                        stringBuilder.append(line + "\n");
                    }
                    String jsonString = stringBuilder.toString();
                    return new JSONObject(jsonString);
                } catch ( IOException e){
                    System.err.println("Warning : could not read rates: " + e.getLocalizedMessage());
                }catch ( JSONException e){
                    System.err.println("Warning : could not parse rates: " + e.getLocalizedMessage());
                }

                publishProgress((int) ((i / (float) count) * 100));
                // Escape early if cancel() is called
                if (isCancelled()) break;

                return null;
            }

            return null;
        }



    }

}
