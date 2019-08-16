package ru.egoraganin.githubsample.model.data.server

data class SequenceParameters<Type>(val params: Array<Type>) {

    override fun toString(): String {
        val stringBuilder = StringBuilder()

        params.forEachIndexed { index, param ->
            stringBuilder.append(param.toString())
            if (index != params.lastIndex) {
                stringBuilder.append(',')
            }
        }

        return stringBuilder.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SequenceParameters<*>

        if (!params.contentEquals(other.params)) return false

        return true
    }

    override fun hashCode(): Int {
        return params.contentHashCode()
    }
}
