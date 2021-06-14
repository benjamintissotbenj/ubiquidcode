package com.tabs.moneyconversion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView outputText;
    private TextView outputCurrency;
    private Button inputCurrencyButton;
    private TextView inputTextHint;
    private TextView outputTextHint;
    private Toolbar mTopToolbar;
    private TextView rateValue;


    private double conversionRate = 1.3;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rateValue = (TextView) findViewById(R.id.rate_value);
        editText = (EditText) findViewById(R.id.edit_input);
        outputText = (TextView) findViewById(R.id.output);

        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        inputTextHint = (TextView) findViewById(R.id.input_text);
        outputTextHint= (TextView) findViewById(R.id.output_text);

        //switch case sur inputCurrency et outputCurrency pour savoir si on affiche € ou $ ou £
    }

    @Override
    public void onResume(){
        SharedPreferences preferencesCurrency = getSharedPreferences(getString(R.string.currencies),Context.MODE_PRIVATE);
        String inputCurrency = preferencesCurrency.getString(getString(R.string.input_currency),"EUR");
        String outputCurrency = preferencesCurrency.getString(getString(R.string.output_currency),"EUR");
        SharedPreferences preferencesRate = getSharedPreferences(getString(R.string.rate_preference),Context.MODE_PRIVATE);
        double rate = (double) (Math.floor(preferencesRate.getFloat(getString(R.string.rate),(float) 1)*1000)/1000);
        rateValue.setText(String.valueOf(rate));

        switch (inputCurrency){
            case "EUR":
                inputTextHint.setText(R.string.input_value_euros);
                break;
            case "USD":
                inputTextHint.setText(R.string.input_value_dollars);
                break;
            case "GBP":
                inputTextHint.setText(R.string.input_value_pounds);
                break;
            default:
                inputTextHint.setText(R.string.input_value);
        }

        switch (outputCurrency){
            case "EUR":
                outputTextHint.setText(R.string.output_value_euros);
                break;
            case "USD":
                outputTextHint.setText(R.string.output_value_dollars);
                break;
            case "GBP":
                outputTextHint.setText(R.string.output_value_pounds);
                break;
            default:
                outputTextHint.setText(R.string.output_value);
        }

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void convert(View caller){
        String inputString = editText.getText().toString();
        SharedPreferences preferences = getSharedPreferences(getString(R.string.rate_preference), Context.MODE_PRIVATE);

        this.conversionRate = preferences.getFloat(getString(R.string.rate), 1);
        try{
            double inputValue = Double.valueOf(inputString);
            String outputString = String.valueOf(inputValue*conversionRate);
            outputText.setText(outputString);
            Toast.makeText(this, getString(R.string.conversion), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, getString(R.string.error_conversion), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "convert: invalid input");

        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void chooseCurrency(View sender){
        //open other activity
        Intent intent = new Intent(this, CurrencyActivity.class);
        startActivity(intent);
    }



}