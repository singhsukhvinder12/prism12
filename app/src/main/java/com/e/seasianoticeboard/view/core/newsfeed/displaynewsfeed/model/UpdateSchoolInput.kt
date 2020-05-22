package com.e.seasianoticeboard.view.core.newsfeed.displaynewsfeed.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.File

class UpdateSchoolInput {
    @SerializedName("InstititeId")
    @Expose
    var instititeId: String? = null
    @SerializedName("Name")
    @Expose
    var name: String? = null
    @SerializedName("Logo")
    @Expose
    var logo: String? = null
    @SerializedName("LogoImageName")
    @Expose
    var logoImageName: String? = null
    @SerializedName("ThumbLogo")
    @Expose
    var thumbLogo: String? = null
    @SerializedName("Latitude")
    @Expose
    var latitude: String? = null
    @SerializedName("Longtitude")
    @Expose
    var longtitude: String? = null
    @SerializedName("WebsiteLink")
    @Expose
    var websiteLink: String? = null
    @SerializedName("Address")
    @Expose
    var address: String? = null
    @SerializedName("PhoneNo")
    @Expose
    var phoneNo: String? = null
    @SerializedName("Email")
    @Expose
    var email: String? = null
    @SerializedName("BoardId")
    @Expose
    var boardId: String? = null
    @SerializedName("BoardName")
    @Expose
    var boardName: String? = null
    @SerializedName("Inquiry")
    @Expose
    var inquiry: String? = null
    @SerializedName("EstablishDate")
    @Expose
    var establishDate: String? = null
    @SerializedName("TypeId")
    @Expose
    var typeId: String? = null
    @SerializedName("TypeName")
    @Expose
    var typeName: String? = null
    @SerializedName("AttachmentId")
    @Expose
    var attachmentId: String? = null
    @SerializedName("InstituteAttachmentList")
    @Expose
    var instituteAttachmentList: ArrayList<String>? = null
    @SerializedName("IFile")
    @Expose
    var iFile: File? = null
    @SerializedName("LstDeletedAttachment")
    @Expose
    var lstDeletedAttachment: ArrayList<LstDeleteAttachment>? = null

    class LstDeleteAttachment {
        @SerializedName("deleteAttachmentId")
        @Expose
        var deleteAttachmentId: String? = null
    }
}