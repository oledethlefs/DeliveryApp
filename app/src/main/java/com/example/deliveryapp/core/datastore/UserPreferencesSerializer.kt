package com.example.deliveryapp.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject


/**
 * Serializer for the [UserPreferences] proto object.
 *
 * This class defines how to read and write [UserPreferences] data using Protocol Buffers.
 * It is used by the DataStore to persist and retrieve user-specific settings and preferences.
 *
 * @see Serializer
 */
class UserPreferencesSerializer @Inject constructor() : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: UserPreferences,
        output: OutputStream) = t.writeTo(output)
}