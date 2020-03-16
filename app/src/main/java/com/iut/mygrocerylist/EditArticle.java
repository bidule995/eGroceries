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
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class EditArticle extends AppCompatActivity {

    private final GroceryDatabase db = new GroceryDatabase(this);
    private String idArticle;
    private Article article;
    private TextView txArticleName, txArticleRemarques, txArticleQuantite;
    private CheckBox cbArticleRecupere;
    private RadioGroup rgPriorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        this.idArticle = this.getIntent().getExtras().getString("ID_ARTICLE");
        this.article = db.getArticle(idArticle);
        initializeForm();
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
            startActivity(intent);
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
        intent.putExtra("CANCEL_MSG", getResources().getString(R.string.cancel_edit_article));
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    // Initialiser le formulaire
    public void initializeForm() {
        this.txArticleName = findViewById(R.id.modifyArticleName);
        this.txArticleName.setText(article.getNom());
        this.txArticleRemarques = findViewById(R.id.modifyArticleRemarques);
        this.txArticleRemarques.setText(article.getRemarques());
        this.txArticleQuantite = findViewById(R.id.modifyArticleQuantite);
        this.txArticleQuantite.setText(article.getQuantite());
        this.cbArticleRecupere = findViewById(R.id.modifyArticleRecupere);
        String recupere = article.getRecupere();
        if(recupere == null) recupere = "0";
        if(recupere.equals("1")) {
            this.cbArticleRecupere.setChecked(true);
        } else {
            this.cbArticleRecupere.setChecked(false);
        }
        this.rgPriorite = findViewById(R.id.groupPrioriteEditArticle);
        switch(this.article.getPriorite()){
            case "1":
                this.rgPriorite.check(R.id.articleSetPrioriteIndispensable);
                break;
            case "3":
                this.rgPriorite.check(R.id.articleSetPrioriteBasse);
                break;
            default:
                this.rgPriorite.check(R.id.articleSetPrioriteMoyenne);
                break;
        }
    }

    // Gestion du clic sur le bouton valider
    public void onClickValider(View view) {
        String nomArticle = this.txArticleName.getText().toString();
        if(nomArticleCorrect(nomArticle)) {
            String remarquesArticle = this.txArticleRemarques.getText().toString();
            String quantiteArticle = this.txArticleQuantite.getText().toString();
            String recupereArticle;
            if (this.cbArticleRecupere.isChecked()) {
                recupereArticle = "1";
            } else {
                recupereArticle = "0";
            }
            String prioriteArticle = GroceryDatabase.getValueFromPriorite(((RadioButton) findViewById(this.rgPriorite.getCheckedRadioButtonId())).getText().toString(), EditArticle.this);
            db.modifyArticle(this.idArticle, nomArticle, quantiteArticle, prioriteArticle, remarquesArticle, recupereArticle);
            Intent intent = new Intent(EditArticle.this, GroceryList.class);
            intent.putExtra("SUCCESS_MSG", getResources().getString(R.string.edit_article_successful));
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    // Gestion du clic sur le bouton supprimer
    public void onClickSupprimer(View view) {
        final AlertDialog.Builder confirmDeleteArticle = new AlertDialog.Builder(this);
        confirmDeleteArticle.setMessage(getString(R.string.confirm_delete) + " " + article.getNom() + getString(R.string.q_mark));
        confirmDeleteArticle.setPositiveButton(R.string.yes_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogI, int which) {
                db.deleteArticle(idArticle);
                Intent intent = new Intent(EditArticle.this, GroceryList.class);
                intent.putExtra("CANCEL_MSG", getResources().getString(R.string.article_deletion_confirmed));
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
        confirmDeleteArticle.setNegativeButton(R.string.no_cancel, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogI, int which) {
                Toast.makeText(getApplicationContext(), getString(R.string.article_deletion_canceled), Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialogConfirmDeleteArticle = confirmDeleteArticle.create();
        dialogConfirmDeleteArticle.show();
    }

    private boolean nomArticleCorrect(String nomArticle) {
        if (nomArticle.isEmpty()) {
            this.txArticleName.setError(getResources().getString(R.string.nom_liste_vide));
            this.txArticleName.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}
