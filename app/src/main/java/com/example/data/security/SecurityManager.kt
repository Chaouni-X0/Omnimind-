package com.example.omnimind.data.security

import android.util.Base64
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Encrypts API keys with AES-GCM backed by Android Keystore. Legacy XOR values
 * remain readable so existing installations can migrate without losing keys.
 */
class SecurityManager {

    private val obfuscationKey = "OmniMindLocalKey".toByteArray(Charsets.UTF_8)

    fun encrypt(data: String): String {
        if (data.isBlank()) return ""
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        val payload = cipher.iv + cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))
        return "$VERSION_PREFIX${Base64.encodeToString(payload, Base64.NO_WRAP)}"
    }

    fun decrypt(data: String): String {
        return try {
            if (data.startsWith(VERSION_PREFIX)) {
                val payload = Base64.decode(data.removePrefix(VERSION_PREFIX), Base64.NO_WRAP)
                if (payload.size <= IV_SIZE_BYTES) return ""
                val cipher = Cipher.getInstance(TRANSFORMATION)
                cipher.init(
                    Cipher.DECRYPT_MODE,
                    getOrCreateKey(),
                    GCMParameterSpec(128, payload.copyOfRange(0, IV_SIZE_BYTES))
                )
                String(cipher.doFinal(payload.copyOfRange(IV_SIZE_BYTES, payload.size)), StandardCharsets.UTF_8)
            } else {
                decryptLegacy(data)
            }
        } catch (e: Exception) {
            ""
        }
    }

    fun validateToken(token: String): Boolean {
        return token.isNotBlank() && token.length >= 8
    }

    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER).apply { load(null) }
        (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }
        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER).run {
            init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
            generateKey()
        }
    }

    private fun decryptLegacy(data: String): String {
        val input = Base64.decode(data, Base64.NO_WRAP)
        return String(ByteArray(input.size) { index ->
            (input[index].toInt() xor obfuscationKey[index % obfuscationKey.size].toInt()).toByte()
        }, Charsets.UTF_8)
    }

    private companion object {
        const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        const val KEY_ALIAS = "omnimind_api_keys_v1"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val VERSION_PREFIX = "v2:"
        const val IV_SIZE_BYTES = 12
    }
}
