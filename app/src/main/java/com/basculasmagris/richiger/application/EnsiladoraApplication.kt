package com.basculasmagris.richiger.application

import android.app.Application
import com.basculasmagris.richiger.model.database.*

class EnsiladoraApplication: Application() {
    private  val database by lazy { RichigerRoomDatabase.getDatabase((this@EnsiladoraApplication))}
    val corralRepository by lazy { CorralRepository(database.corralDao()) }
    val dietRepository by lazy { DietRepository(database.dietDao()) }
    val establishmentRepository by lazy { EstablishmentRepository(database.establishmentDao()) }
    val mixerRepository by lazy { MixerRepository(database.mixerDao()) }
    val productRepository by lazy { ProductRepository(database.productDao()) }
    val carroRepository by lazy { CarroRepository(database.carroDao()) }
    val userRepository by lazy { UserRepository(database.userDao())}
    val roundRepository by lazy { RoundRepository(database.roundDao())}
}