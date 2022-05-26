package com.lindquist.api.weather

import com.lindquist.api.weather.models.CityWeather
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Subject

class WeatherServiceSpec extends Specification {
  
  WeatherClient weatherClient = Mock()
  
  @Subject
  WeatherService weatherService = new WeatherService(weatherClient)
  
  def "GetWeatherByCity"() {
    given:
    String city = "Minneapolis"
    CityWeather cityWeather = new CityWeather(
        "98"
    )

    when:
    weatherService.getWeatherByCity(city)
    
    then:
    1 * weatherClient.getWeather(city) >> Mono.just(cityWeather)
  }
}
