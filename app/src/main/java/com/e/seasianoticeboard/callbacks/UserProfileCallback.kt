package com.e.seasianoticeboard.callbacks

import com.e.seasianoticeboard.model.GetUserProfileResponse
import com.e.seasianoticeboard.model.UserProfileResponse

interface UserProfileCallback {
    fun onSignupUser(body: UserProfileResponse)
    fun onUpdateUser(body: UserProfileResponse)
    fun onGetUserProfile(body: GetUserProfileResponse)
    fun onError()
}