package com.vikas.mobile.mynotes.data

import androidx.room.TypeConverter
import com.vikas.mobile.authandcrypto.Crypto
import com.vikas.mobile.mynotes.data.entity.MaskedData

/**
 * Type converters to allow Room to reference complex data types.
 *
 * Our main intention is to apply cryptography before data get stores to db and Ui receives the data (in readable form)
 * So we used converters to encrypt/decrypt before data flows back and forth.
 *
 * Room can only store primitive types, so we're converting complex object to String when providing complext object to db
 * UI will receive complex (MaskedData) object, so converting from String to complex object
 */
class Converters {

    // To UI <--- Room db
    @TypeConverter
    fun maskData(data: String): MaskedData {
        return MaskedData(Crypto.decrypt(data))
    }

    // From UI --> Room db
    @TypeConverter
    fun unMaskData(maskedData: MaskedData): String {
        return Crypto.encrypt(maskedData.content)
    }

}