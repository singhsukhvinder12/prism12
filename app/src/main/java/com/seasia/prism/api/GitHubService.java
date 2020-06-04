package com.seasia.prism.api;


import com.seasia.prism.core.newsfeed.AddPostResponse;
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.CommentResponse;
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.DeleteCommentInput;
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.DeleteCommentResponse;
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.DeletePostInput;
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.DeletePostResponse;
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.GetCommentResponse;
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.GetFeedInput;
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.GetFeedResponse;
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.GetLikeResponse;
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.LikeInput;
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.LikesResponse;
import com.seasia.prism.model.DeviceTokenInput;
import com.seasia.prism.model.DeviceTokenResponse;
import com.seasia.prism.model.GetUserProfileInput;
import com.seasia.prism.model.GetUserProfileResponse;
import com.seasia.prism.model.SignupVerificationResponse;
import com.seasia.prism.model.UpdateProfileResponse;
import com.seasia.prism.model.UserProfileInput;
import com.seasia.prism.model.UserProfileResponse;
import com.seasia.prism.model.VerifyEmailInput;
import com.seasia.prism.model.VerifyEmailResponse;
import com.seasia.prism.model.input.AddChoiceInput;
import com.seasia.prism.model.input.AskQuestionInput;
import com.seasia.prism.model.input.LogoutInput;
import com.seasia.prism.model.output.AddChoiceResponse;
import com.seasia.prism.model.output.AskQuestionResponse;
import com.seasia.prism.model.output.ChoiceResponse;
import com.seasia.prism.model.output.LogoutResponse;
import com.seasia.prism.model.output.QuestionResponse;
import com.seasia.prism.core.newsfeed.displaynewsfeed.model.RepostPostResponse;


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
    Call<AddPostResponse> addPostThumb(@PartMap Map<String, RequestBody> params,
                                       @Part MultipartBody.Part updateImage, @Part MultipartBody.Part updateThumbNail);

    @Multipart
    @POST("Social/AddUpdateNewsLetter")
    Call<AddPostResponse> addPost(@PartMap Map<String, RequestBody> params,
                                  @Part MultipartBody.Part[] updateImage);

    @Multipart
    @POST("Social/AddUpdateNewsLetter")
    Call<AddPostResponse> addPost(@PartMap Map<String, RequestBody> params);


    @POST("Social/GetNewsLetter")
    Call<GetFeedResponse> getPostDara(@Body GetFeedInput UserId);

    @POST("User/AddUpdateDeviceDetail")
    Call<DeviceTokenResponse> sendDeviceToken(@Body DeviceTokenInput UserId);

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

    @POST("User/Logout")
    Call<LogoutResponse> logout(@Query("userId") String userId, @Query("deviceType") String deviceType);

    @POST("User/EmailVerificationForSignUp")
    Call<SignupVerificationResponse> signupVerification(@Query("email") String email);

    @GET("Social/GetChoiceQuestions")
    Call<ChoiceResponse> getChoiceQuestion(@Query("userId") String userId);

    @POST("Social/AddUpdateChoiceQuestionAnswers")
    Call<AddChoiceResponse> addChoice(@Body AddChoiceInput input);


    @GET("Social/GetQuestions")
    Call<QuestionResponse> getQuestion(@Query("userId") String userId);

    @POST("Social/AddUpdateQuestionAnswers")
    Call<AskQuestionResponse> addQuestion(@Body AskQuestionInput input);

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
