package au.edu.jcu.assessment.utilityapp.data.repository

import au.edu.jcu.assessment.utilityapp.data.model.WeatherInfo
import au.edu.jcu.assessment.utilityapp.data.remote.WeatherApiService

/**
 * Repository sits between the API service and the ViewModel.
 * Its job is to fetch raw data from the API and transform it
 * into something the UI can easily display.
 *
 * This follows the Repository pattern — if we ever needed to add
 * caching or switch to a different data source, we'd only change this class.
 */
class WeatherRepository(private val apiService: WeatherApiService) {

    /**
     * Gets weather for a location and returns a clean WeatherInfo object.
     * Throws an exception if the network request fails.
     */
    suspend fun getWeather(latitude: Double, longitude: Double): WeatherInfo {
        val response = apiService.getCurrentWeather(latitude, longitude)
        val current = response.current

        // Convert the numeric weather code to a readable description + emoji
        val (conditionText, conditionEmoji) = mapWeatherCode(current.weather_code)

        return WeatherInfo(
            temperature = current.temperature_2m,
            feelsLike = current.apparent_temperature,
            humidity = current.relative_humidity_2m,
            windSpeed = current.wind_speed_10m,
            conditionText = conditionText,
            conditionEmoji = conditionEmoji
        )
    }

    /**
     * Maps Open-Meteo WMO weather codes to human-readable descriptions and emojis.
     * Full reference: https://open-meteo.com/en/docs
     */
    private fun mapWeatherCode(code: Int): Pair<String, String> {
        return when (code) {
            0 -> "Clear sky" to "☀️"
            1 -> "Mainly clear" to "🌤️"
            2 -> "Partly cloudy" to "⛅"
            3 -> "Overcast" to "☁️"
            45, 48 -> "Foggy" to "🌫️"
            51, 53, 55 -> "Drizzle" to "🌦️"
            61, 63, 65 -> "Rain" to "🌧️"
            66, 67 -> "Freezing rain" to "🌨️"
            71, 73, 75 -> "Snowfall" to "❄️"
            77 -> "Snow grains" to "🌨️"
            80, 81, 82 -> "Rain showers" to "🌧️"
            85, 86 -> "Snow showers" to "❄️"
            95 -> "Thunderstorm" to "⛈️"
            96, 99 -> "Thunderstorm with hail" to "⛈️"
            else -> "Unknown" to "🌡️"
        }
    }
}
