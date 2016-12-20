package com.bugginho.nicol.desafiobugginho;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FuncionarioActivity extends AppCompatActivity {
    private List<Funcionario> funcionarios;
    DatabaseHelper db;
    ListView lstFuncionarios;
    String mSearchTerm;
    SimpleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcionario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DatabaseHelper(this);
        buscaFuncionarios();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verFuncionario(new Funcionario());
            }
        });
        lstFuncionarios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long arg3) {
                AlertDialog dialog = AskOption(funcionarios.get(position));
                dialog.show();
                return true;
            }

        });
        lstFuncionarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                verFuncionario(funcionarios.get(position));
            }
        });

    }

    public void verFuncionario(Funcionario funcionario){
        Intent intent = new Intent(this, DadosFuncionario.class);
        intent.putExtra("Funcionario", funcionario);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_funcionario, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_TEXT);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String newFilter =!TextUtils.isEmpty(newText) ? newText : null;
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, LucroActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    public void buscaFuncionarios(){
        funcionarios = new ArrayList<Funcionario>(db.getFuncionarios());
        List<Map<String,String>> data = new ArrayList<Map<String,String>>();
        for (Funcionario item : funcionarios) {
            Log.d("Dios mio", item.getNome());
            Map<String,String> datum = new HashMap<String,String>(2);
            datum.put("nome", item.getNome());
            datum.put("cpf", item.getCpf());
            data.add(datum);
        }
        lstFuncionarios = (ListView) findViewById(R.id.lstFuncionarios);
            adapter = new SimpleAdapter(this,
                    data,
                    android.R.layout.simple_list_item_2,
                    new String[]{"nome", "cpf"},
                    new int[]{android.R.id.text1,
                            android.R.id.text2});
            lstFuncionarios.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        buscaFuncionarios();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
    private AlertDialog AskOption(final Funcionario funcionario)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                .setTitle("Excluir funcionário")
                .setMessage("Tem certeza de que deseja excluir este funcionário?")
                .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.deleteFuncionario(funcionario);
                        funcionarios.clear();
                        buscaFuncionarios();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }
}
