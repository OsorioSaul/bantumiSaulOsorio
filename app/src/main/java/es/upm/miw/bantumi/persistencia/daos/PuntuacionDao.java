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
}
