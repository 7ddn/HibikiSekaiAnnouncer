package org.sddn.plugin.hibiki

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File

object HttpUtils {
    suspend fun httpGet(
        url : String) : Response?
    {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("User-Agent", PluginConfig.UserAgent)
            .get()
            .build()
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful && response.body != null){
                //response.close()
                return response
            }
        } catch (e:Exception) {
            PluginMain.logger.info(e.message)
        }
        return null
    }

    suspend fun getImageFromUrlOrSave(
        file : File? = null,
        url : String) :ByteArray?{
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("User-Agent", PluginConfig.UserAgent)
            .get()
            .build()
        val response = client.newCall(request).execute()
        try {
            if (response.isSuccessful && response.body != null){
                val iS = response.body!!.byteStream()
                val bitmap = iS.readBytes()
                file?.writeBytes(bitmap)
                if (file!=null && file.exists()) {
                    PluginMain.logger.info("Successfully saved image ${file.absolutePath}")
                } else {
                    if (file!=null) PluginMain.logger.info("Failed at saving image ${file.absolutePath}")
                }
                response.close()
                return bitmap
            }
        } catch (e:Exception){
            response.close()
            PluginMain.logger.info(e.message)
        }
        return null
    }

    fun cardNormalUrlGenerate(characterID: String, relativeID: String) : String{
        return "https://assets.pjsek.ai/file/pjsekai-assets/startapp/character/member/" +
            "res${characterID}_no${relativeID}/card_normal.png"
    }

    fun cardAfterTrainingUrlGenerate(characterID: String, relativeID: String) : String{
        return "https://assets.pjsek.ai/file/pjsekai-assets/startapp/character/member/" +
            "res${characterID}_no${relativeID}/card_after_training.png"
    }

    fun iconNormalUrlGenerate(characterID: String, relativeID: String) : String{
        return "https://assets.pjsek.ai/file/pjsekai-assets/startapp/character/member_cutout/" +
            "res${characterID}_no${relativeID}/normal/thumbnail_xl.png"
    }

    fun iconAfterTrainingUrlGenerate(characterID: String, relativeID: String) : String{
        return "https://assets.pjsek.ai/file/pjsekai-assets/startapp/character/member_cutout/" +
            "res${characterID}_no${relativeID}/after_training/thumbnail_xl.png"
    }

    fun gachaLogoUrlGenerate(gachaID: Int) : String{
        return "https://assets.pjsek.ai/file/pjsekai-assets/ondemand/gacha/ab_gacha_" +
            "$gachaID/logo/logo.png"
    }


}