package ru.egoraganin.githubsample.model.data

sealed class Result<out Payload, out Error> {
    data class Success<out Payload>(val payload: Payload) : Result<Payload, Nothing>()
    data class Failure<out Error>(val error: Error) : Result<Nothing, Error>()
}