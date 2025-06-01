package com.example.learningnumbers

import android.os.Parcel
import android.os.Parcelable
import java.util.Locale


data class Language(val label: String = "English", val locale: Locale = Locale.US, val code: String = "us") : Parcelable {

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(label)
        dest.writeSerializable(locale)
    }

    companion object CREATOR : Parcelable.Creator<Language> {
        val ENGLISH = Language("English", Locale.US, code = "us")
        val POLISH = Language("Polish", Locale("pl"), code = "pl")

        override fun createFromParcel(parcel: Parcel): Language {
            val label = parcel.readString() ?: ""
            val locale = parcel.readParcelable(
                ClassLoader.getSystemClassLoader(),
                Locale::class.java
            ) as Locale
            return Language(label, locale)
        }

        override fun newArray(size: Int): Array<Language> {
            return emptyArray()
        }
    }
}

fun toLocale(lang: String, country: String? = null): Locale? {
    return try {
        country?.let { Locale(lang, it) } ?: Locale(lang)
    } catch (e: NullPointerException) {
        null
    }
}