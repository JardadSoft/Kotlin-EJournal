package com.JARDAD.EJournal

import android.util.Base64
import org.json.JSONObject
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class JCipher(val key: String) {

    private val tag = "#JARDAD #" + this.javaClass.simpleName

    private val kiv = "OFRna73m*aze01xY".toByteArray()

    private val k = SecretKeySpec(key.toByteArray(), "AES")

    private val CEncrypt = Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
        init(javax.crypto.Cipher.ENCRYPT_MODE, k, javax.crypto.spec.IvParameterSpec(kiv))
    }

    private val CDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
        init(Cipher.DECRYPT_MODE, k, IvParameterSpec(kiv))
    }

    fun Encrypt(json: JSONObject): String {
        val bytes = json.toString().toByteArray(Charsets.UTF_8)
        return Base64.encodeToString(CEncrypt.doFinal(bytes), Base64.DEFAULT)
    }

    fun Decrypt(cipher_text: String): JSONObject {
        val bytes = Base64.decode(cipher_text, Base64.DEFAULT)
        return JSONObject(CDecrypt.doFinal(bytes).toString(Charsets.UTF_8))
    }

}