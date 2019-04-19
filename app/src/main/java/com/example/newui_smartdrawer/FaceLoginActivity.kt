package com.example.newui_smartdrawer

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.example.newui_smartdrawer.util.DBManager
import com.example.newui_smartdrawer.util.SC_Const
import com.example.newui_smartdrawer.util.UploadRecordManager
import com.example.newui_smartdrawer.util.UserAccount
import kotlinx.android.synthetic.main.activity_facelogin.*
import kotlinx.android.synthetic.main.activity_login.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timerTask

class FaceLoginActivity :BaseActivity() {
    private var dbManager: DBManager? = null
    private var scApp: SCApp? = null
    private val LOGINNAME = "smart_cabinet)smart_cabinet_login_name"
    private val NAME = "name"
    private val REQUEST_CODE_OP = 3
    //    private var service:MyService?=null
//    private var bound = false
//    private var sc = MyseriviceConnection()

    private val TAG = this.javaClass.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facelogin)
        dbManager = DBManager(this)
        dbManager?.tableUpgrade()
        scApp = application as SCApp
        account_login.setOnClickListener {
            val intent=Intent()
            intent.setClass(this@FaceLoginActivity, LoginActivity::class.java)
            startActivity(intent)

        }

        face_button.setOnClickListener{

            if ((applicationContext as Application).mFaceDB.mRegister.isEmpty()) {
                Toast.makeText(this, "没有注册人脸，请先注册！", Toast.LENGTH_SHORT).show()
            } else {
                startDetector(0)
            }


        }



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_OP) {
            Log.i(TAG, "RESULT =$resultCode")
            if (data == null) {
                return
            }
            val bundle = data.extras
            val path = bundle!!.getString("imagePath")
            Log.i(TAG, "path=" + path!!)
        }
    }

    /**
     * @param uri
     * @return
     */
    private fun getPath(uri: Uri): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

                    return getDataColumn(this, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(this, contentUri, selection, selectionArgs)
                }
            }
        }
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val actualimagecursor = this.contentResolver.query(uri, proj, null, null, null)
        val actual_image_column_index = actualimagecursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        actualimagecursor.moveToFirst()
        val img_path = actualimagecursor.getString(actual_image_column_index)
        val end = img_path.substring(img_path.length - 4)
        return if (0 != end.compareTo(".jpg", ignoreCase = true) && 0 != end.compareTo(".png", ignoreCase = true)) {
            null
        } else img_path
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                      selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param mBitmap
     */


    private fun startDetector(camera: Int) {
        val it = Intent(this@FaceLoginActivity, DetecterActivity::class.java)
        it.putExtra("Camera", camera)
        startActivityForResult(it, REQUEST_CODE_OP)
    }



}
