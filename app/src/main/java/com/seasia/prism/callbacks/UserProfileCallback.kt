package com.seasia.prism.callbacks

import com.seasia.prism.model.GetUserProfileResponse
import com.seasia.prism.model.UserProfileResponse

interface UserProfileCallback {
    fun onSignupUser(body: UserProfileResponse)
    fun onUpdateUser(body: UserProfileResponse)
    fun onGetUserProfile(body: GetUserProfileResponse)
    fun onError()
}