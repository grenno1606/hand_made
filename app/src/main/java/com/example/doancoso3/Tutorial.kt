package com.example.dacs3_ns_22ns082

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class Tutorial(@SerializedName("tutorialid") val tutorialId:String,@SerializedName("tutorialname") val tutorialName:String, val video:String, @SerializedName("created_at") val createdAt:String, @SerializedName("updated_at") val updatedAt:String)

data class TutorialRequest(val tutorialId: String,val tutorialName: String,val createdAt:String,val updatedAt:String, val video: File) {
    fun toMap(): Map<String, RequestBody> {
        val map = mutableMapOf<String, RequestBody>()
        map["tutorialid"]=tutorialId.toRequestBody()
        map["tutorialname"] = tutorialName.toRequestBody()
        map["created_at"]=createdAt.toRequestBody()
        map["updated_at"]=updatedAt.toRequestBody()
        return map
    }

    fun getVideoPart(): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            "video",
            video.name + ".mp4",
            video.asRequestBody("video/mp4".toMediaType())
        )
    }
}