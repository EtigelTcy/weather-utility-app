package au.edu.jcu.assessment.utilityapp

import android.app.Application
import au.edu.jcu.assessment.utilityapp.di.AppContainer

/**
 * Custom Application class — this is the first thing that runs when the app starts.
 *
 * We use it to set up the DI container so that all our dependencies
 * (Retrofit, Repository, etc.) are ready before any screen loads.
 *
 * Activities can access the container like this:
 *   val app = application as UtilityApplication
 *   val repo = app.container.weatherRepository
 */
class UtilityApplication : Application() {

    // The container holds all our app-level dependencies
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}
