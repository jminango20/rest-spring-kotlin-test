package br.com.juan.testcontainers

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.lifecycle.Startables
import java.util.stream.Stream


@ContextConfiguration(initializers = [AbstractIntegrationClass.Initializer::class])
open class AbstractIntegrationClass {

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            startContainers()
            val environment = applicationContext.environment
            val testContainers = MapPropertySource(
                "testcontainers", createConnectionConfiguration()
            )
        }


        companion object{

            private var mysql: MySQLContainer<*> = MySQLContainer("mysql:8.0.28")

            private fun createConnectionConfiguration(): MutableMap<String, Any> {
                return java.util.Map.of(
                    "spring.datasource.url", mysql.jdbcUrl,
                    "spring.datasource.username", mysql.username,
                    "spring.datasource.password", mysql.password
                )
            }

            private fun startContainers() {
                Startables.deepStart(Stream.of(mysql)).join()
            }
        }


    }

}