package ir.amirroid.kdm

fun formatSize(sizeInBytes: Long): String {
    val sizeInKb = sizeInBytes / 1024
    val sizeInMb = sizeInKb / 1024
    val sizeInGb = sizeInMb / 1024
    val sizeInTb = sizeInGb / 1024

    return when {
        sizeInTb > 0 -> String.format("%.2f TB", sizeInTb.toDouble())
        sizeInGb > 0 -> String.format("%.2f GB", sizeInGb.toDouble())
        sizeInMb > 0 -> String.format("%.2f MB", sizeInMb.toDouble())
        sizeInKb > 0 -> String.format("%.2f KB", sizeInKb.toDouble())
        else -> "$sizeInBytes B"
    }
}

fun formatSize(sizeInBytes: Float): String {
    val sizeInKb = sizeInBytes / 1024
    val sizeInMb = sizeInKb / 1024
    val sizeInGb = sizeInMb / 1024
    val sizeInTb = sizeInGb / 1024

    return when {
        sizeInTb >= 1 -> String.format("%.2f TB", sizeInTb)
        sizeInGb >= 1 -> String.format("%.2f GB", sizeInGb)
        sizeInMb >= 1 -> String.format("%.2f MB", sizeInMb)
        sizeInKb >= 1 -> String.format("%.2f KB", sizeInKb)
        else -> String.format("%.2f B", sizeInBytes)
    }
}