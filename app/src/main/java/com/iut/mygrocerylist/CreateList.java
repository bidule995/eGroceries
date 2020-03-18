package com.iut.mygrocerylist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateList extends AppCompatActivity {

    private EditText nomListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.getTheme(this));
        setContentView(R.layout.activity_create_list);
        this.nomListe = findViewById(R.id.nomListe);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("CANCEL_MSG", getResources().getString(R.string.cancel_create_list));
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
        String id;
        String nomListe = this.nomListe.getText().toString();
        GroceryDatabase db = new GroceryDatabase(CreateList.this);
        if(nomListeCorrect(nomListe)) {
            id = db.insertNewListe(nomListe) + "";
            Intent intent = new Intent(CreateList.this, MainActivity.class);
            intent.putExtra("ID_LISTE", id);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}
