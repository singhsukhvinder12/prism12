package com.e.seasianoticeboard.presenter

import androidx.lifecycle.MutableLiveData
import com.e.seasianoticeboard.api.GetRestAdapter
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.AddUpdateImageInput
import com.e.seasianoticeboard.views.institute.newsfeed.AddPostActivity
import com.e.seasianoticeboard.views.institute.newsfeed.AddPostInput
import com.e.seasianoticeboard.views.institute.newsfeed.AddPostResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import java.io.File
import java.util.HashMap

class AddPostPresenter(var addPostActivity: AddPostActivity) {
    private var imageParts: Array<MultipartBody.Part?>? = null
    private val IMAGE_EXTENSION = "image/*"
    private val AUDIO_EXTENSION = "audio/*"
    private val VIDEO_EXTENSION = "video/*"

    fun toRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }
    fun getPostData(addPost: AddPostInput, videoFIle: File?, audioFIle: File?, imagesList: ArrayList<AddUpdateImageInput>?){
        val callResponse: Call<AddPostResponse>

        val map = HashMap<String, RequestBody>()
        map["Title"] = toRequestBody(addPost.Title.toString())
        map["Description"] = toRequestBody(addPost.Description.toString())
        map["DeleteIds"] = toRequestBody(addPost.DeleteIds.toString())
        map["Links"] = toRequestBody(addPost.Links.toString())
        map["NewsLetterIds"] = toRequestBody(addPost.NewsLetterIds.toString())
        map["ParticularId"] = toRequestBody(addPost.ParticularId.toString())
        map["TypeId"] = toRequestBody(addPost.TypeId.toString())


        if (audioFIle != null) {
            val audioRequestFile =
                RequestBody.create(MediaType.parse(AUDIO_EXTENSION), audioFIle!!)
            val audioFile =
                MultipartBody.Part.createFormData(
                    "IstNewsLetterVideo",
                    audioFIle!!.name,
                    audioRequestFile
                )

            callResponse = GetRestAdapter.getRestAdapter(true)
                .addPost(map, audioFile)
        } else if (videoFIle != null) {
            val videoRequestFile =
                RequestBody.create(MediaType.parse(VIDEO_EXTENSION), videoFIle)
            val videoFile =
                MultipartBody.Part.createFormData(
                    "IstNewsLetterAudio",
                    videoFIle!!.name,
                    videoRequestFile
                )
            callResponse = GetRestAdapter.getRestAdapter(true)
                .addPost(map, videoFile)
        } else if (imagesList!!.size > 0) {

            imageParts = arrayOfNulls(imagesList.size)

            for (i in 0..imagesList.size - 1) {
                val imageBody = RequestBody.create(
                    MediaType.parse(IMAGE_EXTENSION),
                    imagesList!![i].fileUrl!!
                )
                imageParts!![i] =
                    MultipartBody.Part.createFormData(
                        "IstNewsLetterImage",
                        imagesList!![i].fileUrl!!.name,
                        imageBody
                    )
            }


            callResponse = GetRestAdapter.getRestAdapter(true)
                .addPost(map, imageParts)

        } else {
            callResponse = GetRestAdapter.getRestAdapter(true)
                .addPost(map)
        }

        callResponse.enqueue(object : Callback<AddPostResponse> {
            override fun onResponse(
                call: Call<AddPostResponse>,
                response: retrofit2.Response<AddPostResponse>?
            ) {
                if (response != null) {
                    addPostActivity.onSuccess(response.body())

                } else {
                    addPostActivity.onError()
                }
            }

            override fun onFailure(call: Call<AddPostResponse>, t: Throwable) {
                addPostActivity.onError()
                UtilsFunctions.showToastError(t?.message)
            }
        })

    }
}