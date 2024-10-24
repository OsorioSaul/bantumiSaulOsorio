package es.upm.miw.bantumi.persistencia.daos;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.dominio.logica.JuegoBantumi;


@Entity(tableName = "puntuaciones")
public class Puntuacion {
    @PrimaryKey(autoGenerate = true)
    protected Integer sId;

    @ColumnInfo(name = "nombre_del_jugador")
    protected String nombreDelJugador;

    @ColumnInfo(name = "almacen_jugador_1")
    protected Integer almacenJugador1;

    @ColumnInfo(name = "almacen_jugador_2")
    protected Integer almacenJugador2;

    @ColumnInfo(name = "fecha")
    protected Date fecha;


    public Integer getsId() {
        return sId;
    }

    public void setsId(Integer sId) {
        this.sId = sId;
    }

    public String getNombreDelJugador() {
        return nombreDelJugador;
    }

    public void setNombreDelJugador(String nombreDelJugador) {
        this.nombreDelJugador = nombreDelJugador;
    }

    public Integer getAlmacenJugador1() {
        return almacenJugador1;
    }

    public void setAlmacenJugador1(Integer almacenJugador1) {
        this.almacenJugador1 = almacenJugador1;
    }

    public Integer getAlmacenJugador2() {
        return almacenJugador2;
    }


    public void setAlmacenJugador2(Integer almacenJugador2) {
        this.almacenJugador2 = almacenJugador2;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public static Puntuacion fromActualGame(JuegoBantumi actualGame, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String nombreDelJugador = preferences.getString("nombreJugador", "Jugador1");
        Puntuacion puntuacion = new Puntuacion();
        puntuacion.almacenJugador1 = actualGame.getSemillas(6);
        puntuacion.almacenJugador2 = actualGame.getSemillas(13);
        puntuacion.nombreDelJugador = nombreDelJugador;
        puntuacion.fecha = Calendar.getInstance().getTime();
        return puntuacion;
    }
}
