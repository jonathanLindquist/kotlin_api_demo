package com.lindquist.api.weather.models

data class CityWeather(
        val temperature: String
)

fun CityWeather.toCityWeatherDTO() =
        CityWeatherDTO(
                temperature = temperature
        )