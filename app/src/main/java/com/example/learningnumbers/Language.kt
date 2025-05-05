package com.example.learningnumbers

import android.os.Parcel
import android.os.Parcelable
import java.util.Locale


data class Language(val label: String = "", val locale: Locale = Locale.US) : Parcelable {
    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(label)
        dest.writeSerializable(locale)
    }

    companion object CREATOR : Parcelable.Creator<Language> {
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