package com.bugginho.nicol.desafiobugginho;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by nicol on 18/12/2016.
 */
/*http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
* http://www.androidhive.info/2013/09/android-sqlite-database-with-multiple-tables/*/

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "funcionariosManager";

    private static final String TABLE_FUNCIONARIO = "funcionarios";
    private static final String TABLE_CARGO = "cargos";
    private static final String TABLE_LUCRO = "todo_tags";

    private static final String KEY_ID = "id";

    private static final String KEY_NOME = "nome";
    private static final String KEY_CARGO = "cargo";
    private static final String KEY_CPF = "cpf";
    private static final String KEY_SALARIO = "salario";

    private static final String KEY_NOME_CARGO = "nome_cargo";
    private static final String KEY_BONUS = "bonus";

    private static final String KEY_LUCRO_ANUAL = "lucro_anual";


    private static final String CREATE_TABLE_FUNCIONARIO = "CREATE TABLE "
            + TABLE_FUNCIONARIO + "(" + KEY_ID + " TEXT PRIMARY KEY," + KEY_NOME
            + " TEXT," + KEY_CARGO + " TEXT," + KEY_CPF
            + " TEXT," + KEY_SALARIO + " REAL " + ")";

    private static final String CREATE_TABLE_CARGO = "CREATE TABLE " + TABLE_CARGO
            + "(" + KEY_ID + " TEXT PRIMARY KEY," + KEY_NOME_CARGO + " TEXT, "
            + KEY_BONUS + " REAL" + ")";

    private static final String CREATE_TABLE_LUCRO = "CREATE TABLE " + TABLE_LUCRO
            + "(" + KEY_ID + " TEXT PRIMARY KEY," + KEY_LUCRO_ANUAL + " REAL" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_CARGO);
        db.execSQL(CREATE_TABLE_FUNCIONARIO);
        db.execSQL(CREATE_TABLE_LUCRO);

            ContentValues valorLucro = new ContentValues();
        valorLucro.put(KEY_ID, UUID.randomUUID().toString());
        valorLucro.put(KEY_LUCRO_ANUAL, 100.0);
            db.insert(TABLE_LUCRO, null, valorLucro);

        List<Cargo> cargos = new ArrayList<Cargo>(defineCargos());
        for (Cargo cargo : cargos) {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, cargo.getId());
            values.put(KEY_NOME_CARGO, cargo.getNomeCargo());
            values.put(KEY_BONUS, cargo.getBonus());
            db.insert(TABLE_CARGO, null, values);
        }
    }

    private List<Cargo> defineCargos(){
        List<Cargo> cargos = new ArrayList<Cargo>();
        cargos.add(new Cargo(UUID.randomUUID().toString(), "Programador", 0.015));
        cargos.add(new Cargo(UUID.randomUUID().toString(), "Designer", 0.015));
        cargos.add(new Cargo(UUID.randomUUID().toString(), "Gerente", 0.03));
        cargos.add(new Cargo(UUID.randomUUID().toString(), "Atendimento", 0.01));

        return cargos;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_FUNCIONARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LUCRO);*/
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARGO);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_FUNCIONARIO_CARGO);
        onCreate(db);
    }
    public void createFuncionario(Funcionario funcionario){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, funcionario.getId());
        values.put(KEY_NOME, funcionario.getNome());
        values.put(KEY_CPF, funcionario.getCpf());
        values.put(KEY_CARGO, funcionario.getCargo());
        values.put(KEY_SALARIO, funcionario.getSalario());
        db.insert(TABLE_FUNCIONARIO, null, values);
    }

    public void updateFuncionario(Funcionario funcionario){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, funcionario.getId());
        values.put(KEY_NOME, funcionario.getNome());
        values.put(KEY_CPF, funcionario.getCpf());
        values.put(KEY_CARGO, funcionario.getCargo());
        values.put(KEY_SALARIO, funcionario.getSalario());
        db.update(TABLE_FUNCIONARIO, values, KEY_ID + " = ?", new String[]{funcionario.getId()});
    }

    public void deleteFuncionario(Funcionario funcionario){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FUNCIONARIO, KEY_ID + " = ?", new String[]{funcionario.getId()});
    }
    public Funcionario getFuncionario(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FUNCIONARIO, new String[] { KEY_ID,
                        KEY_NOME, KEY_CPF, KEY_SALARIO, KEY_CARGO, }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
            Funcionario funcionario = new Funcionario(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getDouble(4));
            return funcionario;
        }
        cursor.close();
        return null;
    }
    public Cargo getCargo(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CARGO, new String[] { KEY_ID,
                        KEY_NOME_CARGO, KEY_BONUS, }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
            Cargo cargo = new Cargo(cursor.getString(0), cursor.getString(1),
                    cursor.getDouble(2));
            return cargo;
        }
        cursor.close();
        return null;
    }
    public Lucro getLucro(){
        SQLiteDatabase db = this.getReadableDatabase();
        Lucro lucro = new Lucro();
        String selectQuery = "SELECT  * FROM " + TABLE_LUCRO;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                lucro.setId(c.getString(0));
                lucro.setLucroAnual(c.getDouble(1));
            } while (c.moveToNext());
        }
        c.close();
        return lucro;
    }
    public List<Funcionario> getFuncionarios(){
        List<Funcionario> funcionarios = new ArrayList<Funcionario>();
        String selectQuery = "SELECT  * FROM " + TABLE_FUNCIONARIO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do{
                Funcionario funcionario = new Funcionario();
                funcionario.setId(c.getString(0));
                funcionario.setNome(c.getString(1));
                funcionario.setCargo(c.getString(2));
                funcionario.setCpf(c.getString(3));
                funcionario.setSalario(c.getDouble(4));
                funcionarios.add(funcionario);
            } while(c.moveToNext());
        }
        c.close();
        return funcionarios;
    }
    public int getNumeroFuncionariosCargo(String cargo){
        String selectQuery = "SELECT  * FROM " + TABLE_FUNCIONARIO;
        int x=0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do{
                if(c.getString(2).equals(cargo))
                {
                    x++;
                }

            } while(c.moveToNext());
        }
        c.close();
        return x;
    }
    public List<Cargo> getCargos(){
        List<Cargo> cargos = new ArrayList<Cargo>();
        String selectQuery = "SELECT  * FROM " + TABLE_CARGO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()){
            do{
                Cargo cargo = new Cargo();
                cargo.setId(c.getString(c.getColumnIndex(KEY_ID)));
                cargo.setNomeCargo(c.getString(c.getColumnIndex(KEY_NOME_CARGO)));
                cargo.setBonus(c.getDouble(c.getColumnIndex(KEY_BONUS)));
                cargos.add(cargo);
            } while(c.moveToNext());
        }
        c.close();
        return cargos;
    }

    public void createLucro(Lucro lucro){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, lucro.getId());
        values.put(KEY_LUCRO_ANUAL, lucro.getLucroAnual());
        db.insert(TABLE_LUCRO, null, values);
    }
    public void updateLucro(Lucro lucro){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, lucro.getId());
        values.put(KEY_LUCRO_ANUAL, lucro.getLucroAnual());
        db.update(TABLE_LUCRO, values, KEY_ID + " = ?", new String[]{lucro.getId()});
    }
    public void deleteLucro(Lucro lucro){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FUNCIONARIO, KEY_ID + " = ?", new String[]{lucro.getId()});
    }
    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db !=null && db.isOpen())
            db.close();
    }
}
