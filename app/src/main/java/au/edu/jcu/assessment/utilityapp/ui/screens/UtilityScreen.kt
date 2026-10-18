package au.edu.jcu.assessment.utilityapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import au.edu.jcu.assessment.utilityapp.ui.WeatherUiState
import au.edu.jcu.assessment.utilityapp.ui.WeatherViewModel

/**
 * Main utility screen — shows current weather at a glance.
 *
 * Displays a big emoji for the weather condition, the temperature,
 * and detail cards for humidity, wind speed, and "feels like" temperature.
 * This is the core "utility" part of the app.
 */
@Composable
fun UtilityScreen(viewModel: WeatherViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Show which city we're displaying weather for
        Text(
            text = selectedCity.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Show different content based on the current state
        when (val state = uiState) {
            is WeatherUiState.Loading -> {
                // Centered loading spinner
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is WeatherUiState.Error -> {
                // Error message with a retry button
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "😔", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadWeather() }) {
                        Text("Try Again")
                    }
                }
            }

            is WeatherUiState.Success -> {
                val weather = state.weather

                Spacer(modifier = Modifier.height(16.dp))

                // Big weather emoji — the main visual element
                Text(
                    text = weather.conditionEmoji,
                    fontSize = 96.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Large temperature display
                Text(
                    text = viewModel.displayTemperature(weather.temperature),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Weather condition text (e.g., "Partly cloudy")
                Text(
                    text = weather.conditionText,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Detail cards row — shows additional weather info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherDetailCard(
                        label = "Feels Like",
                        value = viewModel.displayTemperature(weather.feelsLike),
                        emoji = "🌡️"
                    )
                    WeatherDetailCard(
                        label = "Humidity",
                        value = "${weather.humidity}%",
                        emoji = "💧"
                    )
                    WeatherDetailCard(
                        label = "Wind",
                        value = "${weather.windSpeed.toInt()} km/h",
                        emoji = "💨"
                    )
                }

                // Push the refresh button to the bottom
                Spacer(modifier = Modifier.weight(1f))

                // Manual refresh button
                OutlinedButton(onClick = { viewModel.loadWeather() }) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Refresh weather"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Refresh")
                }
            }
        }
    }
}

/**
 * A small card that displays one piece of weather detail.
 * Used for humidity, wind speed, and feels-like temperature.
 */
@Composable
fun WeatherDetailCard(label: String, value: String, emoji: String) {
    Card(
        modifier = Modifier.width(105.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
