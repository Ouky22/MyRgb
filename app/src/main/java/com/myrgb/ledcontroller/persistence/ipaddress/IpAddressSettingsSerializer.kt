package com.myrgb.ledcontroller.persistence.ipaddress

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.myrgb.ledcontroller.IpAddressSettings
import java.io.InputStream
import java.io.OutputStream

/**
 * This class tells DataStore how to read and write the IpAddressSettings
 */
@Suppress("BlockingMethodInNonBlockingContext")
object IpAddressSettingsSerializer : Serializer<IpAddressSettings> {
    override val defaultValue: IpAddressSettings = IpAddressSettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): IpAddressSettings {
        try {
            return IpAddressSettings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: IpAddressSettings, output: OutputStream) = t.writeTo(output)
}