package com.myrgb.ledcontroller.network

import com.myrgb.ledcontroller.domain.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRgbRequestRepository @Inject constructor(private val rgbRequestService: RgbRequestService) :
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

    override suspend fun turnStripOn(ledMicrocontroller: LedMicrocontroller, rgbStrip: RgbStrip) {
        sendRgbRequest(ledMicrocontroller.ipAddress, RgbRequest(onCommandIdentifier, rgbStrip.id))
    }

    override suspend fun turnAllStripsOn(ledMicrocontroller: LedMicrocontroller) {
        sendRgbRequest(ledMicrocontroller.ipAddress, RgbRequest(onCommandIdentifier))
    }

    override suspend fun turnStripOff(ledMicrocontroller: LedMicrocontroller, rgbStrip: RgbStrip) {
        sendRgbRequest(ledMicrocontroller.ipAddress, RgbRequest(offCommandIdentifier, rgbStrip.id))
    }

    override suspend fun turnAllStripsOff(ledMicrocontroller: LedMicrocontroller) {
        sendRgbRequest(ledMicrocontroller.ipAddress, RgbRequest(offCommandIdentifier))
    }

    override suspend fun setBrightness(ledMicrocontroller: LedMicrocontroller, brightness: Int) {
        sendRgbRequest(
            ledMicrocontroller.ipAddress,
            RgbRequest(brightnessCommandIdentifier, brightness)
        )
    }

    override suspend fun setColor(ledMicrocontroller: LedMicrocontroller, color: RgbTriplet) {
        sendRgbRequest(ledMicrocontroller.ipAddress, RgbRequest(color.red, color.green, color.blue))
    }

    override suspend fun startRgbShow(ledMicrocontroller: LedMicrocontroller, speed: Int) {
        sendRgbRequest(ledMicrocontroller.ipAddress, RgbRequest(rgbShowCommandIdentifier, speed))
    }

    override suspend fun setRgbShowSpeed(ledMicrocontroller: LedMicrocontroller, speed: Int) {
        sendRgbRequest(ledMicrocontroller.ipAddress, RgbRequest(rgbShowCommandIdentifier, speed))
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































