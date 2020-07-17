package com.seasia.prism.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class Utils {
    private val imageExtName = "/.iSMS/"

    fun getImageBitmap(categoryId: String, imageName: String, imageUrl: String): Bitmap? {
        extStorageDirectory = Environment.getExternalStorageDirectory()
        var myBitmap: Bitmap? = null

        var fileInputStream: FileInputStream? = null
        val bufferInputStream: BufferedInputStream
        val path = extStorageDirectory.toString() + imageExtName + categoryId + "/" + imageName
        try {
            Environment.getExternalStorageState()
            fileInputStream = FileInputStream(path)
            bufferInputStream = BufferedInputStream(fileInputStream)
            myBitmap = BitmapFactory.decodeStream(bufferInputStream)
        } catch (e: FileNotFoundException) {
            Log.d("FileNotFoundException", e.toString() + "===" + e.message)
            try {
                Log.d("URL :", "url::$imageUrl")
                myBitmap = convertBitmap(imageUrl)
                if (myBitmap != null) {
                    addImagesToSDcard(myBitmap, categoryId, imageName)
                } else {
                    Log.d("URL Bitmap", "URL not working==$myBitmap")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return myBitmap
    }




    companion object {
        val downloadDirectory = "Androhub Downloads"
        private val imageExtName = "/.iSMS/"
        var PRINT_LOGS = false
        private var extStorageDirectory: File? = null

        fun copyStream(`is`: InputStream, os: OutputStream) {
            val bufferSize = 1024
            try {
                val bytes = ByteArray(bufferSize)
                while (true) {
                    val count = `is`.read(bytes, 0, bufferSize)
                    if (count == -1)
                        break
                    os.write(bytes, 0, count)
                }
            } catch (ex: Exception) {
                Log.d("Exception", ex.toString() + "==convertBitmap==" + ex.message)
            }
        }

        fun convertBitmap(url: String): Bitmap? {
            var myBitmap: Bitmap? = null
            try {
                val myImageURL = URL(url)
                val connection = myImageURL.openConnection() as HttpURLConnection
                val encoded = ""
                connection.setRequestProperty("Authorization", "Basic $encoded")
                connection.doInput = true
                connection.connect()

                if (connection.responseCode != 404) {
                    val input = connection.inputStream
                    myBitmap = BitmapFactory.decodeStream(input)

                    myBitmap = resizeYourBitmap(myBitmap)
                }
            } catch (e: Exception) {
                Log.d("Exception", e.toString() + "==convertBitmap==" + e.message)
            }

            return myBitmap
        }

        private fun resizeYourBitmap(yourSelectedImageBitmap: Bitmap?): Bitmap? {
            var resizedBitmap: Bitmap? = null
            if (yourSelectedImageBitmap != null) {
                //----------resize for set in imageView------------
                val width = yourSelectedImageBitmap.width
                val height = yourSelectedImageBitmap.height
                val newWidth = 100
                val newHeight = 100

                // calculate the scale - in this case = 0.4f
                val scaleWidth = newWidth.toFloat() / width
                val scaleHeight = newHeight.toFloat() / height

                val matrix = Matrix()
                // resize the bit map
                matrix.postScale(scaleWidth, scaleHeight)

                resizedBitmap =
                    Bitmap.createBitmap(yourSelectedImageBitmap, 0, 0, width, height, matrix, true)


            }
            return resizedBitmap
        }

        fun addImagesToSDcard(bitmap: Bitmap, categoryId: String, imageName: String) {
            extStorageDirectory = Environment.getExternalStorageDirectory()
            Log.d("message::::", "addImagesToSDcard:start")
            var outStreamFile: OutputStream? = null
            var file: File? = null
            try {
                Environment.getExternalStorageState()

                file = File(extStorageDirectory.toString() + imageExtName + categoryId + "/")
                if (!file.exists()) {
                    file.mkdirs()
                }
                file = File(
                    extStorageDirectory.toString() + imageExtName + categoryId + "/",
                    imageName
                )

            } catch (e: Exception) {
                Log.d("Exception", e.toString() + "==addImagesToSDcard==" + e.message)
            }

            try {
                Environment.getExternalStorageState()
                outStreamFile = FileOutputStream(file)
                Environment.getExternalStorageState()
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, outStreamFile)
                outStreamFile.flush()
                outStreamFile.close()
            } catch (e1: FileNotFoundException) {
                Log.d("message", "FileNotFoundException::::::::::completed " + e1.message)
                e1.printStackTrace()
            } catch (e: IOException) {
                Log.d("message", "IOException::::::::::completed " + e.message)
                e.printStackTrace()
            }
            Log.d("message", "addImagesToSDcard::::::::::completed ===imageName:=$imageName")
        }

        fun getRoundedCornerBitmap(bitmap: Bitmap, pixels: Int): Bitmap {
            val output = Bitmap.createBitmap(
                bitmap.width, bitmap
                    .height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(output)

            val color = -0xbdbdbe
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)
            val rectF = RectF(rect)
            val roundPx = pixels.toFloat()

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)

            return output
        }


        fun openAttachement(uri: Uri, context: Context) {
            val intent = Intent(Intent.ACTION_VIEW)
            if (uri.toString().contains(".doc") || uri.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword")
            } else if (uri.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf")
            } else if (uri.toString().contains(".ppt") || uri.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
            } else if (uri.toString().contains(".xls") || uri.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel")
            } else if (uri.toString().contains(".zip") || uri.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav")
            } else if (uri.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf")
            } else if (uri.toString().contains(".wav") || uri.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav")
            } else if (uri.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif")
            } else if (uri.toString().contains(".jpg") || uri.toString().contains(".jpeg") || uri.toString().contains(
                    ".png"
                )
            ) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg")
            } else if (uri.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain")
            } else if (uri.toString().contains(".3gp") || uri.toString().contains(".mpg") || uri.toString().contains(
                    ".mpeg"
                ) || uri.toString().contains(".mpe") || uri.toString().contains(".mp4") || uri.toString().contains(
                    ".avi"
                )
            ) {
                // Video files
                intent.setDataAndType(uri, "video/*")
            } else {
                intent.setDataAndType(uri, "*/*")
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                //if user doesn't have pdf reader instructing to download a pdf reader
                Toast.makeText(
                    context,
                    "Please download any document/pdf reader.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }







        fun emailValidator(email: String?): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val EMAIL_PATTERN =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@seasiainfotech+(\\.[A-Za-z0-9]+)*(\\.[com]{2,})$"
            pattern = Pattern.compile(EMAIL_PATTERN)
            matcher = pattern.matcher(email)
            return matcher.matches()
        }

        fun emailValidatorSecond(email: String?): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val EMAIL_PATTERN =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@seasia+(\\.[A-Za-z0-9]+)*(\\.[in]{2,})$"
            pattern = Pattern.compile(EMAIL_PATTERN)
            matcher = pattern.matcher(email)
            return matcher.matches()
        }

        fun emailValidatorThired(email: String?): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val EMAIL_PATTERN =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@cerebrum+(\\.[A-Za-z0-9]+)*(\\.[com]{2,})$"
            pattern = Pattern.compile(EMAIL_PATTERN)
            matcher = pattern.matcher(email)
            return matcher.matches()
        }



    }
    fun getLocalDate(format: String, milisec: String?,outputFormat:String?): String {
        val inputFormat = SimpleDateFormat(format, Locale.getDefault())
        val outputFormat1 = SimpleDateFormat(outputFormat, Locale.getDefault())

        //val tz = TimeZone.getTimeZone("Local")
        // inputFormat.timeZone = tz
        val date = inputFormat.parse(milisec)

        val tzLocal = TimeZone.getDefault()
        outputFormat1.timeZone = tzLocal
        return  outputFormat1.format(date)


    }
}