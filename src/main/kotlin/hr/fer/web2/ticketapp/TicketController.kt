package hr.fer.web2.ticketapp

import java.util.UUID
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tickets")
class TicketController(private val ticketService: TicketService) {

    @PostMapping("/generate")
    fun generateTicket(@RequestBody request: TicketRequest): ResponseEntity<ByteArray> {
        val qrCodeResponse = ticketService.createTicket(request.vatin, request.firstName, request.lastName)

        val headers = HttpHeaders().apply {
            contentType = MediaType.IMAGE_PNG
            set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"qr-code.png\"")
        }

        return ResponseEntity(qrCodeResponse.qrCodeImage, headers, HttpStatus.CREATED)
    }

    @GetMapping("/details/{id}", produces = [MediaType.IMAGE_PNG_VALUE])
    fun getTicketDetails(@PathVariable ticketId: UUID): String {
        return ""
    }
}

data class TicketRequest(val vatin: String, val firstName: String, val lastName: String)