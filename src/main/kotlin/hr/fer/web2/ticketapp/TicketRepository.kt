package hr.fer.web2.ticketapp

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface TicketRepository : JpaRepository<Ticket, UUID> {
    fun countByVatin(vatin: String): Long  // Check how many tickets a user has created using their OIB
}