import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        desktopMain.dependencies {
            implementation(libs.mosaic)
        }
    }
}


compose.desktop {
    application {
        mainClass = "ir.amirroid.kdm.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ir.amirroid.kdm"
            packageVersion = "1.0.0"
        }
    }
}