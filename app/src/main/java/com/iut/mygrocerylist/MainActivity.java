package com.iut.mygrocerylist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ArrayList<HashMap<String, String>> listeListes;
    private String nomListe, idListe;
    private View dialogView;
    private BottomSheetDialog dialog;
    private Intent intentEdit;
    private final GroceryDatabase db = new GroceryDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.getTheme(this));
        setContentView(R.layout.activity_main);

        idListe = null;

        dialogView = getLayoutInflater().inflate(R.layout.list_bottom_sheet, null);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);
        dialog.dismiss();

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
        ListAdapter adapter = new ListesAdapter(MainActivity.this, listeListes, R.layout.list_row_listes,
                new String[]{"nom", "progressValue"},
                new int[]{R.id.listeNom, R.id.listProgressValue});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, GroceryList.class);
                intent.putExtra("ID_LISTE", listeListes.get(position).get(GroceryDatabase.LISTE_ID));
                intent.putExtra("NOM_LISTE", listeListes.get(position).get(GroceryDatabase.LISTE_NOM));
                intent.putExtra("PROGRESS_VALUE", listeListes.get(position).get("progressValue"));
                startActivityForResult(intent, 1);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
                dialog.show();
                intentEdit = new Intent(MainActivity.this, EditGroceryList.class);
                intentEdit.putExtra("ID_LISTE", listeListes.get(i).get(GroceryDatabase.LISTE_ID));
                nomListe = listeListes.get(i).get(GroceryDatabase.LISTE_NOM);
                idListe = listeListes.get(i).get(GroceryDatabase.LISTE_ID);
                return true;
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
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Clic sur le bottom sheet
    public void onClickEditBottom(View view) {
        dialog.dismiss();
        startActivityForResult(intentEdit,1);
    }

    public void onClickDeleteBottom(View view) {
        AlertDialog.Builder confirmDeleteList = new AlertDialog.Builder(this);
        confirmDeleteList.setMessage(getString(R.string.confirm_delete) + " " + nomListe + getString(R.string.q_mark));
        confirmDeleteList.setPositiveButton(R.string.yes_delete, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogI, int which) {
                Toast.makeText(getApplicationContext(), getString(R.string.list_deletion_confirmed), Toast.LENGTH_SHORT).show();
                db.deleteListe(idListe);
                dialog.dismiss();
                recreate();
            }
        });
        confirmDeleteList.setNegativeButton(R.string.no_cancel, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogI, int which) {
                Toast.makeText(getApplicationContext(), getString(R.string.list_deletion_canceled), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        AlertDialog dialogConfirmDeleteList = confirmDeleteList.create();
        dialogConfirmDeleteList.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        recreate();

        if (resultCode == Activity.RESULT_CANCELED) {
            recreate();
            Toast.makeText(getApplicationContext(), data.getStringExtra("CANCEL_MSG"), Toast.LENGTH_SHORT).show();
        }

        if (resultCode == Activity.RESULT_OK) {
            recreate();
            String id = data.getStringExtra("ID_LISTE");
            Intent intent = new Intent(MainActivity.this, GroceryList.class);
            intent.putExtra("ID_LISTE", id);
            startActivity(intent);
        }

        if (resultCode == 134679852) {
            recreate();
        }
    }
}
