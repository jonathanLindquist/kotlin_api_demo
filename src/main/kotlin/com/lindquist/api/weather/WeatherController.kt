package com.lindquist.api.weather

import com.lindquist.api.weather.models.CityWeatherDTO
import com.lindquist.api.weather.models.toCityWeatherDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController("/api/v1/weather")
class WeatherController(val weatherService: WeatherService) {
  @GetMapping("/city/{city}")
  fun getWeatherByCity(@PathVariable city: String, @RequestParam item: String?): Mono<CityWeatherDTO> {
    return weatherService
        .getWeatherByCity(city)
        .map { it.toCityWeatherDTO() }
  }
}