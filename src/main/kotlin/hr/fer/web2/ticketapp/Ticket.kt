package hr.fer.web2.ticketapp

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Ticket(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: UUID? = null,
    val vatin: String,
    val firstName: String,
    val lastName: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)