package ru.egoraganin.githubsample.model.data

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.lang.reflect.Type

interface IDataMapper {

    @Throws(JsonSyntaxException::class)
    fun <Model> fromJson(json: String?, modelClass: Class<Model>): Model

    @Throws(JsonSyntaxException::class)
    fun <Model> fromJson(json: String?, modelType: Type): Model

    fun toJson(src: Any?, typeOfSrc: Type): String
}

class GsonDataMapper(private val gson: Gson): IDataMapper {

    override fun <Model> fromJson(json: String?, modelClass: Class<Model>) = gson.fromJson(json, modelClass)

    override fun <Model> fromJson(json: String?, modelType: Type) = gson.fromJson(json, modelType) as Model

    override fun toJson(src: Any?, typeOfSrc: Type) = gson.toJson(src, typeOfSrc)
}