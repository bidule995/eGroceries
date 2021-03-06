package com.iut.mygrocerylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class GroceryDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "grocery_database";

    //  Nom des tables et colonnes
    private static final String TABLE_LISTES = "table_listes";
    protected static final String LISTE_ID = "listeID";
    protected static final String LISTE_NOM = "nom";
    private static final String LISTE_DATE = "dateCreation";

    private static final String TABLE_ARTICLES = "table_items";
    protected static final String ARTICLE_ID = "articleID";
    protected static final String ARTICLE_NOM = "nom";
    protected static final String ARTICLE_QUANTITE = "quantite";
    protected static final String ARTICLE_RECUPERE = "recupere";
    protected static final String ARTICLE_PRIORITE = "priorite";
    protected static final String ARTICLE_REMARQUES = "remarques";

    private static final String TABLE_SUGGESTIONS = "table_suggestions";
    protected static final String SUGGESTION_ID = "suggestionID";
    protected static final String SUGGESTION_NOM = "nom";
    private static final String SUGGESTION_POIDS = "poids";

    protected static final int NB_MAX_SUGGESTIONS = 25;

    //  Requêtes de création des tables
    private static final String CREATE_TABLE_LISTES = "CREATE TABLE " + TABLE_LISTES + " (" +
            LISTE_ID + " INTEGER PRIMARY KEY, " + LISTE_NOM + " VARCHAR(50), " +
            LISTE_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

    private static final String CREATE_TABLE_ARTICLES = "CREATE TABLE " + TABLE_ARTICLES + " (" +
            ARTICLE_ID + " INTEGER PRIMARY KEY, " + ARTICLE_NOM + " VARCHAR(50), " +
            ARTICLE_QUANTITE + " SMALLINT, " + ARTICLE_RECUPERE + " BOOLEAN, " +
            ARTICLE_PRIORITE + " TINYINT, " + ARTICLE_REMARQUES + " TEXT, " +
            LISTE_ID + " INTEGER, FOREIGN KEY(" + LISTE_ID + ") REFERENCES " + TABLE_LISTES + "(" +
            LISTE_ID + "));";

    private static final String CREATE_TABLE_SUGGESTIONS = "CREATE TABLE " + TABLE_SUGGESTIONS + " (" +
            SUGGESTION_ID + " INTEGER PRIMARY KEY, " + SUGGESTION_NOM + " VARCHAR(50), " +
            SUGGESTION_POIDS + " INT);";

    public GroceryDatabase(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Création des tables
        sqLiteDatabase.execSQL(CREATE_TABLE_SUGGESTIONS);
        sqLiteDatabase.execSQL(CREATE_TABLE_ARTICLES);
        sqLiteDatabase.execSQL(CREATE_TABLE_LISTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    // Insérer un nouvel article
    public long insertNewListe(String nom) {
        nom = formatName(nom);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LISTE_NOM, nom);
        long idListe = db.insert(TABLE_LISTES, null, values);
        return idListe;
    }

    // Insérer un nouvel article
    public long insertNewArticle(String nom, String id) {
        nom = formatName(nom);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ARTICLE_NOM, nom); values.put(ARTICLE_PRIORITE, 2);
        values.put(ARTICLE_RECUPERE, 0); values.put(LISTE_ID, id);
        long idArticle = db.insert(TABLE_ARTICLES, null, values);
        ContentValues valuesSuggestions = new ContentValues();
        String s = checkIfSuggestionExists(nom);
        if(s == null){
            valuesSuggestions.put(SUGGESTION_NOM, nom); valuesSuggestions.put(SUGGESTION_POIDS, 1);
            db.insert(TABLE_SUGGESTIONS, null, valuesSuggestions);
        } else {
            Cursor c = db.rawQuery("UPDATE " + TABLE_SUGGESTIONS + " SET " + SUGGESTION_POIDS + " = " +
                    SUGGESTION_POIDS + " + 1 WHERE " + SUGGESTION_ID + " = " + s, null);
            c.moveToFirst();
            c.close();
        }
        return idArticle;
    }

    // Modifier une liste
    public void modifyListe(String id, String nom) {
        nom = formatName(nom);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("UPDATE " + TABLE_LISTES + " SET " + LISTE_NOM + " = '" + nom +
                "' WHERE " + LISTE_ID + " = " + id, null);
        c.moveToFirst();
        c.close();
    }

    // Modifier un article
    public void modifyArticle(String id, String nom, String quantite, String priorite, String remarques, String recupere) {
        nom = formatName(nom);
        SQLiteDatabase db = this.getWritableDatabase();
        if(quantite.equals("")) quantite = "null";
        Cursor c = db.rawQuery("UPDATE " + TABLE_ARTICLES + " SET " + ARTICLE_NOM + " = '" + nom + "', "
                + ARTICLE_QUANTITE + " = " + quantite + ", " + ARTICLE_PRIORITE + " = " + priorite + ", "
                + ARTICLE_REMARQUES + " = '" + remarques + "', " + ARTICLE_RECUPERE + " = " + recupere +
                " WHERE " + ARTICLE_ID + " = " + id, null);
        c.moveToFirst();
        c.close();
        ContentValues valuesSuggestions = new ContentValues();
        String s = checkIfSuggestionExists(nom);
        if(s == null) {
            valuesSuggestions.put(SUGGESTION_NOM, nom);
            valuesSuggestions.put(SUGGESTION_POIDS, 1);
            db.insert(TABLE_SUGGESTIONS, null, valuesSuggestions);
        }
    }

    // Supprimer une liste
    public void deleteListe(String listeID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LISTES, LISTE_ID + " = ?", new String[]{String.valueOf(listeID)});
        db.delete(TABLE_ARTICLES, LISTE_ID + " = ?", new String[]{String.valueOf(listeID)});
    }

    // Supprimer un article
    public void deleteArticle(String articleID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ARTICLES, ARTICLE_ID + " = ?", new String[]{String.valueOf(articleID)});
    }

    // Obtenir la liste des listes de courses
    public ArrayList<HashMap<String, String>> getListes() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> listeListes = new ArrayList<>();
        String query = "SELECT " + LISTE_NOM + ", " + LISTE_ID + " FROM " + TABLE_LISTES +
                " ORDER BY " + LISTE_DATE + " DESC";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> liste = new HashMap<>();
            liste.put(LISTE_ID, cursor.getString(cursor.getColumnIndex(LISTE_ID)));
            liste.put(LISTE_NOM, cursor.getString(cursor.getColumnIndex(LISTE_NOM)));
            liste.put("progressValue", getRecuperes(cursor.getString(cursor.getColumnIndex(LISTE_ID))));
            liste.put("progressValueBar", getValeurProgression(cursor.getString(cursor.getColumnIndex(LISTE_ID)))+"");
            listeListes.add(liste);
        }
        cursor.close();
        return listeListes;
    }

    // Obtenir la liste des articles d'une liste de courses
    public ArrayList<HashMap<String, String>> getArticles(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> listeArticles = new ArrayList<>();
        String query = "SELECT " + ARTICLE_NOM + ", " + ARTICLE_QUANTITE + ", " + ARTICLE_PRIORITE +
                ", " + ARTICLE_RECUPERE + ", " + ARTICLE_REMARQUES + ", " + ARTICLE_ID + " FROM " +
                TABLE_ARTICLES + " WHERE " + LISTE_ID + " = " + id + " ORDER BY " + ARTICLE_PRIORITE
                + ", " + ARTICLE_NOM;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> liste = new HashMap<>();
            liste.put(ARTICLE_NOM, cursor.getString(cursor.getColumnIndex(ARTICLE_NOM)));
            String quantite = cursor.getString(cursor.getColumnIndex(ARTICLE_QUANTITE));
            if(quantite == null){
                liste.put(ARTICLE_QUANTITE, "1");
            } else {
                liste.put(ARTICLE_QUANTITE, quantite);

            }
            liste.put(ARTICLE_PRIORITE, cursor.getString(cursor.getColumnIndex(ARTICLE_PRIORITE)));
                 //  liste.put(ARTICLE_RECUPERE, "1");
            liste.put(ARTICLE_RECUPERE, cursor.getString(cursor.getColumnIndex(ARTICLE_RECUPERE)));
            liste.put(ARTICLE_REMARQUES, cursor.getString(cursor.getColumnIndex(ARTICLE_REMARQUES)));
            liste.put(ARTICLE_ID, cursor.getString(cursor.getColumnIndex(ARTICLE_ID)));
            listeArticles.add(liste);
        }
        cursor.close();
        return listeArticles;
    }

    // Obtenir la liste des suggestions (NB_MAX_SUGGESTIONS premiers articles les plus ajoutés aux listes)
    public ArrayList<HashMap<String, String>> getSuggestions() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> listeSuggestions = new ArrayList<>();
        String query = "SELECT " + SUGGESTION_NOM + ", " + SUGGESTION_ID + " FROM " + TABLE_SUGGESTIONS +
                " ORDER BY " + SUGGESTION_POIDS + " DESC LIMIT " + NB_MAX_SUGGESTIONS;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> liste = new HashMap<>();
            liste.put(SUGGESTION_ID, cursor.getString(cursor.getColumnIndex(SUGGESTION_ID)));
            liste.put(SUGGESTION_NOM, cursor.getString(cursor.getColumnIndex(SUGGESTION_NOM)));
            listeSuggestions.add(liste);
        }
        cursor.close();
        return listeSuggestions;
    }

    // Obtenir un article à partir de son id
    public Article getArticle(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Article article = new Article(id);
        String query = "SELECT " + ARTICLE_NOM + ", " + ARTICLE_QUANTITE + ", " + ARTICLE_PRIORITE +
                ", " + ARTICLE_RECUPERE + ", " + ARTICLE_REMARQUES + " FROM " + TABLE_ARTICLES +
                " WHERE " + ARTICLE_ID + " = " + id;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            article.setNom(cursor.getString(cursor.getColumnIndex(ARTICLE_NOM)));
            article.setQuantite(cursor.getString(cursor.getColumnIndex(ARTICLE_QUANTITE)));
            article.setPriorite(cursor.getString(cursor.getColumnIndex(ARTICLE_PRIORITE)));
            article.setRecupere(cursor.getString(cursor.getColumnIndex(ARTICLE_RECUPERE)));
            article.setRemarques(cursor.getString(cursor.getColumnIndex(ARTICLE_REMARQUES)));
        }
        cursor.close();
        return article;
    }

    // Obtenir une valeur de la base de données
    public String getValueFromDb(String tableName, String select, String selectBy, String selectName) {
        String value = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(tableName, new String[] {select}, selectBy + "=?", new String[]{selectName}, null, null, null);
        if(c.getCount() == 1){
            c.moveToFirst();
            value = c.getString(0);
        }
        c.close();
        return value;
    }

    // Obtenir une valeur de la base de données depuis une requête SQL
    public String getValueFromQuery(String query) {
        String value = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        if(c.getCount() == 1){
            c.moveToFirst();
            value = c.getString(0);
        }
        c.close();
        return value;
    }

    // Obtenir le nom d'une liste depuis son id
    public String getNomListe(String idListe) {
        return getValueFromDb(TABLE_LISTES, LISTE_NOM, LISTE_ID, idListe);
    }

    // Obtenir le nombre d'articles dans une liste depuis son id
    public String getNbArticles(String idListe) {
        return getValueFromQuery("SELECT COUNT(*) FROM " + TABLE_ARTICLES + " WHERE " + LISTE_ID +
                " = " + idListe);
    }

    // Obtenir le nombre d'articles récupérés dans une liste depuis son id
    public String getNbArticlesRecuperes(String idListe) {
        return getValueFromQuery("SELECT COUNT(*) FROM " + TABLE_ARTICLES + " WHERE " + LISTE_ID +
                " = " + idListe + " AND " + ARTICLE_RECUPERE + " = 1");
    }

    // Obtenir le nombre d'articles récupérés sur le nombre d'articles d'une liste depuis son id
    public String getRecuperes(String idListe) {
        return getNbArticlesRecuperes(idListe) + "/" + getNbArticles(idListe);
    }

    public int getValeurProgression(String idListe) {
        return (int)((Float.parseFloat(getNbArticlesRecuperes(idListe)) / Float.parseFloat(getNbArticles(idListe))) * 100);
    }

    public static String getValueFromPriorite(String priorite, Context context) {
        if (priorite.equals(context.getString(R.string.priority_low))){
            return "3";
        } else if (priorite.equals(context.getString(R.string.priority_essential))) {
            return "1";
        } else {
            return "2";
        }
    }

    // Renvoie id si existe, null sinon
    public String checkIfSuggestionExists(String name) {
        formatName(name);
        String test = getValueFromQuery("SELECT COUNT(*) FROM " + TABLE_SUGGESTIONS + " WHERE "
                + SUGGESTION_NOM + " = \"" + name + "\" GROUP BY " + SUGGESTION_ID);
        if(test != null){
            return getValueFromQuery("SELECT " + SUGGESTION_ID + " FROM " + TABLE_SUGGESTIONS + " WHERE "
                    + SUGGESTION_NOM  + " = \"" + name + "\"");
        } else {
            return null;
        }
    }

    // Renvoie id si existe, null sinon
    public String checkIfArticleExistsInList(String name, String listID) {
        formatName(name);
        String test = getValueFromQuery("SELECT COUNT(*) FROM " + TABLE_ARTICLES + " WHERE "
                + ARTICLE_NOM + " = \"" + name + "\" AND " + LISTE_ID + " = " + listID + " GROUP BY " + ARTICLE_ID);
        if(test != null){
            return getValueFromQuery("SELECT " + ARTICLE_ID + " FROM " + TABLE_ARTICLES + " WHERE "
                    + ARTICLE_NOM  + " = \"" + name + "\"  AND " + LISTE_ID + " = " + listID);
        } else {
            return null;
        }
    }

    public String formatName(String name) {
        name = name.replaceAll("[\"]","");
        name = name.toLowerCase();
        String s = name.substring(0, 1).toUpperCase();
        name = s + name.substring(1);
        return name;
    }

    public void incrementArticleQuantity(String idArticle) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("UPDATE " + TABLE_ARTICLES + " SET " + ARTICLE_QUANTITE + " = "
                + ARTICLE_QUANTITE + " + 1  WHERE " + ARTICLE_ID + " = "
                + idArticle, null);
        c.moveToFirst();
        c.close();
    }

    public void setRecupere(String idArticle, String recupere) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("UPDATE " + TABLE_ARTICLES + " SET " + ARTICLE_RECUPERE + " = "
                + recupere + " WHERE " + ARTICLE_ID + " = " +
                idArticle, null);
        c.moveToFirst();
        c.close();
    }

    public void resetSuggestions() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("DELETE FROM " + TABLE_SUGGESTIONS, null);
        c.moveToFirst();
        c.close();
    }
}


