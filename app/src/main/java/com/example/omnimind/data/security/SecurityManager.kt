package com.example.omnimind.data.security

import android.util.Base64

/**
 * تشفير خفيف لمفاتيح الـ API قبل حفظها في قاعدة البيانات المحلية.
 *
 * ملاحظة: هذا تعتيم بسيط (XOR + Base64) وليس تشفيرًا معتمدًا على Android Keystore.
 * يكفي لمنع ظهور المفتاح كنص صريح داخل قاعدة البيانات، لكن للاستخدام الإنتاجي
 * يُنصح بالترقية إلى androidx.security-crypto (EncryptedSharedPreferences / Tink)
 * لربط التشفير بمفتاح مخزّن فعليًا في Android Keystore.
 */
class SecurityManager {

    private val obfuscationKey = "OmniMindLocalKey".toByteArray(Charsets.UTF_8)

    fun encrypt(data: String): String {
        val input = data.toByteArray(Charsets.UTF_8)
        val output = ByteArray(input.size)
        for (i in input.indices) {
            output[i] = (input[i].toInt() xor obfuscationKey[i % obfuscationKey.size].toInt()).toByte()
        }
        return Base64.encodeToString(output, Base64.NO_WRAP)
    }

    fun decrypt(data: String): String {
        return try {
            val input = Base64.decode(data, Base64.NO_WRAP)
            val output = ByteArray(input.size)
            for (i in input.indices) {
                output[i] = (input[i].toInt() xor obfuscationKey[i % obfuscationKey.size].toInt()).toByte()
            }
            String(output, Charsets.UTF_8)
        } catch (e: Exception) {
            ""
        }
    }

    fun validateToken(token: String): Boolean {
        return token.isNotBlank() && token.length >= 8
    }
}
