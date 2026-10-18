# Weather Utility App

A simple weather utility app for Android that shows current weather conditions at a glance.
Built with **Kotlin**, **Jetpack Compose**, and **Material Design 3** for the CP3406 Assessment 1.


## Features

### Weather Screen (Main)
- Displays current temperature with a large, readable format
- Shows weather condition with an intuitive emoji icon (☀️ ⛅ 🌧️ etc)
- Detail cards for **Feels Like**, **Humidity**, and **Wind Speed**
- Manual refresh button to get the latest data
- Loading spinner and error handling with retry

### Settings Screen
- **Temperature Unit**: Toggle between °C and °F using a Switch
- **City Selection**: Choose from 5 Australian cities via RadioButtons
- Changes are immediately reflected on the Weather screen
- Settings are shared between screens via a ViewModel (not persistent)

## Architecture

The app follows modern Android architecture patterns:

```
┌─────────────┐     ┌──────────────┐     ┌─────────────────┐     ┌─────────────┐
│   UI Layer  │ ──▶ │  ViewModel   │ ──▶ │   Repository    │ ──▶ │  Retrofit   │
│  (Compose)  │ ◀── │ (StateFlow)  │ ◀── │  (Data Layer)   │ ◀── │  (API)      │
└─────────────┘     └──────────────┘     └─────────────────┘     └─────────────┘
```

| Layer | File | Purpose |
|-------|------|---------|
| **UI** | `UtilityScreen.kt`, `SettingsScreen.kt` | Jetpack Compose UI |
| **ViewModel** | `WeatherViewModel.kt` | State management with StateFlow |
| **Repository** | `WeatherRepository.kt` | Data transformation layer |
| **API** | `WeatherApiService.kt` | Retrofit interface for Open-Meteo |
| **DI** | `AppContainer.kt` | Manual dependency injection |
| **Models** | `WeatherData.kt` | Data classes for API + UI |

## API

Uses the [Open-Meteo API](https://open-meteo.com/) — a free, open-source weather API that requires no API key.

**Endpoint**: `GET https://api.open-meteo.com/v1/forecast`

**Parameters used**:
- `latitude`, `longitude` — city coordinates
- `current` — weather variables (temperature, humidity, wind, etc.)


## Key Concepts Demonstrated

| Week | Concept | Implementation |
|------|---------|---------------|
| 1 | Kotlin + Android Studio | All source code in Kotlin |
| 2 | Jetpack Compose Layouts | Column, Row, Card, Box composables |
| 3 | Material Design 3 | Theme, NavigationBar, Cards, Typography |
| 4 | ViewModel + DI + Repository | WeatherViewModel, AppContainer, WeatherRepository |
| 5 | Retrofit (Web API) | WeatherApiService with Open-Meteo |

## Project Structure

```
app/src/main/java/au/edu/jcu/assessment/utilityapp/
├── MainActivity.kt              — Entry point, sets up ViewModel + navigation
├── UtilityApplication.kt        — Application class for DI initialization
├── data/
│   ├── model/
│   │   └── WeatherData.kt       — Data classes (API response + UI model)
│   ├── remote/
│   │   └── WeatherApiService.kt — Retrofit API interface
│   └── repository/
│       └── WeatherRepository.kt — Repository pattern implementation
├── di/
│   └── AppContainer.kt          — Manual DI container
└── ui/
    ├── WeatherViewModel.kt      — State management
    ├── screens/
    │   ├── UtilityScreen.kt     — Main weather display
    │   └── SettingsScreen.kt    — User preferences
    └── theme/
        ├── Color.kt             — Color palette
        ├── Theme.kt             — Material 3 theme
        └── Type.kt              — Typography
```


