package es.upm.miw.bantumi.ui.actividades;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import es.upm.miw.bantumi.persistencia.database.AppDatabase;
import es.upm.miw.bantumi.persistencia.daos.Puntuacion;
import es.upm.miw.bantumi.ui.fragmentos.FinalAlertDialog;
import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.dominio.logica.JuegoBantumi;
import es.upm.miw.bantumi.ui.fragmentos.RestartDialog;
import es.upm.miw.bantumi.ui.viewmodel.BantumiViewModel;

public class MainActivity extends AppCompatActivity {

    protected final String LOG_TAG = "MiW";
    public JuegoBantumi juegoBantumi;
    private BantumiViewModel bantumiVM;
    int numInicialSemillas;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Instancia el ViewModel y el juego, y asigna observadores a los huecos
        numInicialSemillas = getResources().getInteger(R.integer.intNumInicialSemillas);
        bantumiVM = new ViewModelProvider(this).get(BantumiViewModel.class);
        juegoBantumi = new JuegoBantumi(bantumiVM, JuegoBantumi.Turno.turnoJ1, numInicialSemillas);
        crearObservadores();
        db = instanciarBaseDeDatos();
    }

    /**
     * Crea y subscribe los observadores asignados a las posiciones del tablero.
     * Si se modifica el contenido del tablero -> se actualiza la vista.
     */
    private void crearObservadores() {
        for (int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
            int finalI = i;
            bantumiVM.getNumSemillas(i).observe(    // Huecos y almacenes
                    this,
                    new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            mostrarValor(finalI, juegoBantumi.getSemillas(finalI));
                        }
                    });
        }
        bantumiVM.getTurno().observe(   // Turno
                this,
                new Observer<JuegoBantumi.Turno>() {
                    @Override
                    public void onChanged(JuegoBantumi.Turno turno) {
                        marcarTurno(juegoBantumi.turnoActual());
                    }
                }
        );
    }

    /**
     * Indica el turno actual cambiando el color del texto
     *
     * @param turnoActual turno actual
     */
    private void marcarTurno(@NonNull JuegoBantumi.Turno turnoActual) {
        TextView tvJugador1 = findViewById(R.id.tvPlayer1);
        TextView tvJugador2 = findViewById(R.id.tvPlayer2);
        switch (turnoActual) {
            case turnoJ1:
                tvJugador1.setTextColor(getColor(R.color.white));
                tvJugador1.setBackgroundColor(getColor(android.R.color.holo_blue_light));
                tvJugador2.setTextColor(getColor(R.color.black));
                tvJugador2.setBackgroundColor(getColor(R.color.white));
                break;
            case turnoJ2:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador1.setBackgroundColor(getColor(R.color.white));
                tvJugador2.setTextColor(getColor(R.color.white));
                tvJugador2.setBackgroundColor(getColor(android.R.color.holo_blue_light));
                break;
            default:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador2.setTextColor(getColor(R.color.black));
        }
    }

    /**
     * Muestra el valor <i>valor</i> en la posición <i>pos</i>
     *
     * @param pos   posición a actualizar
     * @param valor valor a mostrar
     */
    private void mostrarValor(int pos, int valor) {
        String num2digitos = String.format(Locale.getDefault(), "%02d", pos);
        // Los identificadores de los huecos tienen el formato casilla_XX
        int idBoton = getResources().getIdentifier("casilla_" + num2digitos, "id", getPackageName());
        if (0 != idBoton) {
            TextView viewHueco = findViewById(idBoton);
            viewHueco.setText(String.valueOf(valor));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast toast = new Toast(this);
        switch (item.getItemId()) {
//            case R.id.opcAjustes: // @todo Preferencias
//                startActivity(new Intent(this, BantumiPrefs.class));
//                return true;
            case R.id.opcAcercaDe:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.aboutTitle)
                        .setMessage(R.string.aboutMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return true;

            // @TODO!!! resto opciones
            case R.id.opcGuardarPartida:
                try {
                    FileOutputStream fos = openFileOutput("partida.txt", Context.MODE_PRIVATE);
                    fos.write(juegoBantumi.serializa().getBytes());
                    fos.close();
                    toast.setText("Partida guardada");
                    toast.show();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;

            case R.id.opcRecuperarPartida:
                try {
                    BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput("partida.txt")));
                    StringBuilder estadoDelJuego = leeEstadoDelJuego(fin);
                    juegoBantumi.deserializa(estadoDelJuego.toString());
                    toast.setText("Partida recuperada");
                    toast.show();
                } catch (FileNotFoundException e) {
                    toast.setText("Partida no encontrada");
                    toast.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;
            case R.id.opcReiniciarPartida:
                RestartDialog restartDialog = new RestartDialog();
                restartDialog.show(getSupportFragmentManager(), "ConfirmRestartDialog");
                return true;

            case R.id.opcMejoresResultados:
                desplegarMejoresResultados();
                return true;

            default:
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtSinImplementar),
                        Snackbar.LENGTH_LONG
                ).show();
        }
        return true;
    }



    /**
     * Acción que se ejecuta al pulsar sobre cualquier hueco
     *
     * @param v Vista pulsada (hueco)
     */
    public void huecoPulsado(@NonNull View v) {
        String resourceName = getResources().getResourceEntryName(v.getId()); // pXY
        int num = Integer.parseInt(resourceName.substring(resourceName.length() - 2));
        Log.i(LOG_TAG, "huecoPulsado(" + resourceName + ") num=" + num);
        switch (juegoBantumi.turnoActual()) {
            case turnoJ1:
                Log.i(LOG_TAG, "* Juega Jugador");
                juegoBantumi.jugar(num);
                break;
            case turnoJ2:
                Log.i(LOG_TAG, "* Juega Computador");
                juegoBantumi.juegaComputador();
                break;
            default:    // JUEGO TERMINADO
                finJuego();
        }
        if (juegoBantumi.juegoTerminado()) {
            finJuego();
        }
    }

    /**
     * El juego ha terminado. Volver a jugar?
     */
    private void finJuego() {
        String texto = (juegoBantumi.getSemillas(6) > 6 * numInicialSemillas)
                ? "Gana Jugador 1"
                : "Gana Jugador 2";
        if (juegoBantumi.getSemillas(6) == 6 * numInicialSemillas) {
            texto = "¡¡¡ EMPATE !!!";
        }
        // @TODO guardar puntuación
        Puntuacion puntuacion = Puntuacion.fromActualGame(juegoBantumi, this);
        // terminar
        new FinalAlertDialog(texto).show(getSupportFragmentManager(), "ALERT_DIALOG");
        //Guardar puntuación en la base de datos.
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.puntuacionDao().insertAll(puntuacion);
            }
        }).start();
    }

    @NonNull
    private static StringBuilder leeEstadoDelJuego(BufferedReader fin) throws IOException {
        StringBuilder estadoDelJuego = new StringBuilder();
        String linea;
        while ((linea = fin.readLine()) != null) {
            estadoDelJuego.append(linea);
            estadoDelJuego.append('\n');
        }
        return estadoDelJuego;
    }

    @NonNull
    private AppDatabase instanciarBaseDeDatos() {
        return Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "puntuaciones-db"
        ).build();
    }

    private void desplegarMejoresResultados() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Puntuacion> puntuaciones = db.puntuacionDao().getTopTenScores();
            runOnUiThread(() -> {
                String[] renglonDePuntuaciones = new String[puntuaciones.size()];
                for (int i = 0; i < puntuaciones.size(); i++) {
                    renglonDePuntuaciones[i] = puntuaciones.get(i).toStilizedString();
                }
                Intent intent = new Intent(MainActivity.this, MejoresResultadosActivity.class);
                intent.putExtra("puntuaciones", renglonDePuntuaciones);
                startActivity(intent);
            });
        });
    }
}