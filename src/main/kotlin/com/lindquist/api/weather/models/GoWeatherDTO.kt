package com.lindquist.api.weather.models

data class GoWeatherDTO(
    val temperature: String
)

fun GoWeatherDTO.toCityWeather() =
    CityWeather(
        temperature = temperature
    )