package ir.amirroid.kdm.network

import ir.amirroid.kdm.service.DownloadService
import ir.amirroid.kdm.service.DownloadServiceImpl

object NetworkClientProvider {
    fun provideService(): DownloadService = DownloadServiceImpl()
}