package com.example
import io.vertx.core.*

suspend fun main(args: Array<String>) {
    val vertx: Vertx = Vertx.vertx()
    println("Going to start Shell Verticle")
    vertx.deployVerticle(ShellVerticle())

    println("Going to start Logger Verticle")
}
