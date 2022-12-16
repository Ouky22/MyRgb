package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RgbRequestRepository @Inject constructor(private val rgbRequestService: RgbRequestService) {

    suspend fun loadCurrentRgbSettings(ipAddress: String): RgbSettingsResponse? {
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

    suspend fun turnStripOn(ledMicrocontroller: LedMicrocontroller, rgbStrip: RgbStrip) {
        sendRgbRequest(ledMicrocontroller.ipAddress, RgbRequest(onCommandIdentifier, rgbStrip.id))
    }

    suspend fun turnAllStripsOn(ledMicrocontroller: LedMicrocontroller) {
        sendRgbRequest(ledMicrocontroller.ipAddress, RgbRequest(onCommandIdentifier))
    }

    suspend fun turnStripOff(ledMicrocontroller: LedMicrocontroller, rgbStrip: RgbStrip) {
        sendRgbRequest(ledMicrocontroller.ipAddress, RgbRequest(offCommandIdentifier, rgbStrip.id))
    }

    suspend fun turnAllStripsOff(ledMicrocontroller: LedMicrocontroller) {
        sendRgbRequest(ledMicrocontroller.ipAddress, RgbRequest(offCommandIdentifier))
    }

    suspend fun setBrightness(ledMicrocontroller: LedMicrocontroller, brightness: Int) {
        sendRgbRequest(
            ledMicrocontroller.ipAddress,
            RgbRequest(brightnessCommandIdentifier, brightness)
        )
    }

    suspend fun setColor(ledMicrocontroller: LedMicrocontroller, color: RgbTriplet) {
        sendRgbRequest(ledMicrocontroller.ipAddress, RgbRequest(color.red, color.green, color.blue))
    }

    suspend fun startRgbShow(ledMicrocontroller: LedMicrocontroller, speed: Int) {
        sendRgbRequest(ledMicrocontroller.ipAddress, RgbRequest(rgbShowCommandIdentifier, speed))
    }

    suspend fun setRgbShowSpeed(ledMicrocontroller: LedMicrocontroller, speed: Int) {
        sendRgbRequest(ledMicrocontroller.ipAddress, RgbRequest(rgbShowCommandIdentifier, speed))
    }

    suspend fun triggerRgbAlarm(ledMicrocontrollerIpAddress: String) {
        sendRgbRequest(ledMicrocontrollerIpAddress, RgbRequest(rgbAlarmCommandIdentifier))
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































