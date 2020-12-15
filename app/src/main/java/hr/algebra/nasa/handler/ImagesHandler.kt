package hr.algebra.nasa.handler

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import hr.algebra.nasa.factory.createGetHttpUrlConnection
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.HttpURLConnection

private const val ARTICLE_PICTURE_WIDTH = 1000
private const val ARTICLE_PICTURE_HEIGHT = 750
private const val QUALITY = 100
private const val TAG = "ImagesHandler"

fun downloadImageAndStore(context: Context, url: String, filename: String): String? {
    val extension = url.substring(url.lastIndexOf("."))

    var file: File = getFile(context, filename, extension)

    try {
        val con: HttpURLConnection = createGetHttpUrlConnection(url)
        con.inputStream.use { `is` ->
            FileOutputStream(file).use { fos ->
                val bitmap = BitmapFactory.decodeStream(`is`)
                val resizedBitmap: Bitmap =
                    getResizedBitmap(bitmap, ARTICLE_PICTURE_WIDTH, ARTICLE_PICTURE_HEIGHT)
                val buffer = getBytesFromBitmap(resizedBitmap)
                fos.write(buffer)
                return file.absolutePath
            }
        }
    } catch (e: Exception) {

    }

    return null
}

private fun getBytesFromBitmap(bitmap: Bitmap): ByteArray {
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, bos)
    return bos.toByteArray()
}

private fun getResizedBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
    val scaleWidth = newWidth.toFloat() / bitmap.width
    val scaleHeight = newHeight.toFloat() / bitmap.height

    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
}

private fun getFile(context: Context, filename: String, extension: String): File {
    val dir: File? = context.applicationContext.getExternalFilesDir(null)
    val file = File(dir, File.separator.toString() + filename + extension)

    if (file.exists()) {
        file.delete()
    }

    return file
}
