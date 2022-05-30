package com.lindquist.api.config

import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.jooq.impl.DefaultExecuteListenerProvider
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource

@Configuration
class InitialConfig(private val dataSource: DataSource) {

  @Bean
  fun connectionProvider(): DataSourceConnectionProvider {
    return DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource))
  }

  @Bean
  fun dsl(): DefaultDSLContext {
    return DefaultDSLContext(configuration())
  }

  fun configuration(): org.jooq.Configuration {
    return DefaultConfiguration()
      .set(connectionProvider())
      .set(DefaultExecuteListenerProvider(JooqExceptionTranslator()))
  }
}