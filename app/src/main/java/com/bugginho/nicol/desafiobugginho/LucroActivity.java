package com.bugginho.nicol.desafiobugginho;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public class LucroActivity extends AppCompatActivity {
    DatabaseHelper db;
    Lucro lucro;
    EditText editLucro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucro);
        db = new DatabaseHelper(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Alterar lucro");
        lucro = db.getLucro();
        Log.d("Vasco", lucro.getLucroAnual().toString());
        editLucro = (EditText) findViewById(R.id.editLucro);
        NumberFormat f = NumberFormat.getInstance(Locale.GERMANY);
        if (f instanceof DecimalFormat) {
            ((DecimalFormat) f).applyPattern("###,###.#");
        }
        editLucro.addTextChangedListener(new MoneyTextWatcher(editLucro, new Locale("pt", "BR")));
        editLucro.setText(f.format(lucro.getLucroAnual()));
        Button buttonSalvar = (Button) findViewById(R.id.buttonSalvarLucro);
        buttonSalvar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if((editLucro.getText().toString()!=null &&!editLucro.getText().toString().isEmpty()))
                    salvarDados();
                else
                    Snackbar.make(findViewById(R.id.activity_dados_funcionario),
                            "Há dados incompletos no formulário",
                            Snackbar.LENGTH_LONG)
                            .show();
            }
        });
    }
    public void salvarDados(){
            lucro.setLucroAnual(parseDouble(editLucro.getText().toString()
                    .replace("R","")
                    .replace("$","")
                    .replace(".","")
                    .replace(",","")));
        Log.d("Flamengo", lucro.getLucroAnual().toString());
            db.updateLucro(lucro);
            this.finish();
    }
    double parseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch(Exception e) {
                return 0;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        }
        else return 0;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
