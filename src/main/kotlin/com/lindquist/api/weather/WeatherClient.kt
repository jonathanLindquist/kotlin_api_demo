package com.lindquist.api.weather

import com.lindquist.api.weather.models.CityWeather
import com.lindquist.api.weather.models.GoWeatherDTO
import com.lindquist.api.weather.models.toCityWeather
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.util.concurrent.TimeUnit

@Component
class WeatherClient(val client: WebClient = webClient()) {
  fun getWeather(city: String): Mono<CityWeather> =
    client.post()
      .uri("https://goweather.herokuapp.com/weather/${city}")
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .accept(MediaType.APPLICATION_JSON)
      .acceptCharset(StandardCharsets.UTF_8)
      .retrieve()
      .onStatus(HttpStatusCode::is4xxClientError) { response ->
        if (response.statusCode() == HttpStatusCode.valueOf(403)) {
          Mono.error(Exception())
        } else {
          Mono.error(Exception())
        }
      }.toEntity(GoWeatherDTO::class.java)
      .mapNotNull { it.body?.toCityWeather() }
}

fun webClient(): WebClient =
  WebClient.builder()
    .clientConnector(ReactorClientHttpConnector(httpClient()))
    .build()

fun httpClient(): HttpClient =
  HttpClient.create()
    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
    .responseTimeout((Duration.ofMillis(5000)))
    .doOnConnected { conn ->
      conn.addHandlerLast(ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
        .addHandlerLast(WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
    }