package hr.fer.web2.ticketapp

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import org.springframework.stereotype.Service

@Service
class TicketService(private val ticketRepository: TicketRepository) {

    private val baseAppUrl = "https://web2-projekt1-7x55.onrender.com"

    fun createTicket(vatin: String, firstName: String, lastName: String): QRCodeResponse {
        if (ticketRepository.countByVatin(vatin) >= 3) {
            throw IllegalArgumentException("Maximum 3 tickets allowed for this VAT (OIB)")
        }

        val ticket = Ticket(vatin = vatin, firstName = firstName, lastName = lastName)
        val savedTicket = ticketRepository.save(ticket)

        val ticketUrl = "$baseAppUrl/tickets/details/${savedTicket.id}"

        val qrCodeImage = generateQRCode(ticketUrl)

        return QRCodeResponse(qrCodeImage = qrCodeImage)
    }

    fun generateQRCode(url: String): ByteArray {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix: BitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 250, 250)

        val byteArrayOutputStream = ByteArrayOutputStream()
        ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "PNG", byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }
}

data class QRCodeResponse(
    val qrCodeImage: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QRCodeResponse

        return qrCodeImage.contentEquals(other.qrCodeImage)
    }

    override fun hashCode(): Int {
        return qrCodeImage.contentHashCode()
    }
}
