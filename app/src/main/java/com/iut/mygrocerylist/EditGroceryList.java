package com.iut.mygrocerylist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EditGroceryList extends AppCompatActivity {

    private String idListe;
    private TextView txNomListe;
    private final GroceryDatabase db = new GroceryDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.getTheme(this));
        setContentView(R.layout.activity_edit_grocery_list);

        this.idListe = this.getIntent().getExtras().getString("ID_LISTE");
        this.txNomListe = findViewById(R.id.nomModifierListe);
        this.txNomListe.setText(db.getNomListe(idListe));
    }

    // Affichage et gestion du menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("CANCEL_MSG", getResources().getString(R.string.cancel_edit_liste));
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }


    public void onClickSupprimer(View view) {
        final AlertDialog.Builder confirmDeleteArticle = new AlertDialog.Builder(this);
        confirmDeleteArticle.setMessage(getString(R.string.confirm_delete) + " " + db.getNomListe(idListe) + getString(R.string.q_mark));
        confirmDeleteArticle.setPositiveButton(R.string.yes_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogI, int which) {
                db.deleteListe(idListe);
                Intent intentSupprimer = new Intent(EditGroceryList.this, MainActivity.class);
                intentSupprimer.putExtra("CANCEL_MSG", getResources().getString(R.string.list_deletion_confirmed));
                setResult(Activity.RESULT_CANCELED, intentSupprimer);
                intentSupprimer.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentSupprimer);
            }
        });
        confirmDeleteArticle.setNegativeButton(R.string.no_cancel, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogI, int which) {
                Toast.makeText(getApplicationContext(), getString(R.string.list_deletion_canceled), Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialogConfirmDeleteArticle = confirmDeleteArticle.create();
        dialogConfirmDeleteArticle.show();
    }

    public void onClickValider(View view) {
        String nomArticle = this.txNomListe.getText().toString();
        if (nomListeCorrect(nomArticle)) {
            db.modifyListe(this.idListe, nomArticle);
            Intent intentValider = new Intent();
            intentValider.putExtra("SUCCESS_MSG", getResources().getString(R.string.edit_liste_successful));
            intentValider.putExtra("ID_LISTE", this.idListe);
            setResult(Activity.RESULT_OK, intentValider);
            finish();
        }
    }
        private boolean nomListeCorrect(String nomListe) {
            if (nomListe.isEmpty()) {
                this.txNomListe.setError(getResources().getString(R.string.nom_liste_vide));
                this.txNomListe.requestFocus();
                return false;
            } else {
                return true;
            }
        }

    // Gestion du résultat envoyé par une autre activité
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 134679852) {
            recreate();
        }
    }
}
