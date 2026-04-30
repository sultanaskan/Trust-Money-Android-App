package com.wnapp.trustmoney.utils

// ফাইল: com.wnapp.trustmoney.util.CoilUtils.kt
import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder

fun getSvgImageLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory()) // এটি SVG ফাইল পড়ার ক্ষমতা যোগ করে
        }
        .build()
}