package com.iut.mygrocerylist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class GroceryList extends AppCompatActivity {

    ArrayList<HashMap<String, String>> listeArticles;
    final GroceryDatabase db = new GroceryDatabase(this);
    String idListe, nomListe, valeurProgression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);

        TextView progressValue = findViewById(R.id.listProgressValue);

        // Récupérer l'ID et le titre de la liste
        idListe = this.getIntent().getExtras().getString("ID_LISTE");
        nomListe = db.getNomListe(idListe);
        valeurProgression = db.getRecuperes(idListe);
        progressValue.setText(valeurProgression);

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
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(nomListe);
        actionbar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
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

        if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), data.getStringExtra("CANCEL_MSG"), Toast.LENGTH_SHORT).show();
        }
        if (resultCode == Activity.RESULT_OK) {
            recreate();
        }
    }
}
