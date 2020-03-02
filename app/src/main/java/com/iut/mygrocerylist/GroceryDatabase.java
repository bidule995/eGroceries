package com.iut.mygrocerylist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import androidx.annotation.Nullable;

public class GroceryDatabase extends SQLiteOpenHelper {

//  Nom des tables et colonnes
    private static final String TABLE_LISTES = "table_listes";
    private static final String LISTE_ID = "ID";
    private static final String LISTE_NOM = "nom";
    private static final String LISTE_DATE = "dateCreation";

    private static final String TABLE_ITEMS = "table_items";
    private static final String ITEM_ID = "ID";
    private static final String ITEM_TITRE = "titre";
    private static final String ITEM_QUANTITE = "quantite";
    private static final String ITEM_RECUPERE = "recupere";
    private static final String ITEM_PRIORITE = "priorite";
    private static final String ITEM_REMARQUES = "remarques";

    public GroceryDatabase(@Nullable Context context, @Nullable String name, @Nullable CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
