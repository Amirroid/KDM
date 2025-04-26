package ir.amirroid.kdm

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.NonInteractivePolicy
import com.jakewharton.mosaic.runMosaicBlocking
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Text
import java.net.URI

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Error: No URL provided.")
        return
    }
    val inputUrl = args.first()

    runMosaicBlocking(
        onNonInteractive = NonInteractivePolicy.Ignore
    ) {
        validateUrl(
            url = inputUrl,
            onValid = {
                Downloading(
                    uri = it,
                )
            },
            onInvalid = {
                Text("Url is invalid", color = Color.Red)
            }
        )
    }
}

@Composable
private inline fun validateUrl(
    url: String,
    onValid: @Composable (URI) -> Unit,
    onInvalid: @Composable () -> Unit,
) {
    runCatching {
        URI(url).apply { toASCIIString() }
    }.onSuccess { onValid.invoke(it) }.onFailure { onInvalid.invoke() }
}