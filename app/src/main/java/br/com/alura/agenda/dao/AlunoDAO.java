package br.com.alura.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.sql.SQLInput;
import java.util.ArrayList;
import java.util.List;

import br.com.alura.agenda.modelo.Aluno;

public class AlunoDAO extends SQLiteOpenHelper {

    public AlunoDAO(Context context) {
        super(context, "Agenda", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Alunos (ID INTEGER PRIMARY KEY, NOME TEXT NOT NULL, ENDERECO TEXT, TELEFONE TEXT, EMAIL TEXT, NOTA REAL, CAMINHOFOTO TEXT);";
        db.execSQL(sql);
    }

    /*@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Alunos";
        db.execSQL(sql);
        onCreate(db);
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";
        switch (oldVersion) {
            case 2:
                sql = "ALTER TABLE Alunos ADD COLUMN CAMINHOFOTO TEXT";
                db.execSQL(sql);
        }
    }

    public void insere(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = pegaDadosDoAluno(aluno);
        db.insert("Alunos", null, dados);
    }

    @NonNull
    private ContentValues pegaDadosDoAluno(Aluno aluno) {
        ContentValues dados = new ContentValues();
        dados.put("NOME", aluno.getNome());
        dados.put("ENDERECO", aluno.getEndereco());
        dados.put("TELEFONE", aluno.getTelefone());
        dados.put("EMAIL", aluno.getSite());
        dados.put("NOTA", aluno.getNota());
        dados.put("CAMINHOFOTO", aluno.getCaminhoFoto());
        return dados;
    }

    public List<Aluno> buscaAlunos() {
        String sql = "SELECT * FROM Alunos;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<Aluno> alunos = new ArrayList<Aluno>();
        while (c.moveToNext()){
            Aluno aluno = new Aluno();
            aluno.setId(c.getLong(c.getColumnIndex("ID")));
            aluno.setNome(c.getString(c.getColumnIndex("NOME")));
            aluno.setEndereco(c.getString(c.getColumnIndex("ENDERECO")));
            aluno.setTelefone(c.getString(c.getColumnIndex("TELEFONE")));
            aluno.setSite(c.getString(c.getColumnIndex("EMAIL")));
            aluno.setNota(c.getDouble(c.getColumnIndex("NOTA")));
            aluno.setCaminhoFoto(c.getString(c.getColumnIndex("CAMINHOFOTO")));
            alunos.add(aluno);
        }
        c.close();
        return alunos;
    }

    public void deleta(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {aluno.getId().toString()};
        db.delete("Alunos", "ID = ?", params);
    }

    public void altera(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = pegaDadosDoAluno(aluno);
        String[] params = {aluno.getId().toString()};
        db.update("Alunos", dados, "id = ?", params);
    }

    public boolean ehAluno(String telefone) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Alunos WHERE telefone = ?", new String[]{telefone});
        int resultado =  c.getCount();
        c.close();
        return resultado > 0;
    }
}
