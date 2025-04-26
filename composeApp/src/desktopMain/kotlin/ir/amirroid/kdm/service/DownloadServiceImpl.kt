package ir.amirroid.kdm.service

import ir.amirroid.kdm.data.ReadWriteState
import ir.amirroid.kdm.service.DownloadService.Companion.CONNECT_TIMEOUT
import ir.amirroid.kdm.service.DownloadService.Companion.READ_TIMEOUT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URI

class DownloadServiceImpl() : DownloadService {
    override suspend fun download(uri: URI) = callbackFlow {
        withContext(Dispatchers.IO) {
            try {
                val connection = uri.toURL().openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = CONNECT_TIMEOUT
                connection.readTimeout = READ_TIMEOUT
                connection.connect()

                val totalLength = connection.contentLengthLong
                val inputStream: InputStream = connection.inputStream

                val fileName = uri.path.substringAfterLast("/")
                    .substringBefore("?")
                    .takeIf { it.isNotEmpty() }
                    ?: (uri.host + "_file")

                if (connection.responseCode in 200..299) {
                    val file = File(fileName)
                    val outputStream = file.outputStream()
                    var bytesDownloaded: Long = 0
                    val buffer = ByteArray(4096)
                    var bytesRead: Int

                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                        bytesDownloaded += bytesRead
                        trySend(
                            ReadWriteState.Progress(
                                fileName = fileName,
                                contentSize = totalLength,
                                progress = (bytesDownloaded / totalLength.toFloat()).coerceIn(
                                    0f,
                                    1f
                                )
                            )
                        )
                    }

                    outputStream.close()
                    trySend(
                        ReadWriteState.Success(
                            fileName = fileName,
                        )
                    )
                } else {
                    val errorMessage = connection.responseMessage
                    val errorCode = connection.responseCode
                    trySend(ReadWriteState.Error(message = errorMessage, errorCode = errorCode))
                }
            } catch (ـ: Exception) {
                trySend(ReadWriteState.Error())
            }
        }
        awaitClose { trySend(ReadWriteState.Idle) }
    }
}