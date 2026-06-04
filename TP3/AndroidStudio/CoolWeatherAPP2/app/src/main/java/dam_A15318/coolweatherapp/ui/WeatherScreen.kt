package dam_A15318.coolweatherapp.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dam_A15318.coolweatherapp.R
import dam_A15318.coolweatherapp.data.WMO_WeatherCode
import dam_A15318.coolweatherapp.data.getWeatherCodeMap
import dam_A15318.coolweatherapp.viewmodel.WeatherViewModel

@Composable
fun WeatherUI(weatherViewModel: WeatherViewModel = viewModel()) {
    val weatherUIState by weatherViewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    val hour = weatherUIState.time.let {
        if (it.length >= 13) it.substring(11, 13).toIntOrNull() ?: 12 else 12
    }
    val isDay = hour in 6..18

    val mapt = getWeatherCodeMap()
    val wCode = mapt[weatherUIState.weathercode]
    val wImageName = when (wCode) {
        WMO_WeatherCode.CLEAR_SKY,
        WMO_WeatherCode.MAINLY_CLEAR,
        WMO_WeatherCode.PARTLY_CLOUDY -> if (isDay) "${wCode.image}day" else "${wCode.image}night"
        else -> wCode?.image ?: "fog"
    }
    val wIcon = context.resources
        .getIdentifier(wImageName, "drawable", context.packageName)
        .takeIf { it != 0 } ?: R.drawable.fog

    val backgroundBrush = if (isDay) {
        Brush.verticalGradient(colors = listOf(Color(0xFF4A90E2), Color(0xFF50E3C2)))
    } else {
        Brush.verticalGradient(colors = listOf(Color(0xFF1F1C2C), Color(0xFF928DAB)))
    }

    val cardBackgroundColor = if (isDay) Color(0xFFFFFFFF).copy(alpha = 0.85f) else Color(0xFF23252F).copy(alpha = 0.85f)
    val contentColor = if (isDay) Color(0xFF2C3E50) else Color(0xFFECEFF1)

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        LandscapeWeatherUI(
            wIcon = wIcon,
            backgroundBrush = backgroundBrush,
            cardBgColor = cardBackgroundColor,
            contentColor = contentColor,
            uiState = weatherUIState,
            onLatitudeChange = { weatherViewModel.updateLatitude(it.toFloatOrNull() ?: 0f) },
            onLongitudeChange = { weatherViewModel.updateLongitude(it.toFloatOrNull() ?: 0f) },
            onUpdateButtonClick = { weatherViewModel.fetchWeather() }
        )
    } else {
        PortraitWeatherUI(
            wIcon = wIcon,
            backgroundBrush = backgroundBrush,
            cardBgColor = cardBackgroundColor,
            contentColor = contentColor,
            uiState = weatherUIState,
            onLatitudeChange = { weatherViewModel.updateLatitude(it.toFloatOrNull() ?: 0f) },
            onLongitudeChange = { weatherViewModel.updateLongitude(it.toFloatOrNull() ?: 0f) },
            onUpdateButtonClick = { weatherViewModel.fetchWeather() }
        )
    }
}

@Composable
fun PortraitWeatherUI(
    wIcon: Int,
    backgroundBrush: Brush,
    cardBgColor: Color,
    contentColor: Color,
    uiState: WeatherUIState,
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Image(painter = painterResource(id = wIcon), contentDescription = null, modifier = Modifier.size(130.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "${uiState.temperature}°C", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator(color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
        }

        CoordinatesCard(
            latitude = uiState.latitude,
            longitude = uiState.longitude,
            cardBgColor = cardBgColor,
            contentColor = contentColor,
            onLatitudeChange = onLatitudeChange,
            onLongitudeChange = onLongitudeChange
        )
        Spacer(modifier = Modifier.height(16.dp))

        WeatherCardCustom(
            seaLevelPressure = uiState.seaLevelPressure,
            windDirection = uiState.winddirection,
            windSpeed = uiState.windspeed,
            time = uiState.time,
            cardBgColor = cardBgColor,
            contentColor = contentColor
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onUpdateButtonClick,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f), contentColor = Color.White)
        ) {
            Text(stringResource(R.string.button), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun LandscapeWeatherUI(
    wIcon: Int,
    backgroundBrush: Brush,
    cardBgColor: Color,
    contentColor: Color,
    uiState: WeatherUIState,
    onLatitudeChange: (String) -> Unit,
    onLongitudeChange: (String) -> Unit,
    onUpdateButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(painter = painterResource(id = wIcon), contentDescription = null, modifier = Modifier.size(90.dp))
                Text(text = "${uiState.temperature}°C", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)

                if (uiState.isLoading) {
                    Spacer(modifier = Modifier.height(8.dp))
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                }
            }

            CoordinatesCard(
                latitude = uiState.latitude,
                longitude = uiState.longitude,
                cardBgColor = cardBgColor,
                contentColor = contentColor,
                onLatitudeChange = onLatitudeChange,
                onLongitudeChange = onLongitudeChange,
                modifier = Modifier.weight(1f)
            )

            WeatherCardCustom(
                seaLevelPressure = uiState.seaLevelPressure,
                windDirection = uiState.winddirection,
                windSpeed = uiState.windspeed,
                time = uiState.time,
                cardBgColor = cardBgColor,
                contentColor = contentColor,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onUpdateButtonClick,
            modifier = Modifier.fillMaxWidth().height(46.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f), contentColor = Color.White)
        ) {
            Text(stringResource(R.string.button), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}