package com.iut.mygrocerylist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class EditGroceryList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grocery_list);

        TextView testId = findViewById(R.id.idTestEdit);
        testId.setText(this.getIntent().getExtras().getString("ID_LISTE"));
    }
}
