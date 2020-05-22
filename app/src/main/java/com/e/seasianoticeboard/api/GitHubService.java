package com.e.seasianoticeboard.api;



import com.e.seasianoticeboard.model.GetUserProfileInput;
import com.e.seasianoticeboard.model.GetUserProfileResponse;
import com.e.seasianoticeboard.model.SignupVerificationResponse;
import com.e.seasianoticeboard.model.UpdateProfileResponse;
import com.e.seasianoticeboard.model.UserProfileInput;
import com.e.seasianoticeboard.model.UserProfileResponse;
import com.e.seasianoticeboard.model.VerifyEmailInput;
import com.e.seasianoticeboard.model.VerifyEmailResponse;
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.ReportPostInput;
import com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model.RepostPostResponse;
import com.e.seasianoticeboard.views.institute.newsfeed.AddPostResponse;
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.CommentResponse;
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.DeleteCommentInput;
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.DeleteCommentResponse;
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.DeletePostInput;
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.DeletePostResponse;
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetCommentResponse;
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetFeedInput;
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetFeedResponse;
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.GetLikeResponse;
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.LikeInput;
import com.e.seasianoticeboard.views.institute.newsfeed.displaynewsfeed.model.LikesResponse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface GitHubService {
    @Multipart
    @POST("Social/AddUpdateNewsLetter")
    Call<AddPostResponse> addPost(@PartMap Map<String, RequestBody> params,
                                  @Part MultipartBody.Part updateImage);
    @Multipart
    @POST("Social/AddUpdateNewsLetter")
    Call<AddPostResponse> addPost(@PartMap Map<String, RequestBody> params,
                                  @Part MultipartBody.Part[] updateImage);

    @Multipart
    @POST("Social/AddUpdateNewsLetter")
    Call<AddPostResponse> addPost(@PartMap Map<String, RequestBody> params);


    @POST("Social/GetNewsLetter")
    Call<GetFeedResponse> getPostDara(@Body GetFeedInput UserId);

    @Multipart
    @POST("Social/AddUpdateComment")
    Call<CommentResponse> viewComment(@PartMap Map<String, RequestBody> params);

    @POST("Social/AddUpdateLike")
    Call<LikesResponse> getLikes(@Body LikeInput input);

    @POST("Social/DeleteNewsLetter")
    Call<DeletePostResponse> deletePost(@Body DeletePostInput input);

    @POST("Social/GetCommentNewsFeed")
    Call<GetCommentResponse> getCommentList(@Query("postId") String postId);

    @POST("Social/GetLikesNewsFeed")
    Call<GetLikeResponse> getLikeList(@Query("postId") String postId);

    @POST("Social/DeleteComment")
    Call<DeleteCommentResponse> deleteComment(@Body DeleteCommentInput input);

    @Multipart
    @POST("Social/AddUpdateIssueReported")
    Call<RepostPostResponse> reportPost(@PartMap Map<String, RequestBody> params);

    @POST("User/VerifyEmail")
    Call<VerifyEmailResponse> verifyEmail(@Body VerifyEmailInput email);

    @POST("User/GetUserDetailByPhoneEmail")
    Call<GetUserProfileResponse> getProfile(@Body GetUserProfileInput email);

   @POST("User/EmailVerificationForSignUp")
   Call<SignupVerificationResponse> signupVerification(@Query("email") String email);


    @Multipart
    @POST("User/UserSignUp")
    Call<UserProfileResponse> signupUser(@PartMap Map<String, RequestBody> params,
                                         @Part MultipartBody.Part signupUser);
    @Multipart
    @POST("User/UserSignUp")
    Call<UserProfileResponse> signupUser(@PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("User/UpdateUser")
    Call<UserProfileResponse> updateUser(@PartMap Map<String, RequestBody> params,
                                           @Part MultipartBody.Part signupUser);
    @Multipart
    @POST("User/UpdateUser")
    Call<UserProfileResponse> updateUser(@PartMap Map<String, RequestBody> params);
}
