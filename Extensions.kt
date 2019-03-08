import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.annotation.ColorInt
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.io.File
import java.io.FileOutputStream
import java.util.*


/**
 * Kotlin extensions.
 * 😎🤯
 */

inline fun <reified T> Gson.fromJsonType(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)!!

fun Any?.notNull(f: () -> Unit) {
    if (this != null) {
        f()
    }
}

fun String.isValidEmail(): Boolean = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Int.convertItOr(it: String, defaultValueToReturn: Int): Int {
    return try {
        Integer.valueOf(it)
    } catch (error: java.lang.Exception) {
        return defaultValueToReturn
    }
}

fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun Boolean.toByte(): Byte {
    return if (this) 1.toByte() else 0.toByte()
}

/**
 * Open video link on default media player.
 */
fun Context.openVideoLink(videoLink: String) {
    val intent = RedyVideoPlayerActivity.getIntent(this, videoLink)
    startActivity(intent)
}

fun Context.nullOrEmpty(string: String?): Boolean {
    return string == null || string.isEmpty()
}

/**
 * For us, a small screen is below 4.0 inch.
 */
fun Activity.hasSmallScreen(): Boolean {
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)
    val x = Math.pow((dm.widthPixels / dm.xdpi).toDouble(), 2.0)
    val y = Math.pow((dm.heightPixels / dm.ydpi).toDouble(), 2.0)
    return Math.sqrt(x + y) <= 4.0
}

/**
 * Get the width in pixels.
 */
fun Activity.getScreenWidth(): Int {

    val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val metrics = DisplayMetrics()
    display.getMetrics(metrics)
    return metrics.widthPixels
}

/**
 * Add a Bold String at the send of a TextView.
 */
fun TextView.boldString(fullText: String, part: String) {
    val myCurrentTexStart = fullText.indexOf(part)
    val myCurrentTexEnd = part.length
    val builder = SpannableString(fullText)
    builder.setSpan(StyleSpan(Typeface.BOLD), myCurrentTexStart, myCurrentTexEnd, 0)
    text = builder
}

fun Activity.checkIfThisPermissionWasGrantedOrRequestIt(value: String, requestCode: Int): Boolean {

    if (ContextCompat.checkSelfPermission(this, value) != PackageManager.PERMISSION_GRANTED) {

        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, value)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this, arrayOf(value), requestCode)

            // android.Manifest.permission.WRITE_EXTERNAL_STORAGE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

        //it will be requested
        return false
    } else {
        return true
    }
}

/**
 * Share an image URL.
 */
fun Activity.shareImage(imageURL: String?) {

    if (imageURL?.isNotEmpty()!! || imageURL.isNotBlank()) {

        try {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "image/png"
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://$imageURL"))
            val openInChooser = Intent(intent)
            startActivity(openInChooser)
        } catch (e: Exception) {
            toast("Not able to share.")
        }
    }

}

/**
 * Convert a view into PNG. It will save the png image into download folder.
 */
fun View.convertItToPNG(): String {
    this.isDrawingCacheEnabled = true
    this.buildDrawingCache()
    val cache = this.drawingCache
    val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val fileName = "file${System.currentTimeMillis()}.png"
    val path = "$downloadFolder/$fileName"
    try {
        val fileOutputStream = FileOutputStream(path)
        cache.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        return if (File(path).exists()) path else ""
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        //this.destroyDrawingCache()
    }
    return ""
}

/**
 * Checks if the text inside the TextView is truncated.
 * @return True if truncated, false otherwise.
 */
fun TextView.isTruncated(): Boolean {

    val layout = this.layout
    val lines = layout.lineCount

    if (lines > 0) {
        val ellipsisCount = layout.getEllipsisCount(lines - 1)
        if (ellipsisCount > 0) {
            return true
        }
    }

    return false
}

fun View.goneIt() {
    this.visibility = View.GONE
}

fun View.showIt() {
    this.visibility = View.VISIBLE
}

fun View.hideIt() {
    this.visibility = View.INVISIBLE
}

fun View.isVisible() : Boolean { return this.visibility == View.VISIBLE }

fun View.progressAnim(): ObjectAnimator {
    val mProgressAnim = ObjectAnimator.ofFloat(this, "rotation", 360f).setDuration(1000)
    mProgressAnim.interpolator = AccelerateDecelerateInterpolator()
    mProgressAnim.repeatMode = ValueAnimator.RESTART
    mProgressAnim.repeatCount = Animation.INFINITE
    return mProgressAnim
}

fun String.isNumber(): Boolean {
    val result = this.toIntOrNull()
    return result != null
}


fun Int.formatToDecimal(): String {
    return String.format(Locale.getDefault(), "%02d", this)
}


fun View.changeSolidColor(colorInt: Int) {
    background.overrideColor(ContextCompat.getColor(this.context, colorInt))
}

fun Drawable.overrideColor(@ColorInt colorInt: Int) {
    when (this) {
        is GradientDrawable -> setColor(colorInt)
        is ShapeDrawable -> paint.color = colorInt
        is ColorDrawable -> color = colorInt
    }
}
