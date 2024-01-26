package com.example.bt_dev.util

import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.bt_dev.R

class FontProvider {
    companion object {
        fun getCertificateProvider():GoogleFont.Provider{
            return GoogleFont.Provider(
                providerAuthority = "com.google.android.gms.fonts",
                providerPackage = "com.google.android.gms",
                certificates = R.array.com_google_android_gms_fonts_certs
            )
        }
    }
}