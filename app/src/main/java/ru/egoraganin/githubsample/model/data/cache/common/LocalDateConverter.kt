package ru.egoraganin.githubsample.model.data.cache.common

import androidx.room.TypeConverter
import org.threeten.bp.Instant
import org.threeten.bp.format.DateTimeParseException
import timber.log.Timber

class InstantConverter {

    @TypeConverter
    fun fromInstant(instant: Instant): String {
        return instant.toString()
    }

    @TypeConverter
    fun toInstant(date: String): Instant? {
        return try {
            Instant.parse(date)
        } catch (e: DateTimeParseException) {
            Timber.e(e, "Instant converting is failed")
            null
        }
    }
}