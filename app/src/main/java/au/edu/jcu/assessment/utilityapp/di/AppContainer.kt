package au.edu.jcu.assessment.utilityapp.di

import au.edu.jcu.assessment.utilityapp.data.remote.WeatherApiService
import au.edu.jcu.assessment.utilityapp.data.repository.WeatherRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Manual dependency injection container.
 *
 * Instead of using a framework like Hilt or Koin, we create and connect
 * all our dependencies right here. This is simpler and easier to understand:
 * - Retrofit is created with the base URL and JSON converter
 * - The API service is created from the Retrofit instance
 * - The repository gets the API service injected through its constructor
 *
 * The Activity accesses this container through the Application class.
 */
class AppContainer {

    // Base URL for the Open-Meteo API — free, no API key needed
    private val baseUrl = "https://api.open-meteo.com/"

    // Retrofit handles all the networking: HTTP requests, JSON parsing, etc.
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create the API service from our Retrofit interface definition
    private val weatherApiService: WeatherApiService =
        retrofit.create(WeatherApiService::class.java)

    // The repository is the only thing exposed to the rest of the app
    val weatherRepository: WeatherRepository = WeatherRepository(weatherApiService)
}
