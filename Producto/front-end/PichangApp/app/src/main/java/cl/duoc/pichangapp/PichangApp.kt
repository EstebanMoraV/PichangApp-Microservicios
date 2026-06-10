package cl.duoc.pichangapp

import android.app.Application
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PichangApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Inicializa el SDK de Places para el autocompletado de direcciones.
        if (!Places.isInitialized() && BuildConfig.MAPS_API_KEY.isNotBlank()) {
            Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        }
    }
}
