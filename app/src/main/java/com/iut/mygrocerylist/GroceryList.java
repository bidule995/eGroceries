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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GroceryList extends AppCompatActivity {

    ArrayList<HashMap<String, String>> listeArticles;
    String idListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        TextView testId = findViewById(R.id.idTest);

        // Récupérer l'ID de la liste
        idListe = this.getIntent().getExtras().getString("ID_LISTE");
        testId.setText(idListe);

        // Titre de l'activité
        getSupportActionBar().setTitle(this.getIntent().getExtras().getString("NOM_LISTE"));

        // Affichage des articles
        GroceryDatabase db = new GroceryDatabase(this);
        listeArticles = db.getArticles(idListe);
        ListView lv = findViewById(R.id.listeListes);
        ListAdapter adapter = new SimpleAdapter(GroceryList.this, listeArticles, R.layout.list_row_articles,
                new String[]{"nom"},
                new int[]{R.id.articleLigneNom});
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
