package com.lindquist.api.weather

import com.lindquist.api.weather.models.CityWeather
import com.lindquist.api.weather.models.CityWeatherDTO
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class WeatherService(val client: WeatherClient) {
  fun getWeatherByCity(city: String): Mono<CityWeather> =
      client.getWeather(city)
}