package com.iut.mygrocerylist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateList extends AppCompatActivity {

    EditText nomListe;
    Button ajouterListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        this.nomListe = findViewById(R.id.nomListe);
        this.ajouterListe = findViewById(R.id.ajouterListe);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private boolean nomListeCorrect(String nomListe) {
        if (nomListe.isEmpty()) {
            this.nomListe.setError(getResources().getString(R.string.nom_liste_vide));
            this.nomListe.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    // Lors du clic sur le bouton Ajouter
    public void onClickAjouter(View view) {
        String nomListe = this.nomListe.getText().toString();
        GroceryDatabase db = new GroceryDatabase(CreateList.this);
        if(nomListeCorrect(nomListe)) db.insertNewListe(nomListe);
    }


}
