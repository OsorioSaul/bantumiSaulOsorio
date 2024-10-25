package es.upm.miw.bantumi.ui.fragmentos;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;


import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import es.upm.miw.bantumi.persistencia.database.AppDatabase;



public class DeleteScoresDialog extends DialogFragment {
    AppDatabase db;

    public DeleteScoresDialog(AppDatabase db) {
        super();
        this.db = db;
    }

    @NonNull
    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmación")
                .setMessage("¿Desea eliminar las puntuaciones?")
                .setPositiveButton(
                        getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ExecutorService executor = Executors.newSingleThreadExecutor();
                                executor.execute(() -> {
                                    db.puntuacionDao().deleteAll();
                                    requireActivity().runOnUiThread(() -> {
                                        Toast toast = new Toast(getContext());
                                        toast.setText("partidas eliminadas");
                                        toast.show();
                                        getActivity().recreate();
                                    });
                                });
                            }

                        }
                ).setNegativeButton(
                        getString(android.R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }
                );

        return builder.create();
    }
}
