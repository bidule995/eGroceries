package com.iut.mygrocerylist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class AddArticle extends AppCompatActivity {

    private ArrayList<HashMap<String, String>> listeSuggestions;
    private EditText inputNomArticle;
    private final GroceryDatabase db = new GroceryDatabase(this);
    private String idListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.getTheme(this));
        setContentView(R.layout.activity_add_article);
        inputNomArticle = findViewById(R.id.nameArticle);
        idListe = this.getIntent().getExtras().getString("ID_LISTE");

        // Affichage des suggestions
        listeSuggestions = db.getSuggestions();
        ListView lv = findViewById(R.id.listeSuggestions);
        ListAdapter adapter = new SimpleAdapter(AddArticle.this, listeSuggestions, R.layout.list_row_suggestions,
                new String[]{"nom"},
                new int[]{R.id.suggestionLigneNom});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String suggestion = listeSuggestions.get(position).get(GroceryDatabase.SUGGESTION_NOM);
                String test = db.checkIfArticleExistsInList(suggestion, idListe);
                if (test == null) {
                    db.insertNewArticle(suggestion, idListe);
                } else {
                    db.incrementArticleQuantity(test);
                }
                Intent intent = new Intent(AddArticle.this, GroceryList.class);
                intent.putExtra("ID_LISTE", listeSuggestions.get(position).get(GroceryDatabase.ARTICLE_ID));
                intent.putExtra("SUCCESS_MSG", getString(R.string.article_added));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    public void onClickAjouter(View view) {
        String nomArticle = this.inputNomArticle.getText().toString();
        if(nomArticleCorrect(nomArticle)) {
            String test = db.checkIfArticleExistsInList(nomArticle, idListe);
            if (test == null) {
                db.insertNewArticle(nomArticle, idListe);
            } else {
                db.incrementArticleQuantity(test);
            }
            Intent intent = new Intent();
            intent.putExtra("SUCCESS_MSG", getString(R.string.article_added));
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("CANCEL_MSG", getString(R.string.cancel_add_article));
        returnIntent.putExtra("ID_LISTE", idListe);
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private boolean nomArticleCorrect(String nomArticle) {
        if (nomArticle.isEmpty()) {
            this.inputNomArticle.setError(getResources().getString(R.string.nom_liste_vide));
            this.inputNomArticle.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}
