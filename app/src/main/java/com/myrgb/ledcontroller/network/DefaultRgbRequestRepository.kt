package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.*
import com.myrgb.ledcontroller.feature.rgbcontroller.RgbSettingsResponse
import retrofit2.HttpException
import java.io.IOException

class DefaultRgbRequestRepository(private val rgbRequestService: RgbRequestService) :
    RgbRequestRepository {

    override suspend fun loadCurrentRgbSettings(esp32: Esp32): RgbSettingsResponse? {
        return try {
            rgbRequestService.getCurrentSettings(esp32.ipAddress).body()
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun turnOn(esp32: Esp32, rgbStrip: RgbStrip) {
        sendRgbRequest(esp32.ipAddress, RgbRequest(onCommandIdentifier, rgbStrip.id))
    }

    override suspend fun turnAllOn(esp32: Esp32) {
        sendRgbRequest(esp32.ipAddress, RgbRequest(onCommandIdentifier))
    }

    override suspend fun turnOff(esp32: Esp32, rgbStrip: RgbStrip) {
        sendRgbRequest(esp32.ipAddress, RgbRequest(offCommandIdentifier, rgbStrip.id))
    }

    override suspend fun turnAllOff(esp32: Esp32) {
        sendRgbRequest(esp32.ipAddress, RgbRequest(offCommandIdentifier))
    }

    override suspend fun setBrightness(esp32: Esp32, brightness: Int) {
        sendRgbRequest(esp32.ipAddress, RgbRequest(brightnessCommandIdentifier, brightness))
    }

    override suspend fun setColor(esp32: Esp32, color: RgbTriplet) {
        sendRgbRequest(esp32.ipAddress, RgbRequest(color.red, color.green, color.blue))
    }

    override suspend fun startRgbShow(esp32: Esp32, speed: Int) {
        sendRgbRequest(esp32.ipAddress, RgbRequest(rgbShowCommandIdentifier, speed))
    }

    override suspend fun setRgbShowSpeed(esp32: Esp32, speed: Int) {
        sendRgbRequest(esp32.ipAddress, RgbRequest(rgbShowCommandIdentifier, speed))
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































