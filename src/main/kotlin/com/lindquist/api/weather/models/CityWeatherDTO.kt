package com.lindquist.api.weather.models

data class CityWeatherDTO(
        val temperature: String
)

fun CityWeatherDTO.toCityWeather() =
        CityWeather(
                temperature = temperature
        )