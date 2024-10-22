package hr.fer.web2.ticketapp

import java.util.UUID
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/tickets")
class TicketController(private val ticketService: TicketService) {

    @PostMapping("/create")
    fun createTicket(@RequestBody request: TicketRequest): ResponseEntity<ByteArray> {
        val qrCodeResponse = ticketService.createTicket(request.vatin, request.firstName, request.lastName)

        val headers = HttpHeaders().apply {
            contentType = MediaType.IMAGE_PNG
            set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"qr-code.png\"")
        }

        return ResponseEntity(qrCodeResponse.qrCodeImage, headers, HttpStatus.CREATED)
    }

    @GetMapping("/details/{ticketId}")
    fun getTicketDetails(@PathVariable ticketId: UUID, model: Model): String {
        val ticket = ticketService.getTicketDetails(ticketId)
        model.addAttribute("ticket", ticket)

        return "ticket-details"
    }
}

data class TicketRequest(val vatin: String, val firstName: String, val lastName: String)