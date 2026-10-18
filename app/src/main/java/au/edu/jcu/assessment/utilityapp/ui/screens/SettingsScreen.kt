package au.edu.jcu.assessment.utilityapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import au.edu.jcu.assessment.utilityapp.ui.TemperatureUnit
import au.edu.jcu.assessment.utilityapp.ui.WeatherViewModel

/**
 * Settings screen — lets the user configure the app.
 *
 * Currently supports two settings:
 * 1. Temperature unit (°C or °F) — controlled by a Switch
 * 2. City selection — controlled by RadioButtons
 *
 * Changes here immediately affect the main weather screen
 * because both screens share the same ViewModel.
 * Note: settings are not persisted (they reset when the app closes).
 */
@Composable
fun SettingsScreen(viewModel: WeatherViewModel) {
    val selectedCity by viewModel.selectedCity.collectAsState()
    val temperatureUnit by viewModel.temperatureUnit.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        // --- Temperature Unit Section ---
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Temperature Unit",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Toggle between Celsius and Fahrenheit
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "°C",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = temperatureUnit == TemperatureUnit.FAHRENHEIT,
                        onCheckedChange = { isFahrenheit ->
                            viewModel.setTemperatureUnit(
                                if (isFahrenheit) TemperatureUnit.FAHRENHEIT
                                else TemperatureUnit.CELSIUS
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "°F",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        // --- City Selection Section ---
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select City",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Show a radio button for each available city
                viewModel.availableCities.forEach { city ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectCity(city) }
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = city == selectedCity,
                            onClick = { viewModel.selectCity(city) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = city.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
