package es.upm.miw.bantumi.ui.actividades;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.persistencia.database.AppDatabase;
import es.upm.miw.bantumi.ui.fragmentos.DeleteScoresDialog;

public class MejoresResultadosActivity extends AppCompatActivity {
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "puntuaciones-db"
        ).build();

        setContentView(R.layout.activity_mejores_resultados);

        ListView listView = findViewById(R.id.listViewMejoresResultados);

        String[] items = getIntent().getStringArrayExtra("puntuaciones");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        Button buttonOpenDialog = findViewById(R.id.buttonOpenDialog);
        buttonOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteScoresDialog deleteScoresDialog = new DeleteScoresDialog(db);
                deleteScoresDialog.show(getSupportFragmentManager(), "ConfirmDeleteScoresDialog");
            }
        });
    }
}
