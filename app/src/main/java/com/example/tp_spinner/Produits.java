package com.example.tp_spinner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class Produits extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText editNum, editNum2, editQuantity, editQuantity2, editPrice, editPrice2;
    Spinner spinnerCategorie, spinnerCategorie2;
    String tva, remise, text = "", text2 = "";
    Double result, resultTva;
    Button btnCalculer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produits);

        editNum = (EditText) findViewById(R.id.number10);
        editQuantity = (EditText) findViewById(R.id.number20);
        editPrice = (EditText) findViewById(R.id.number30);
        spinnerCategorie = (Spinner) findViewById(R.id.spinner);

        editNum2 = (EditText) findViewById(R.id.number40);
        editQuantity2 = (EditText) findViewById(R.id.number50);
        editPrice2 = (EditText) findViewById(R.id.number60);
        spinnerCategorie2 = (Spinner) findViewById(R.id.spinner2);

        btnCalculer = (Button) findViewById(R.id.button3);
        btnCalculer.setEnabled(false);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategorie.setAdapter(adapter);
        spinnerCategorie.setOnItemSelectedListener(this);

        spinnerCategorie2.setAdapter(adapter);
        spinnerCategorie2.setOnItemSelectedListener(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tva = extras.getString("Taux TVA");
            remise = extras.getString("Remise");

        }

        editNum.addTextChangedListener(calculerTextWatcher);
        editQuantity.addTextChangedListener(calculerTextWatcher);
        editPrice.addTextChangedListener(calculerTextWatcher);

        editNum2.addTextChangedListener(calculerTextWatcher);
        editQuantity2.addTextChangedListener(calculerTextWatcher);
        editPrice2.addTextChangedListener(calculerTextWatcher);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.spinner:
                text = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinner2:
                text2 = parent.getItemAtPosition(position).toString();
                break;                
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    
    
    //this method permit to calculate the result and send it back to the the main activity
    public void calculer(View view){
        result = (Double.valueOf(editQuantity.getText().toString()) * Double.valueOf(editPrice.getText().toString()) + Double.valueOf(editQuantity2.getText().toString()) * Double.valueOf(editPrice2.getText().toString()));
        result -= result * (Double.valueOf(remise)/100);

        resultTva = result + result * (Double.valueOf(tva)/100);

        Intent intent = new Intent(Produits.this, MainActivity.class);
        Bundle extras = new Bundle();
        extras.putString("Result HT", String.valueOf(result));
        extras.putString("Result TVA", String.valueOf(resultTva));
        intent.putExtras(extras);


        setResult(10,intent);
        finish();
    }


    private TextWatcher calculerTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String numInput = editNum.getText().toString().trim();
            String quantityInput = editQuantity.getText().toString().trim();
            String priceInput = editPrice.getText().toString().trim();

            String num2Input = editNum2.getText().toString().trim();
            String quantity2Input = editQuantity2.getText().toString().trim();
            String price2Input = editPrice2.getText().toString().trim();



            btnCalculer.setEnabled(!numInput.isEmpty() && !quantityInput.isEmpty() && !priceInput.isEmpty() && !num2Input.isEmpty() && !quantity2Input.isEmpty() && !price2Input.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}