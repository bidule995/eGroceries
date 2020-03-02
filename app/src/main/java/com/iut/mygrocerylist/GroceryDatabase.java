package com.iut.mygrocerylist;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class GroceryDatabase extends SQLiteOpenHelper {

//  Nom des tables et colonnes
    private static final String TABLE_LISTES = "table_listes";
    private static final String LISTE_ID = "listeID";
    private static final String LISTE_NOM = "nom";
    private static final String LISTE_DATE = "dateCreation";

    private static final String TABLE_ITEMS = "table_items";
    private static final String ITEM_ID = "itemID";
    private static final String ITEM_NOM = "nom";
    private static final String ITEM_QUANTITE = "quantite";
    private static final String ITEM_RECUPERE = "recupere";
    private static final String ITEM_PRIORITE = "priorite";
    private static final String ITEM_REMARQUES = "remarques";

//  Requêtes de création des tables
    private static final String CREATE_TABLE_LISTES = "CREATE TABLE " + TABLE_LISTES + " (" +
            LISTE_ID + " INTEGER PRIMARY KEY, " + LISTE_NOM + " VARCHAR(50), " +
            LISTE_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

    private static final String CREATE_TABLE_ITEMS = "CREATE TABLE " + TABLE_ITEMS + " (" +
            ITEM_ID + " INTEGER PRIMARY KEY, " + ITEM_NOM + " VARCHAR(50), " +
            ITEM_QUANTITE + " SMALLINT, " + ITEM_RECUPERE + " BOOLEAN, " +
            ITEM_PRIORITE + " VARCHAR(13), " + ITEM_REMARQUES + " TEXT, " +
            LISTE_ID + " INTEGER, FOREIGN KEY(" + LISTE_ID + ") REFERENCES " + TABLE_LISTES + "(" +
            LISTE_ID + "));";

    public GroceryDatabase(@Nullable Context context, @Nullable String name, @Nullable CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Création des tables
        sqLiteDatabase.execSQL(CREATE_TABLE_LISTES);
        sqLiteDatabase.execSQL(CREATE_TABLE_ITEMS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    // Insérer une nouvelle liste
    public void insertNewList(String nom) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LISTE_NOM, nom);
        db.insert(TABLE_LISTES, null, values);
        db.close();
    }

    // Supprimer une liste
    public void deleteList(int listeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LISTES, LISTE_ID + " = ?", new String[]{String.valueOf(listeID)});
        db.close();
    }

    // Obtenir la liste des listes de courses
    public ArrayList<HashMap<String, String>> getListes(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> listeListes = new ArrayList<>();
        String query = "SELECT " + LISTE_NOM + ", " + LISTE_DATE + " FROM "+ TABLE_LISTES;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> liste = new HashMap<>();
            liste.put(LISTE_NOM, cursor.getString(cursor.getColumnIndex(LISTE_NOM)));
            liste.put(LISTE_DATE,cursor.getString(cursor.getColumnIndex(LISTE_DATE)));
            listeListes.add(liste);
        }
        return listeListes;
    }
}
