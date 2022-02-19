package com.example.tp_spinner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.mbms.StreamingServiceInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText editFacture, editDate, editHT, editTVA, editRemise, editResult;
    Button btnCalculer, btnErase;
    public String tvaInput, remiseInput;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editFacture = (EditText) findViewById(R.id.number);
        editDate = (EditText) findViewById(R.id.number6);
        editDate.setText("DD/MM/YYYY");
        editHT = (EditText) findViewById(R.id.number2);
        editTVA = (EditText) findViewById(R.id.number3);
        editRemise = (EditText) findViewById(R.id.number4);
        editResult = (EditText) findViewById(R.id.number5);
        btnCalculer = (Button) findViewById(R.id.button);
        btnErase = (Button) findViewById(R.id.button2);



        editHT.setEnabled(false);
        editResult.setEnabled(false);
        btnCalculer.setEnabled(false);

        editFacture.addTextChangedListener(calculerTextWatcher);
        editDate.addTextChangedListener(calculerTextWatcher);
        editTVA.addTextChangedListener(calculerTextWatcher);
        editRemise.addTextChangedListener(calculerTextWatcher);






        editDate.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);

                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editDate.setText(current);
                    editDate.setSelection(sel < current.length() ? sel : current.length());



                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });




    }

    private TextWatcher calculerTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String quantityInput = editFacture.getText().toString().trim();
            String dateInput = editDate.getText().toString().trim();
            tvaInput = editTVA.getText().toString().trim();
            remiseInput = editRemise.getText().toString().trim();

            btnCalculer.setEnabled(!quantityInput.isEmpty() && !dateInput.isEmpty() && !tvaInput.isEmpty() && !remiseInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

            if(editDate.getText().toString().equals(editable.toString())) {
                Log.i("TAG", String.valueOf(editable.length()));
                if(editable.length() == 2){
                    Log.i("TAG", "hello");
                    editable.append('-');
                }
            }
        }
    };


    public void erase(View view){
        editFacture.setText("");
        editDate.setText("");
        editHT.setText("");
        editTVA.setText("");
        editRemise.setText("");
        editResult.setText("");
    }

    public void calculer(View view){
        Intent intent = new Intent(this, Produits.class);
        intent.putExtra("Taux TVA", tvaInput);
        intent.putExtra("Remise", remiseInput);
        activityLauncher.launch(intent);
    }


    //send data to the next activity and get a result back from it
    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 10) {

                        Bundle intentExtras = result.getData().getExtras();

                        if(intentExtras != null) {
                            String resultHT = intentExtras.getString("Result HT");
                            String resultTVA = intentExtras.getString("Result TVA");

                            DecimalFormat df = new DecimalFormat("0.00 €");
                            editHT.setText(df.format(Double.valueOf(resultHT)));
                            editResult.setText(df.format(Double.valueOf(resultTVA)));
                        }
                    }
                }
            });

}