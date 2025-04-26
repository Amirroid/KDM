package ir.amirroid.kdm.data

sealed class ReadWriteState {
    data class Error(val message: String? = null, val errorCode: Int? = null) : ReadWriteState()
    data class Progress(val fileName: String, val contentSize: Long, val progress: Float) :
        ReadWriteState()

    data class Success(val fileName: String) : ReadWriteState()
    data object Idle : ReadWriteState()
}

inline fun ReadWriteState.onSuccessDownload(action: (String) -> Unit): ReadWriteState {
    return when (this) {
        is ReadWriteState.Success -> {
            action(fileName)
            this
        }

        else -> this
    }
}

inline fun ReadWriteState.onIdle(action: () -> Unit): ReadWriteState {
    return when (this) {
        is ReadWriteState.Idle -> {
            action()
            this
        }

        else -> this
    }
}


inline fun ReadWriteState.onSuccessWrite(action: () -> Unit): ReadWriteState {
    return when (this) {
        is ReadWriteState.Success -> {
            action()
            this
        }

        else -> this
    }
}

inline fun ReadWriteState.onError(action: (ReadWriteState.Error) -> Unit): ReadWriteState {
    return when (this) {
        is ReadWriteState.Error -> {
            action(this)
            this
        }

        else -> this
    }
}

inline fun ReadWriteState.onProgress(
    action: (
        fileName: String,
        contentSize: Long,
        progress: Float
    ) -> Unit
): ReadWriteState {
    return when (this) {
        is ReadWriteState.Progress -> {
            action(fileName, contentSize, progress)
            this
        }

        else -> this
    }
}

fun ReadWriteState.Error.formatErrorMessage(): String {
    return buildString {
        append("Error occurred")
        errorCode?.let { append(" (Code: $it)") }
        message?.let { append(": $it") }
        if (errorCode == null && message == null) append(": Unknown error")
    }
}