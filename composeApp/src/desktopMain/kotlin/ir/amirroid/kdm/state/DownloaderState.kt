package ir.amirroid.kdm.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ir.amirroid.kdm.data.ReadWriteState
import ir.amirroid.kdm.network.NetworkClientProvider
import ir.amirroid.kdm.service.DownloadService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.net.URI

@Stable
class DownloaderState(
    private val scope: CoroutineScope,
    private val service: DownloadService = NetworkClientProvider.provideService()
) {
    private val mutex = Mutex()
    var currentState by mutableStateOf<ReadWriteState>(ReadWriteState.Idle)
        private set

    fun start(uri: URI) = scope.launch(Dispatchers.IO) {
        service.download(uri).collectLatest { state ->
            mutex.withLock {
                currentState = state
            }
        }
    }
}