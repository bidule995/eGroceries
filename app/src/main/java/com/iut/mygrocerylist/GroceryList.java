package com.iut.mygrocerylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GroceryList extends AppCompatActivity {

    ArrayList<HashMap<String, String>> listeArticles;
    final GroceryDatabase db = new GroceryDatabase(this);
    String idListe, nomListe, valeurProgression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        TextView testId = findViewById(R.id.idTest);
        TextView titreListe = findViewById(R.id.listeNom);
        TextView progressValue = findViewById(R.id.listProgressValue);


        // Récupérer l'ID et le titre de la liste
        idListe = this.getIntent().getExtras().getString("ID_LISTE");
        nomListe = this.getIntent().getExtras().getString("NOM_LISTE");
        valeurProgression = this.getIntent().getExtras().getString("PROGRESS_VALUE");

        testId.setText(idListe);
        titreListe.setText(nomListe);
        progressValue.setText(valeurProgression);

        // Titre de l'activité
        getSupportActionBar().setTitle(this.getIntent().getExtras().getString("NOM_LISTE"));

        // Gestion du bouton flottant pour ajouter une liste
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(GroceryList.this, AddArticle.class),1);
            }
        });

        // Affichage des articles
        listeArticles = db.getArticles(idListe);
        ListView lv = findViewById(R.id.listeArticles);
        ListAdapter adapter = new SimpleAdapter(GroceryList.this, listeArticles, R.layout.list_row_articles,
                new String[]{"nom", "recupere"},
                new int[]{R.id.articleLigneNom, R.id.articleRecupereCheckBox});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroceryList.this, GroceryList.class);
                intent.putExtra("ID_ARTICLE", listeArticles.get(position).get(GroceryDatabase.ARTICLE_ID));
                intent.putExtra("NOM_ARTICLE", listeArticles.get(position).get(GroceryDatabase.ARTICLE_NOM));
                startActivity(intent);
            }
        });
       }

    // Affichage et gestion du menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.editAction).setVisible(true);
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

        return super.onOptionsItemSelected(item);
    }

}
