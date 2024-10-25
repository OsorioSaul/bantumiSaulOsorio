package es.upm.miw.bantumi.persistencia.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PuntuacionDao {
    @Query("SELECT * FROM puntuaciones")
    List<Puntuacion> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Puntuacion... puntuaciones);

    @Query("SELECT *  \n" +
            "FROM puntuaciones  \n" +
            "ORDER BY   \n" +
            "    CASE   \n" +
            "        WHEN almacen_jugador_1 > almacen_jugador_2 THEN almacen_jugador_1  \n" +
            "        ELSE almacen_jugador_2  \n" +
            "    END DESC  \n" +
            "LIMIT 10;  ")
    List<Puntuacion> getTopTenScores();

    @Query("DELETE FROM puntuaciones")
    void deleteAll();
}
