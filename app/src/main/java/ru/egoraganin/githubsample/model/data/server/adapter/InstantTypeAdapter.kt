package ru.egoraganin.githubsample.model.data.server.adapter

import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.threeten.bp.Instant
import org.threeten.bp.format.DateTimeParseException

class InstantTypeAdapter : TypeAdapter<Instant>() {

    override fun write(out: JsonWriter, value: Instant?) {
        out.value(value?.toString())
    }

    override fun read(`in`: JsonReader): Instant? {
        var value: Instant? = null

        if (`in`.peek() != JsonToken.NULL) {
            try {
                value = Instant.parse(`in`.nextString())
            } catch (e: DateTimeParseException) {
                throw JsonSyntaxException(e)
            }
        } else {
            `in`.nextNull()
        }

        return value
    }
}