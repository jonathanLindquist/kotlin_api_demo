package com.lindquist.api.weather

import com.lindquist.api.weather.models.CityWeather
import com.lindquist.api.weather.models.CityWeatherDTO
import com.lindquist.api.weather.models.toCityWeatherDTO
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class WeatherService(val client: WeatherClient) {
  fun getWeatherByCity(city: String): Mono<CityWeatherDTO> =
      client.getWeather(city)
          .mapNotNull { it?.toCityWeatherDTO() }
}