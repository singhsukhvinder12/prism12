package com.e.seasianoticeboard.presenter

import androidx.lifecycle.MutableLiveData
import com.e.seasianoticeboard.api.GetRestAdapter
import com.e.seasianoticeboard.model.GetUserProfileInput
import com.e.seasianoticeboard.model.GetUserProfileResponse
import com.e.seasianoticeboard.model.UserProfileInput
import com.e.seasianoticeboard.model.UserProfileResponse
import com.e.seasianoticeboard.utils.UtilsFunctions
import com.e.seasianoticeboard.view.core.auth.UserProfileActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import java.util.HashMap

class UpdateUserProfilePresenter(var userProfileActivity: UserProfileActivity) {



    private val IMAGE_EXTENSION = "image/*"

    fun toRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }

    fun signupUser(input: UserProfileInput) {

        val callResponse: Call<UserProfileResponse>

        val map = HashMap<String, RequestBody>()
        map["UserId"] = toRequestBody(input.UserId.toString())
        map["FirstName"] = toRequestBody(input.FirstName.toString())
        map["LastName"] = toRequestBody(input.LastName.toString())
        map["PhoneNo"] = toRequestBody(input.PhoneNo.toString())
        map["Address"] = toRequestBody(input.Address.toString())
        map["Email"] = toRequestBody(input.Email.toString())
        map["Gender"] = toRequestBody(input.Gender.toString())
        map["DOB"] = toRequestBody(input.DOB.toString())
        map["CityId"] = toRequestBody("1")
        map["Password"] = toRequestBody("")
        val userImage: MultipartBody.Part
        if (input.ImageUrl != null && !input.ImageUrl!!.isEmpty()) {
            userImage = MultipartBody.Part.createFormData(
                "ImageUrl", input.imageFile!!.name,
                RequestBody.create(MediaType.parse(IMAGE_EXTENSION), input.imageFile!!)
            )
            callResponse = GetRestAdapter.getRestAdapter(false).signupUser(map, userImage)
        } else {
            callResponse = GetRestAdapter.getRestAdapter(false).signupUser(map)
        }
        callResponse.enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: retrofit2.Response<UserProfileResponse>?
            ) {
                if (response!!.code() == 500) {
                    UtilsFunctions.showToastError(response.message())
                    userProfileActivity.onError()
                    return
                }

                if (response!!.body() != null) {
                    userProfileActivity.onSignupUser(response.body())

                    //       signupCallbacks.onSignupSuccess(response)
                } else{
                    userProfileActivity.onError()
                    UtilsFunctions.showToastError(response.message())
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                //      signupCallbacks.onSignupError(call, t)
                userProfileActivity.onError()
                UtilsFunctions.showToastError(t.message)
            }
        })
    }



    fun updateUser(input: UserProfileInput) {

        val callResponse: Call<UserProfileResponse>

        val map = HashMap<String, RequestBody>()
        map["UserId"] = toRequestBody(input.UserId.toString())
        map["FirstName"] = toRequestBody(input.FirstName.toString())
        map["LastName"] = toRequestBody(input.LastName.toString())
        map["PhoneNo"] = toRequestBody(input.PhoneNo.toString())
        map["Address"] = toRequestBody(input.Address.toString())
        map["Email"] = toRequestBody(input.Email.toString())
        map["Gender"] = toRequestBody(input.Gender.toString())
        map["DOB"] = toRequestBody(input.DOB.toString())
        map["CityId"] = toRequestBody("1")
        map["Password"] = toRequestBody("")
        val userImage: MultipartBody.Part
        if (input.ImageUrl != null && !input.ImageUrl!!.isEmpty()) {
            userImage = MultipartBody.Part.createFormData(
                "ImageUrl", input.imageFile!!.name,
                RequestBody.create(MediaType.parse(IMAGE_EXTENSION), input.imageFile!!)
            )
            callResponse = GetRestAdapter.getRestAdapter(false).updateUser(map, userImage)
        } else {
            callResponse = GetRestAdapter.getRestAdapter(false).updateUser(map)
        }
        callResponse.enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: retrofit2.Response<UserProfileResponse>?
            ) {
                if (response!!.body() != null) {
                    userProfileActivity.onUpdateUser(response.body())

                    //       signupCallbacks.onSignupSuccess(response)
                } else{
                    userProfileActivity.onError()
                    UtilsFunctions.showToastError(response.message())
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                //      signupCallbacks.onSignupError(call, t)
                userProfileActivity.onError()
                UtilsFunctions.showToastError(t.message)
            }
        })
    }

    fun getUserProfile(input: GetUserProfileInput) {
        val call = GetRestAdapter.getRestAdapter(true).getProfile(input)
        call.enqueue(object : Callback<GetUserProfileResponse> {
            override fun onResponse(call: Call<GetUserProfileResponse>, response: retrofit2.Response<GetUserProfileResponse>?
            ) {

                if (response?.body()?.StatusCode == "302") {
                    userProfileActivity.onGetUserProfile(response.body())
                } else {
                    userProfileActivity.onError()
                    UtilsFunctions.showToastError(response?.body()?.Message)
                }
            }

            override fun onFailure(call: Call<GetUserProfileResponse>, t: Throwable) {
                userProfileActivity.onError()
                UtilsFunctions.showToastError(t.message)
            }
        })
    }

}