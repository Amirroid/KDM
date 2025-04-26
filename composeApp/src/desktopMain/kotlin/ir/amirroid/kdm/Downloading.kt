package ir.amirroid.kdm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.layout.KeyEvent
import com.jakewharton.mosaic.layout.background
import com.jakewharton.mosaic.layout.onKeyEvent
import com.jakewharton.mosaic.layout.padding
import com.jakewharton.mosaic.layout.size
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Alignment
import com.jakewharton.mosaic.ui.Box
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Row
import com.jakewharton.mosaic.ui.Spacer
import com.jakewharton.mosaic.ui.Text
import ir.amirroid.kdm.data.formatErrorMessage
import ir.amirroid.kdm.data.onError
import ir.amirroid.kdm.data.onIdle
import ir.amirroid.kdm.data.onProgress
import ir.amirroid.kdm.data.onSuccessDownload
import ir.amirroid.kdm.state.DownloaderState
import kotlinx.coroutines.delay
import java.net.URI
import kotlin.system.exitProcess

@Composable
fun Downloading(
    uri: URI,
) {
    val scope = rememberCoroutineScope()
    val downloader = remember {
        DownloaderState(scope).apply {
            start(uri)
        }
    }
    val currentState = downloader.currentState

    currentState
        .onIdle {
            DynamicTextWithDots("Receiving data")
        }
        .onSuccessDownload { fileName ->
            Column {
                Text(
                    "File Name: $fileName",
                    modifier = Modifier.padding(top = 1),
                    color = Color.Green
                )
                Text("Completed", color = Color.Green)
                Text(
                    "Press any key to exit.",
                    modifier = Modifier.onKeyEvent { _ ->
                        exitProcess(0)
                    })
            }
        }
        .onProgress { fileName, contentSize, progress ->
            Column {
                DynamicTextWithDots("Download in progress", color = Color.Green)
                Text(
                    "File Name: $fileName",
                    modifier = Modifier.padding(top = 1),
                    color = Color.Yellow
                )
                Text(
                    "Content Size: ${formatSize(contentSize)}",
                    modifier = Modifier.padding(top = 1),
                    color = Color.Yellow
                )
                Text(
                    "Downloaded: ${formatSize(contentSize.times(progress))}",
                    color = Color.Yellow
                )
                ProgressBar(progress)
            }
        }
        .onError {
            Text(it.formatErrorMessage(), color = Color.Red)
        }
}

@Composable
fun DynamicTextWithDots(message: String, color: Color = Color.White) {
    var dotCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            dotCount = (dotCount + 1) % 4
        }
    }

    val dots = ".".repeat(dotCount)

    Text("$message$dots", color = color)
}


@Composable
fun ProgressBar(progressFraction: Float) {
    val totalBarWidth = 50
    val filledBarWidth = (progressFraction * totalBarWidth).toInt()
    val remainingBarWidth = totalBarWidth - filledBarWidth

    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(top = 1)) {
        Row {
            Spacer(Modifier.background(Color.Green).size(filledBarWidth, 1))
            Spacer(Modifier.background(Color.White).size(remainingBarWidth, 1))
        }
        Text(
            String.format("%.1f", progressFraction.times(100)) + "%",
            color = Color.Black,
            modifier = Modifier.padding(left = 1)
        )
    }
}