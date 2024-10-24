package es.upm.miw.bantumi.persistencia.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import es.upm.miw.bantumi.persistencia.converters.Converters;
import es.upm.miw.bantumi.persistencia.daos.PuntuacionDao;
import es.upm.miw.bantumi.persistencia.daos.Puntuacion;

@Database(entities = {Puntuacion.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract PuntuacionDao puntuacionDao();
}
