package com.meria.playtaylermel.util

import android.content.Context
import android.security.keystore.KeyProperties
import android.security.keystore.KeyProperties.KEY_ALGORITHM_AES
import android.util.Base64
import android.util.Log
import com.meria.playtaylermel.BuildConfig
import com.meria.playtaylermel.util.Utils.getDensity
import com.meria.playtaylermel.util.Utils.getHeight
import com.meria.playtaylermel.util.Utils.getWidth
import okhttp3.*
import okio.Buffer
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object AesEncrypt {
    private var keyRemoteConfig: String? = null
    private val buffer = Buffer()
    private val TEXT_PLAIN = MediaType.parse("text/plain")
    private val JSON =
        MediaType.parse("application/json; charset=utf-8")

    private const val TAG_LENGTH = 128
    private const val IV_LENGTH = 16
    private const val IV_DEFAULT = BuildConfig.IV_AES


    // encriptado simple
    fun decryptCBC(str: String?, key: String): String {
        return try {
            val textBytes = Base64.decode(str, Base64.DEFAULT)
            val iv = IV_DEFAULT.toByteArray()
            val cipher = Cipher.getInstance(CYPHER_CBC_INSTANCE)
            val newKey = SecretKeySpec(
                key.toByteArray(StandardCharsets.UTF_8),
                KeyProperties.KEY_ALGORITHM_AES
            )
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)
            val decryptedText = cipher.doFinal(textBytes)
            String(decryptedText)
        } catch (ex: Exception) {
            ""
        }
    }

    fun encryptCBC(str: String, key: String): String {
        return try {
            val iv = IV_DEFAULT.toByteArray()
            val cipher = Cipher.getInstance(CYPHER_CBC_INSTANCE)
            val textBytes = str.toByteArray(StandardCharsets.UTF_8)
            val keySpec = SecretKeySpec(
                key.toByteArray(StandardCharsets.UTF_8),
                KeyProperties.KEY_ALGORITHM_AES
            )
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            val encrypt = cipher.doFinal(textBytes)
            Base64.encodeToString(encrypt, Base64.DEFAULT)
        } catch (ex: Exception) {
            ""
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // encriptado seguridad media

    fun decryptCBCHexadecimal(str: String, key: String): String {
        return try {
            val ivText = str.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            val encrypText =
                str.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val iv = decodeHexString(ivText)
            val cipher = Cipher.getInstance(CYPHER_CBC_INSTANCE)
            val newKey = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), KEY_ALGORITHM_AES)
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)
            val decryptedText = cipher.doFinal(decodeHexString(encrypText))
            String(decryptedText)
        } catch (ex: Exception) {
            ""
        }
    }

    fun encryptCBCHexadecimal(text: String, key: String): String {
        return try {
            val secureRandom = SecureRandom()
            val iv = ByteArray(IV_LENGTH)
            secureRandom.nextBytes(iv)
            val cipher = Cipher.getInstance(CYPHER_CBC_INSTANCE)
            val textBytes = text.toByteArray(StandardCharsets.UTF_8)
            val keySpec = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), KEY_ALGORITHM_AES)
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            val encrypt = cipher.doFinal(textBytes)
            "${bytesToHex(iv)}:${bytesToHex(encrypt)}"
        } catch (ex: Exception) {
            ""
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //encriptado mas seguro
    fun encrypt(str: String, key: String): String? {
        return try {
            val secureRandom = SecureRandom()
            val iv = ByteArray(IV_LENGTH)
            secureRandom.nextBytes(iv)
            Log.d("AQUI", String(iv, StandardCharsets.UTF_8))
            val textBytes = str.toByteArray(StandardCharsets.UTF_8)
            val cipher = Cipher.getInstance(CYPHER_INSTANCE)
            val newKey = SecretKeySpec(
                key.toByteArray(StandardCharsets.UTF_8),
                KeyProperties.KEY_ALGORITHM_AES
            )
            val gcmSpecWithIV =
                GCMParameterSpec(TAG_LENGTH, iv)
            cipher.init(Cipher.ENCRYPT_MODE, newKey, gcmSpecWithIV)
            val encrypt = cipher.doFinal(textBytes)
            val combined = ByteArray(iv.size + encrypt.size)
            System.arraycopy(iv, 0, combined, 0, iv.size)
            System.arraycopy(encrypt, 0, combined, iv.size, encrypt.size)
            Base64.encodeToString(combined, Base64.DEFAULT)
        } catch (ex: Exception) {
            null
        }
    }

    fun decrypt(str: String?, key: String): String? {
        return try {
            val textBytes =
                Base64.decode(str, Base64.DEFAULT)
            val iv =
                Arrays.copyOfRange(textBytes, 0, IV_LENGTH)
            Log.d(
                "IV",
                Base64.encodeToString(iv, Base64.DEFAULT)
            )
            val decrypt =
                Arrays.copyOfRange(textBytes, iv.size, textBytes.size)
            val c =
                Cipher.getInstance("AES/CBC/PKCS5Padding") //Constants.CYPHER_INSTANCE);
            val newKey = SecretKeySpec(
                key.toByteArray(StandardCharsets.UTF_8),
                KeyProperties.KEY_ALGORITHM_AES
            )
            val gcmSpecWithIV =
                GCMParameterSpec(TAG_LENGTH, iv)
            c.init(Cipher.DECRYPT_MODE, newKey, gcmSpecWithIV)
            val ct = c.doFinal(decrypt)
            String(ct, StandardCharsets.UTF_8)
        } catch (ex: Exception) {
            ""
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // metodos de convercion exagecimal


    private fun decodeHexString(hexString: String): ByteArray {
        if (hexString.length % 2 == 1) {
            throw IllegalArgumentException(
                "Invalid hexadecimal String supplied."
            )
        }

        val bytes = ByteArray(hexString.length / 2)
        var i = 0
        while (i < hexString.length) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2))
            i += 2
        }
        return bytes
    }

    private fun toDigit(hexChar: Char): Int {
        val digit = Character.digit(hexChar, 16)
        if (digit == -1) {
            throw IllegalArgumentException(
                "Invalid Hexadecimal Character: $hexChar"
            )
        }
        return digit
    }

    private fun hexToByte(hexString: String): Byte {
        val firstDigit = toDigit(hexString[0])
        val secondDigit = toDigit(hexString[1])
        return ((firstDigit shl 4) + secondDigit).toByte()
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val builder = StringBuilder()
        for (b in bytes) {
            builder.append(String.format("%02x", b))
        }
        return builder.toString()
    }

    @Throws(IOException::class)
    fun intercept(chain: Interceptor.Chain,context : Context): Response {
        keyRemoteConfig = BuildConfig.KEY_AES
        val tokenSession: String = "token"
        var request = chain.request()
        val body = request.body()
        body?.writeTo(buffer)
        var strBody: String = buffer.readUtf8()
        strBody = encryptCBC(strBody, keyRemoteConfig?:"")
        val newBody = RequestBody.create(TEXT_PLAIN, strBody)
        request = request.newBuilder()
            .header("Content-Type", newBody.contentType().toString())
            .header("Authorization", "Bearer $tokenSession")
            .header("x-os", PLATFORM)
            .header("x-density", getDensity(context).toString())
            .header("x-width", getWidth(context).toString())
            .header("x-height", getHeight(context).toString())
            .method(request.method(), if (request.method() == "GET") null else newBody)
            .build()
        val response = chain.proceed(request)
        val responseBodyCopy = response.peekBody(Long.MAX_VALUE)
        val strResponse = responseBodyCopy.string()
        val decrypt =
            if (strResponse.isEmpty()){
              ""
            } else {
                decryptCBC(strResponse, keyRemoteConfig?:"")
            }
        val newResponsebody =
            ResponseBody.create(JSON, decrypt ?: strResponse)
        return response.newBuilder().body(newResponsebody).build()
    }

}


