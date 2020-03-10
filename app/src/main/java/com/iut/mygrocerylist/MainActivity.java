package com.iut.mygrocerylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> listeListes;
    final GroceryDatabase db = new GroceryDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Affichage du menu
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Gestion du bouton flottant pour ajouter une liste
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, CreateList.class),1);
            }
        });

        // Affichage des listes
        listeListes = db.getListes();
        ListView lv = findViewById(R.id.listeListes);
        ListAdapter adapter = new SimpleAdapter(MainActivity.this, listeListes, R.layout.list_row_listes,
                new String[]{"nom", "progressValue"},
                new int[]{R.id.listeNom, R.id.listProgressValue});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, GroceryList.class);
                intent.putExtra("ID_LISTE", listeListes.get(position).get(GroceryDatabase.LISTE_ID));
                intent.putExtra("NOM_LISTE", listeListes.get(position).get(GroceryDatabase.LISTE_NOM));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        if (requestCode == LIST_CREATED) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
            }
            */
        if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), R.string.cancel_create_list, Toast.LENGTH_SHORT).show();
            recreate();
        }
    }
}
