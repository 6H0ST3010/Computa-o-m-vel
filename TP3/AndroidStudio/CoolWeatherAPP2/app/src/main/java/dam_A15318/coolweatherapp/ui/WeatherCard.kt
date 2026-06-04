package dam_A15318.coolweatherapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dam_A15318.coolweatherapp.R

@Composable
fun WeatherCardCustom(
    seaLevelPressure: Float,
    windDirection: Int,
    windSpeed: Float,
    time: String,
    cardBgColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor, contentColor = contentColor),
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.weather_details),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp),
                color = contentColor
            )

            WeatherRowItem(label = stringResource(R.string.sea_level_pressure), value = "$seaLevelPressure hPa", contentColor = contentColor)
            HorizontalDivider(color = contentColor.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 8.dp))

            WeatherRowItem(label = stringResource(R.string.wind_direction), value = "$windDirection°", contentColor = contentColor)
            HorizontalDivider(color = contentColor.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 8.dp))

            WeatherRowItem(label = stringResource(R.string.wind_speed), value = "$windSpeed km/h", contentColor = contentColor)
            HorizontalDivider(color = contentColor.copy(alpha = 0.1f), modifier = Modifier.padding(vertical = 8.dp))

            WeatherRowItem(label = stringResource(R.string.time), value = time, contentColor = contentColor)
        }
    }
}