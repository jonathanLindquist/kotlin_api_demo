package com.lindquist.api.config

import io.r2dbc.spi.ConnectionFactory
import org.jooq.DSLContext
import org.jooq.impl.DSL

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JooqConfig(private val connectionFactory: ConnectionFactory) {

  @Bean
  fun jooqDslContext(): DSLContext {
    return DSL.using(connectionFactory).dsl()
  }
}