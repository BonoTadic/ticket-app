package hr.fer.web2.ticketapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TicketAppApplication

fun main(args: Array<String>) {
	runApplication<TicketAppApplication>(*args)
}
