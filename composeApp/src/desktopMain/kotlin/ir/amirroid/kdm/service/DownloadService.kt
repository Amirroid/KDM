package ir.amirroid.kdm.service

import ir.amirroid.kdm.data.ReadWriteState
import kotlinx.coroutines.flow.Flow
import java.net.URI

interface DownloadService {
    suspend fun download(uri: URI): Flow<ReadWriteState>


    companion object {
        const val READ_TIMEOUT = 60_000_000
        const val CONNECT_TIMEOUT = 60_000_000
    }
}