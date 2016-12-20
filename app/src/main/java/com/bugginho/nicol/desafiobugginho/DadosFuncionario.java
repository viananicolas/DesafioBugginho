package com.bugginho.nicol.desafiobugginho;

import android.content.Intent;
import android.icu.text.NumberFormat;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class DadosFuncionario extends AppCompatActivity {
    public Funcionario funcionario;
    public List<Cargo> cargos;
    public Cargo cargo;
    public Lucro lucro;
    public boolean novoItem=true;
    private int x=0;
    private int numFuncionarios=0;
    EditText editNome;
    EditText editCPF;
    EditText editSalario;
    EditText editBonus;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_funcionario);
        db = new DatabaseHelper(this);
        cargo=new Cargo();
        lucro=new Lucro();
        lucro= db.getLucro();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button buttonSalvar = (Button) findViewById(R.id.buttonSalvar);
        editNome = (EditText) findViewById(R.id.editNome);
        editCPF = (EditText) findViewById(R.id.editCPF);
        editSalario = (EditText) findViewById(R.id.editSalario);
        editBonus = (EditText) findViewById(R.id.editBonus);
        editCPF.addTextChangedListener(Mask.insert(Mask.CPF_MASK,editCPF));
        editSalario.addTextChangedListener(new MoneyTextWatcher(editSalario, new Locale("pt", "BR")));
        editBonus.addTextChangedListener(new MoneyTextWatcher(editBonus, new Locale("pt", "BR")));
        final Spinner lstCargos = (Spinner) findViewById(R.id.spinnerCargo);
        cargos = new ArrayList<Cargo>(db.getCargos());
        funcionario = new Funcionario();
        if (getIntent().getSerializableExtra("Funcionario") != null)
        {
            funcionario = (Funcionario) getIntent().getSerializableExtra("Funcionario");
            if(funcionario.getId()!=null && !funcionario.getId().isEmpty()) {
                editNome.setText(funcionario.getNome());
                editCPF.setText(funcionario.getCpf());
                java.text.NumberFormat f = java.text.NumberFormat.getInstance(Locale.GERMANY);
                if (f instanceof DecimalFormat) {
                    ((DecimalFormat) f).applyPattern("###,###.#");
                }
                editSalario.setText(f.format(funcionario.getSalario()));
                novoItem=false;
            }
            if(novoItem)
                actionBar.setTitle("Novo funcionário");
            else
                actionBar.setTitle("Alterar funcionário");
        }

        List<Map<String,String>> data = new ArrayList<Map<String,String>>();
        for (Cargo item : cargos) {
            Map<String,String> datum = new HashMap<String,String>(2);
            datum.put("nome_cargo", item.getNomeCargo());
            datum.put("id", item.getId());
            data.add(datum);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,
                data,
                android.R.layout.simple_spinner_item,
                new String[]{"nome_cargo"},
                new int[]{android.R.id.text1});
        lstCargos.setAdapter(adapter);
        cargo = getCargoIndex();
        lstCargos.setSelection(cargos.indexOf(cargo),true);
        lstCargos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Double bonus;
                cargo = cargos.get(position);
                numFuncionarios= db.getNumeroFuncionariosCargo(cargo.getId());
                Log.d("Messi ", Integer.toString(numFuncionarios));
                if(numFuncionarios>0){
                    if(novoItem)
                        numFuncionarios++;
                    bonus = cargo.getBonus()/numFuncionarios;
                    bonus*= lucro.getLucroAnual();
                }
                else{
                    bonus=lucro.getLucroAnual()*cargo.getBonus();
                }
                java.text.NumberFormat f = java.text.NumberFormat.getInstance(Locale.GERMANY);
                if (f instanceof DecimalFormat) {
                    ((DecimalFormat) f).applyPattern("###,###.#");
                }
                editBonus.setText(f.format(bonus));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        buttonSalvar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if((editNome.getText().toString()!=null &&!editNome.getText().toString().isEmpty())
                        && (editCPF.getText().toString()!=null &&!editCPF.getText().toString().isEmpty())
                        && (editSalario.getText().toString()!=null &&!editSalario.getText().toString().isEmpty())){
                    if(editCPF.getText().toString().length()<14){
                        Snackbar.make(findViewById(R.id.activity_dados_funcionario),
                                "CPF incompleto",
                                Snackbar.LENGTH_LONG)
                                .show();
                    }
                    else
                    salvarDados();
                }
                else
                    Snackbar.make(findViewById(R.id.activity_dados_funcionario),
                            "Há dados incompletos no formulário",
                            Snackbar.LENGTH_LONG)
                            .show();
            }
        });
        editBonus.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return true;
            }
        });
    }
    public Cargo getCargoIndex(){
        for (Cargo item : cargos) {
            if(item.getId().equals(funcionario.getCargo())){
               return item;
            }
        }
        return null;
    }
    public void salvarDados(){
        funcionario.setNome(editNome.getText().toString());
        funcionario.setCpf(editCPF.getText().toString());
        funcionario.setCargo(cargo.getId());
        funcionario.setSalario(parseDouble(editSalario.getText().toString()
                .replace("R","")
                .replace("$","")
                .replace(".","")
                .replace(",","")));
        if(novoItem){
            funcionario.setId(UUID.randomUUID().toString());
            db.createFuncionario(funcionario);
        }
        else
        {
            db.updateFuncionario(funcionario);
        }
        this.finish();
    }
    double parseDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch(Exception e) {
                return 0;
            }
        }
        else return 0;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
