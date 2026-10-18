package au.edu.jcu.assessment.utilityapp.data.model

/**
 * Data classes that map directly to the Open-Meteo API JSON response.
 * Field names match the API exactly so Gson can parse them automatically.
 */

data class WeatherResponse(
    val current: CurrentWeather
)

data class CurrentWeather(
    val temperature_2m: Double,
    val relative_humidity_2m: Int,
    val apparent_temperature: Double,
    val weather_code: Int,
    val wind_speed_10m: Double
)

/**
 * A cleaner model for displaying weather info in the UI.
 * We convert the raw API data into this before showing it on screen,
 * so the UI doesn't need to know about API field names.
 */
data class WeatherInfo(
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val conditionText: String,
    val conditionEmoji: String
)

/**
 * Represents a city the user can select in settings.
 * Coordinates are used for the API request.
 */
data class City(
    val name: String,
    val latitude: Double,
    val longitude: Double
)
