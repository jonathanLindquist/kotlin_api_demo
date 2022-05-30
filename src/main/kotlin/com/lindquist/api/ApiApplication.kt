package com.lindquist.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement


@SpringBootApplication
@EnableTransactionManagement
class ApiApplication

fun main(args: Array<String>) {
  runApplication<ApiApplication>(*args)
}