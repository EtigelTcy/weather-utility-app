package au.edu.jcu.assessment.utilityapp.data.remote

import au.edu.jcu.assessment.utilityapp.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for the Open-Meteo weather API.
 * Each function here maps to an HTTP GET request.
 *
 * Base URL: https://api.open-meteo.com/
 * This API is free and doesn't require an API key.
 */
interface WeatherApiService {

    /**
     * Fetches current weather data for a given latitude/longitude.
     *
     * Example request:
     * GET /v1/forecast?latitude=-19.26&longitude=146.80&current=temperature_2m,...
     *
     * The [current] parameter tells the API which weather variables we want.
     */
    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m"
    ): WeatherResponse
}
