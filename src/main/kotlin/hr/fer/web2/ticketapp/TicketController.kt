package hr.fer.web2.ticketapp

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/tickets")
class TicketController(private val ticketService: TicketService) {

    @PostMapping("/create")
    fun createTicket(@Valid @RequestBody request: TicketRequest, bindingResult: BindingResult): ResponseEntity<Any> {
        if (bindingResult.hasErrors()) {
            val errors = bindingResult.allErrors.map { it.defaultMessage ?: "Invalid input" }
            return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
        }

        try {
            val qrCodeResponse = ticketService.createTicket(request.vatin, request.firstName, request.lastName)

            val headers = HttpHeaders().apply {
                contentType = MediaType.IMAGE_PNG
                set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"qr-code.png\"")
            }

            return ResponseEntity(qrCodeResponse.qrCodeImage, headers, HttpStatus.CREATED)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/details/{ticketId}")
    fun getTicketDetails(@PathVariable ticketId: UUID, model: Model, @AuthenticationPrincipal user: OidcUser): String {
        val ticket = ticketService.getTicketDetails(ticketId)
        val name = user.fullName ?: user.preferredUsername

        model.addAttribute("ticket", ticket)
        model.addAttribute("name", name)

        return "ticket-details"
    }

    @GetMapping("/total")
    fun getTotalTickets(model: Model): String {
        val totalTickets = ticketService.getTotalTickets()
        model.addAttribute("totalTickets", totalTickets)
        return "total-tickets"
    }
}

data class TicketRequest(
    @field:NotBlank(message = "VATIN is required")
    @field:Size(min = 11, max = 11, message = "VATIN must be 11 characters long")
    val vatin: String,

    @field:NotBlank(message = "First name is required")
    val firstName: String,

    @field:NotBlank(message = "Last name is required")
    val lastName: String
)