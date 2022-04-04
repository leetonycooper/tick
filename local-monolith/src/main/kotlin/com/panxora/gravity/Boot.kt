package com.panxora.gravity
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class Boot {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<Boot>(*args)
        }
    }
}
