
package com.example

import io.vertx.core.json.JsonObject
import io.vertx.core.net.JksOptions
import io.vertx.ext.shell.ShellServer
import io.vertx.ext.shell.ShellServiceOptions
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.ext.shell.command.Command
import io.vertx.ext.shell.command.base.Help
import io.vertx.ext.shell.term.SSHTermOptions
import io.vertx.ext.shell.term.TermServer
import org.litote.kmongo.json




//ssh -p 4000 logger@localhost
class ShellVerticle :  CoroutineVerticle() {
    @Throws(Exception::class)
    override suspend fun start() {
        println("Starting shell")
        val shellOptions = ShellServiceOptions()
            .apply { welcomeMessage = "\nWelcome to Logging Shell Shell\n\n" }
        val shellServer = ShellServer.create(vertx, shellOptions).apply {
            val authProps = JsonObject()
                .put("properties_path", "classpath:shell.properties")
            println(authProps.json)
            val sshTermServer = TermServer.createSSHTermServer(vertx, SSHTermOptions().apply {
                host = "localhost"
                port = 5001
                setKeyPairOptions(JksOptions().setPath("vertx-shell-1.8.jks").setPassword("123456"))
                authOptions = JsonObject().put("provider", "shiro").put("config", authProps)
            })

            registerTermServer(sshTermServer)
            listen { res ->
                if (res.failed())
                    res.cause().printStackTrace()
                else
                    println("Logging shell started successfully")
            }

        }
        shellServer.registerCommandResolver { listOf(Help::class.java).map { Command.create(vertx, it) } }
        println("Commands Registered")

    }
}
