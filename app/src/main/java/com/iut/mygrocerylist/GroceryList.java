package com.iut.mygrocerylist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class GroceryList extends AppCompatActivity {

    private ListView lv;
    private ArrayList<HashMap<String, String>> listeArticles;
    private final GroceryDatabase db = new GroceryDatabase(this);
    private String idListe, nomListe, valeurProgression;
    private TextView progressValue;
    private ProgressBar listProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        progressValue = findViewById(R.id.listProgressValue);
        listProgressBar = findViewById(R.id.listProgressBar);

        // Récupérer l'ID et le titre de la liste
        idListe = this.getIntent().getExtras().getString("ID_LISTE");
        nomListe = db.getNomListe(idListe);

        // Valeur et barre de progression
        valeurProgression = db.getRecuperes(idListe);
        progressValue.setText(valeurProgression);
        int ValeurProgression = db.getValeurProgression(idListe);
        listProgressBar.setProgress(ValeurProgression);
        checkProgressFull(ValeurProgression);

        // Gestion du bouton flottant pour ajouter un article
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroceryList.this, AddArticle.class);
                intent.putExtra("ID_LISTE", idListe);
                startActivityForResult(intent ,1);
            }
        });

        // Affichage des articles
        listeArticles = db.getArticles(idListe);
        lv = findViewById(R.id.listeArticles);
        ArticlesAdapter adapter = new ArticlesAdapter(GroceryList.this, listeArticles, R.layout.list_row_articles,
                new String[]{"nom", "quantite", "remarques", "recupere", "id"},
                new int[]{R.id.articleLigneNom, R.id.articleQuantite, R.id.articleRemarques});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroceryList.this, EditArticle.class);
                intent.putExtra("ARTICLE_NOM", listeArticles.get(position).get(GroceryDatabase.ARTICLE_NOM));
                intent.putExtra("ARTICLE_QUANTITE", listeArticles.get(position).get(GroceryDatabase.ARTICLE_QUANTITE));
                intent.putExtra("ARTICLE_PRIORITE", listeArticles.get(position).get(GroceryDatabase.ARTICLE_PRIORITE));
                intent.putExtra("ARTICLE_RECUPERE", listeArticles.get(position).get(GroceryDatabase.ARTICLE_RECUPERE));
                intent.putExtra("ARTICLE_REMARQUES", listeArticles.get(position).get(GroceryDatabase.ARTICLE_REMARQUES));
                intent.putExtra("ID_ARTICLE", listeArticles.get(position).get(GroceryDatabase.ARTICLE_ID));
                startActivityForResult(intent, 1);
            }
        });
       }

       public void onClickArticleRecupereCheckBox(View v) {
           CheckBox cb = (CheckBox) v;
           int position = Integer.parseInt(cb.getTag().toString());
           String idListe = listeArticles.get(position).get(GroceryDatabase.ARTICLE_ID);

           if (cb.isChecked()) {
               db.setRecupere(idListe, "1");
           } else {
               db.setRecupere(idListe, "0");
           }
           int progressValue = db.getValeurProgression(this.idListe);
           this.listProgressBar.setProgress(progressValue);
           valeurProgression = db.getRecuperes(this.idListe);
           this.progressValue.setText(valeurProgression);
           checkProgressFull(progressValue);
       }

       public void checkProgressFull(int progressValue){
            TextView tx = findViewById(R.id.listProgressValue);
            if(progressValue == 100) tx.setTextColor(ContextCompat.getColor(GroceryList.this, R.color.colorAccent));
            else tx.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
       }

    // Affichage et gestion du menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.editAction).setVisible(true);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(nomListe);
        actionbar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        setResult(Activity.RESULT_FIRST_USER, new Intent());
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.editAction) {
            Intent intent = new Intent(this, EditGroceryList.class);
            intent.putExtra("ID_LISTE", idListe);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Gestion du résultat envoyé par une autre activité
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        recreate();

        if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), data.getStringExtra("CANCEL_MSG"), Toast.LENGTH_SHORT).show();
        }

        if (resultCode == Activity.RESULT_OK) {
            Toast.makeText(getApplicationContext(), data.getStringExtra("SUCCESS_MSG"), Toast.LENGTH_SHORT).show();
            ProgressBar listProgressBar = findViewById(R.id.listProgressBar);
            listProgressBar.setProgress(db.getValeurProgression(idListe));
        }
    }
}
