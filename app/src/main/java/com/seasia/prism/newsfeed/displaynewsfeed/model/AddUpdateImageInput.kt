package com.seasia.prism.newsfeed.displaynewsfeed.model

import android.net.Uri
import java.io.File

class AddUpdateImageInput {
    var imageId: String? = null
    var imageUrl: String? = null
    var fileUrl: File? = null
    var imageFileName: String? = null
    var fileType: String? = null
    var ColorCode: String? = null
    var attachementFile: Uri? = null
}
