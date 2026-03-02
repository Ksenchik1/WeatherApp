package com.example.weatherapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen()
                }
            }
        }
    }
}

// Класс для хранения данных о городе
data class City(
    val nameRu: String,
    val nameEn: String,
    val id: String
)

data class ForecastPeriod(val label: String, val path: String)
val periodsList = listOf(
    ForecastPeriod("Сейчас", "now"),
    ForecastPeriod("Завтра", "tomorrow"),
    ForecastPeriod("На 3 дня", "3-days"),
    ForecastPeriod("На 10 дней", "10-days"),
    ForecastPeriod("На месяц", "month")
)

// Допустимый список городов
val citiesList = listOf(
    City("Москва", "moscow", "4368"),
    City("Санкт-Петербург", "saint-petersburg", "4079"),
    City("Нижний Новгород", "nizhny-novgorod", "4355"),
    City("Казань", "kazan", "4364"),
    City("Калининград", "kaliningrad", "4225"),
    City("Сочи", "sochi", "5233"),
    City("Хабаровск", "khabarovsk", "4862"),
    City("Норильск", "norilsk", "3957"),
    City("Омск", "omsk", "4578")
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen() {
    // Хранение состояния
    var cityExpanded by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf<City?>(null) }
    var periodExpanded by remember { mutableStateOf(false) }
    var selectedPeriod by remember { mutableStateOf<ForecastPeriod?>(null) }

    val context = LocalContext.current
    // Проверка валидности формы
    val isFormValid = selectedCity != null && selectedPeriod != null

    // Цвета
    val skyDark = Color(0xFF0D1B3F)
    val skyLight = Color(0xFF2196F3)
    val glassWhite = Color.White.copy(alpha = 0.15f)
    val glassBorder = Color.White.copy(alpha = 0.3f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(skyDark, skyLight)
                )
            )
    ) {


        // Вертикальный контейнер для элементов
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 600.dp)
                .align(Alignment.TopCenter)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "FORECAST",
                style = MaterialTheme.typography.displayMedium.copy(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.White, Color(0xFF2196F3))
                    )
                ),
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.weight(0.8f))

            // БЛОК ВЫБОРА
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                // ВЫБОР ГОРОДА
                ExposedDropdownMenuBox(
                    expanded = cityExpanded,
                    onExpandedChange = { cityExpanded = !cityExpanded }
                ) {
                    OutlinedTextField(
                        // Если city еще не выбран, поле будет пустым
                        value = selectedCity?.nameRu ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Выберите город") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),

                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = glassWhite,
                            unfocusedContainerColor = glassWhite,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = glassBorder,
                            unfocusedBorderColor = glassBorder,
                            focusedLabelColor = Color.White.copy(alpha = 0.7f),
                            unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = cityExpanded,
                        onDismissRequest = { cityExpanded = false },
                        containerColor = Color(0xFF3177B6).copy(alpha = 0.9f),
                        modifier = Modifier
                            .heightIn(max = 200.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        citiesList.forEach { city ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = city.nameRu,
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                onClick = {
                                    selectedCity = city
                                    cityExpanded = false
                                }
                            )
                        }
                    }
                }
                //ВЫБОР ПЕРИОДА
                ExposedDropdownMenuBox(
                    expanded = periodExpanded,
                    onExpandedChange = { periodExpanded = !periodExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedPeriod?.label ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Выберите период") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = periodExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),

                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = glassWhite,
                            unfocusedContainerColor = glassWhite,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = glassBorder,
                            unfocusedBorderColor = glassBorder,
                            focusedLabelColor = Color.White.copy(alpha = 0.7f),
                            unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                        ),
                    )
                    ExposedDropdownMenu(
                        expanded = periodExpanded,
                        onDismissRequest = { periodExpanded = false },
                        containerColor = Color(0xFF3177B6).copy(alpha = 0.9f),
                        modifier = Modifier
                            .heightIn(max = 200.dp)
                            .clip(RoundedCornerShape(16.dp))

                    ) {
                        periodsList.forEach { period ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = period.label,
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                onClick = {
                                    selectedPeriod = period
                                    periodExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            // Кнопка запуска
            Button(
                onClick = {
                    if (isFormValid){
                        try {
                            //Формирование ссылки по шаблону Gismeteo
                            val url =
                                "https://www.gismeteo.ru/weather-${selectedCity!!.nameEn}-${selectedCity!!.id}/${selectedPeriod!!.path}/"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        } catch (e: Exception){
                            android.widget.Toast.makeText(context, "Не удалось открыть браузер", android.widget.Toast.LENGTH_SHORT).show()
                        }

                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth().height(64.dp),

                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.25f),
                    contentColor = Color.White),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.5f)
                ),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) {
                    Box(modifier = Modifier.size(24.dp))

                    Text(
                        text = "УЗНАТЬ ПОГОДУ\nНА GISMETEO",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        lineHeight = 18.sp,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "*переход на gismeteo.ru",
                color = Color.White,
                modifier = Modifier.padding(bottom = 64.dp)
            )
        }
    }
}



