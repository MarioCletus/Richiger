package com.basculasmagris.richiger.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.basculasmagris.richiger.model.entities.*

@Database(entities = [
    User::class,
    Product::class,
    Mixer::class,
    Establishment::class,
    Corral::class,
    DietProduct::class,
    Diet::class,
    Round::class,
    RoundCorral::class], version = 1)

abstract class RichigerRoomDatabase: RoomDatabase() {

    abstract fun userDao() : UserDao
    abstract fun productDao() : ProductDao
    abstract fun mixerDao() : MixerDao
    abstract fun establishmentDao() : EstablishmentDao
    abstract fun corralDao() : CorralDao
    abstract fun dietDao() : DietDao
    abstract fun roundDao() : RoundDao

    companion object {
        @Volatile
        private  var INSTANCE: RichigerRoomDatabase? = null

        fun getDatabase(context: Context): RichigerRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RichigerRoomDatabase::class.java,
                    "spi_mixer_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}