package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.*
import retrofit2.HttpException
import java.io.IOException

class DefaultRgbRequestRepository(private val rgbRequestService: RgbRequestService) :
    RgbRequestRepository {

    override suspend fun loadCurrentRgbSettings(ipAddress: String): RgbSettingsResponse? {
        return try {
            rgbRequestService.getCurrentSettings(ipAddress).body()
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun turnStripOn(esp32Microcontroller: Esp32Microcontroller, rgbStrip: RgbStrip) {
        sendRgbRequest(esp32Microcontroller.ipAddress, RgbRequest(onCommandIdentifier, rgbStrip.id))
    }

    override suspend fun turnAllStripsOn(esp32Microcontroller: Esp32Microcontroller) {
        sendRgbRequest(esp32Microcontroller.ipAddress, RgbRequest(onCommandIdentifier))
    }

    override suspend fun turnStripOff(esp32Microcontroller: Esp32Microcontroller, rgbStrip: RgbStrip) {
        sendRgbRequest(esp32Microcontroller.ipAddress, RgbRequest(offCommandIdentifier, rgbStrip.id))
    }

    override suspend fun turnAllStripsOff(esp32Microcontroller: Esp32Microcontroller) {
        sendRgbRequest(esp32Microcontroller.ipAddress, RgbRequest(offCommandIdentifier))
    }

    override suspend fun setBrightness(esp32Microcontroller: Esp32Microcontroller, brightness: Int) {
        sendRgbRequest(esp32Microcontroller.ipAddress, RgbRequest(brightnessCommandIdentifier, brightness))
    }

    override suspend fun setColor(esp32Microcontroller: Esp32Microcontroller, color: RgbTriplet) {
        sendRgbRequest(esp32Microcontroller.ipAddress, RgbRequest(color.red, color.green, color.blue))
    }

    override suspend fun startRgbShow(esp32Microcontroller: Esp32Microcontroller, speed: Int) {
        sendRgbRequest(esp32Microcontroller.ipAddress, RgbRequest(rgbShowCommandIdentifier, speed))
    }

    override suspend fun setRgbShowSpeed(esp32Microcontroller: Esp32Microcontroller, speed: Int) {
        sendRgbRequest(esp32Microcontroller.ipAddress, RgbRequest(rgbShowCommandIdentifier, speed))
    }

    private suspend fun sendRgbRequest(ipAddress: String, rgbRequest: RgbRequest) {
        try {
            rgbRequestService.sendRgbRequest(ipAddress, rgbRequest)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: HttpException) {
            e.printStackTrace()
        }
    }
}































