package com.example.newui_smartdrawer

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.hardware.Camera
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import com.arcsoft.ageestimation.ASAE_FSDKAge
import com.arcsoft.ageestimation.ASAE_FSDKEngine
import com.arcsoft.ageestimation.ASAE_FSDKError
import com.arcsoft.ageestimation.ASAE_FSDKFace
import com.arcsoft.ageestimation.ASAE_FSDKVersion
import com.arcsoft.facerecognition.AFR_FSDKEngine
import com.arcsoft.facerecognition.AFR_FSDKError
import com.arcsoft.facerecognition.AFR_FSDKFace
import com.arcsoft.facerecognition.AFR_FSDKMatching
import com.arcsoft.facerecognition.AFR_FSDKVersion
import com.arcsoft.facetracking.AFT_FSDKEngine
import com.arcsoft.facetracking.AFT_FSDKError
import com.arcsoft.facetracking.AFT_FSDKFace
import com.arcsoft.facetracking.AFT_FSDKVersion
import com.arcsoft.genderestimation.ASGE_FSDKEngine
import com.arcsoft.genderestimation.ASGE_FSDKError
import com.arcsoft.genderestimation.ASGE_FSDKFace
import com.arcsoft.genderestimation.ASGE_FSDKGender
import com.arcsoft.genderestimation.ASGE_FSDKVersion
import com.example.newui_smartdrawer.util.*
import com.guo.android_extend.GLES2Render
import com.guo.android_extend.java.AbsLoop
import com.guo.android_extend.java.ExtByteArrayOutputStream
import com.guo.android_extend.tools.CameraHelper
import com.guo.android_extend.widget.CameraFrameData
import com.guo.android_extend.widget.CameraGLSurfaceView
import com.guo.android_extend.widget.CameraSurfaceView
import com.guo.android_extend.widget.CameraSurfaceView.OnCameraListener

import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timerTask

/**
 * Created by gqj3375 on 2017/4/28.
 */

class DetecterActivity : Activity(), OnCameraListener, View.OnTouchListener, Camera.AutoFocusCallback, View.OnClickListener {
    private val TAG = this.javaClass.simpleName
    private var dbManager: DBManager? = null
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mFormat: Int = 0
    private var mSurfaceView: CameraSurfaceView? = null
    private var mGLSurfaceView: CameraGLSurfaceView? = null
    private var mCamera: Camera? = null

    private var scApp: SCApp? = null

    internal var version = AFT_FSDKVersion()
    internal var engine = AFT_FSDKEngine()
    internal var mAgeVersion = ASAE_FSDKVersion()
    internal var mAgeEngine = ASAE_FSDKEngine()
    internal var mGenderVersion = ASGE_FSDKVersion()
    internal var mGenderEngine = ASGE_FSDKEngine()
    internal var result: MutableList<AFT_FSDKFace> = ArrayList()
    internal var ages: List<ASAE_FSDKAge> = ArrayList()
    internal var genders: List<ASGE_FSDKGender> = ArrayList()
    internal var spi: SerialPortInterface? = null
    internal var mCameraID: Int = 0
    internal var mCameraRotate: Int = 0
    internal var mCameraMirror: Int = 0
    internal var mImageNV21: ByteArray? = null
    internal var mFRAbsLoop: FRAbsLoop? = null
    internal var mAFT_FSDKFace: AFT_FSDKFace? = null
    private  var mHandler: Handler?=null
    internal var isPostted = false
    private val hide = Runnable {
        mTextView!!.alpha = 0.5f
        mImageView!!.imageAlpha = 128
        isPostted = false
    }

    private var mTextView: TextView? = null
    private var mTextView1: TextView? = null
    private var mImageView: ImageView? = null
    private var mImageButton: ImageButton? = null

    internal inner class FRAbsLoop : AbsLoop() {

        var version = AFR_FSDKVersion()
        var engine = AFR_FSDKEngine()
        var result = AFR_FSDKFace()
        var mResgist: List<FaceDB.FaceRegist> = (this@DetecterActivity.applicationContext as Application).mFaceDB.mRegister
        var face1: MutableList<ASAE_FSDKFace> = ArrayList()
        var face2: MutableList<ASGE_FSDKFace> = ArrayList()

        override fun setup() {
            var error = engine.AFR_FSDK_InitialEngine(FaceDB.appid, FaceDB.fr_key)
            Log.d(TAG, "AFR_FSDK_InitialEngine = " + error.code)
            error = engine.AFR_FSDK_GetVersion(version)
            Log.d(TAG, "FR=" + version.toString() + "," + error.code) //(210, 178 - 478, 446), degree = 1　780, 2208 - 1942, 3370
        }

        override fun loop() {
            if (mImageNV21 != null) {
                val rotate = mCameraRotate

                val time = System.currentTimeMillis()
                var error = engine.AFR_FSDK_ExtractFRFeature(mImageNV21, mWidth, mHeight, AFR_FSDKEngine.CP_PAF_NV21, mAFT_FSDKFace!!.rect, mAFT_FSDKFace!!.degree, result)
                Log.d(TAG, "AFR_FSDK_ExtractFRFeature cost :" + (System.currentTimeMillis() - time) + "ms")
                Log.d(TAG, "Face=" + result.featureData[0] + "," + result.featureData[1] + "," + result.featureData[2] + "," + error.code)
                val score = AFR_FSDKMatching()
                var max = 0.0f
                var name: String? = null
                for (fr in mResgist) {
                    for (face in fr.mFaceList.values) {
                        error = engine.AFR_FSDK_FacePairMatching(result, face, score)
                        Log.d(TAG, "Score:" + score.score + ", AFR_FSDK_FacePairMatching=" + error.code)
                        if (max < score.score) {
                            max = score.score
                            name = fr.mName
                        }
                    }
                }

                //age & gender
                face1.clear()
                face2.clear()
                face1.add(ASAE_FSDKFace(mAFT_FSDKFace!!.rect, mAFT_FSDKFace!!.degree))
                face2.add(ASGE_FSDKFace(mAFT_FSDKFace!!.rect, mAFT_FSDKFace!!.degree))
                val error1 = mAgeEngine.ASAE_FSDK_AgeEstimation_Image(mImageNV21, mWidth, mHeight, AFT_FSDKEngine.CP_PAF_NV21, face1, ages)
                val error2 = mGenderEngine.ASGE_FSDK_GenderEstimation_Image(mImageNV21, mWidth, mHeight, AFT_FSDKEngine.CP_PAF_NV21, face2, genders)
                Log.d(TAG, "ASAE_FSDK_AgeEstimation_Image:" + error1.code + ",ASGE_FSDK_GenderEstimation_Image:" + error2.code)
                Log.d(TAG, "age:" + ages[0].age + ",gender:" + genders[0].gender)
                val age = if (ages[0].age == 0) "年龄未知" else ages[0].age.toString() + "岁"
                val gender = if (genders[0].gender == -1) "性别未知" else if (genders[0].gender == 0) "男" else "女"

                //crop
                val data = mImageNV21
                val yuv = YuvImage(data, ImageFormat.NV21, mWidth, mHeight, null)
                val ops = ExtByteArrayOutputStream()
                yuv.compressToJpeg(mAFT_FSDKFace!!.rect, 80, ops)
                val bmp = BitmapFactory.decodeByteArray(ops.byteArray, 0, ops.byteArray.size)
                try {
                    ops.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                if (max > 0.6f) {
                    //fr success.
                    val max_score = max
                    Log.d(TAG, "fit Score:$max, NAME:$name")
                    val mNameShow = name
                    mHandler?.removeCallbacks(hide)
                    mHandler?.post {
//                        mTextView!!.alpha = 1.0f
//                        mTextView!!.text = mNameShow
//                        mTextView!!.setTextColor(Color.RED)
//                        mTextView1!!.visibility = View.VISIBLE
//                        mTextView1!!.text = "置信度：" + (max_score * 1000).toInt().toFloat() / 1000.0
//                        mTextView1!!.setTextColor(Color.RED)
//                        mImageView!!.rotation = rotate.toFloat()
//                        mImageView!!.scaleY = (-mCameraMirror).toFloat()
//                        mImageView!!.imageAlpha = 255
//                        mImageView!!.setImageBitmap(bmp)
                        login(mNameShow.toString(), dbManager!!.getUserAccountByUserName(mNameShow).userPassword)

                        //                            FaceRegister register = dbManager.getRegisterByUser(mNameShow);
                        //                            if(register!=null) {
                        //								spi.sendOpenLock(1, register.getBoxId());
                        //								dbManager.deletFaceregister(mNameShow);
                        //							}
                    }
                }
//              else {
//                    val mNameShow = "未识别"
//                    this@DetecterActivity.runOnUiThread {
//                        mTextView!!.alpha = 1.0f
//                        mTextView1!!.visibility = View.VISIBLE
//                        mTextView1!!.text = "$gender,$age"
//                        mTextView1!!.setTextColor(Color.RED)
//                        mTextView!!.text = mNameShow
//                        mTextView!!.setTextColor(Color.RED)
//                        mImageView!!.imageAlpha = 255
//                        mImageView!!.rotation = rotate.toFloat()
//                        mImageView!!.scaleY = (-mCameraMirror).toFloat()
//                        mImageView!!.setImageBitmap(bmp)
//                    }
//                }
                mImageNV21 = null
            }

        }

        override fun over() {
            val error = engine.AFR_FSDK_UninitialEngine()
            Log.d(TAG, "AFR_FSDK_UninitialEngine : " + error.code)
        }
    }

    /* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        //        spi = new SerialPortInterface(this,"/dev/ttyS4");
        scApp = application as SCApp
        dbManager = DBManager(this)
        mCameraID = if (intent.getIntExtra("Camera", 0) == 0) Camera.CameraInfo.CAMERA_FACING_BACK else Camera.CameraInfo.CAMERA_FACING_FRONT
        mCameraRotate = if (intent.getIntExtra("Camera", 0) == 0) 90 else 270
        mCameraMirror = if (intent.getIntExtra("Camera", 0) == 0) GLES2Render.MIRROR_NONE else GLES2Render.MIRROR_X
        mWidth = 1280
        mHeight = 960
        mFormat = ImageFormat.NV21
        mHandler = Handler()

        setContentView(R.layout.activity_camera)
        mGLSurfaceView = findViewById<View>(R.id.glsurfaceView) as CameraGLSurfaceView
        mGLSurfaceView!!.setOnTouchListener(this)
        mSurfaceView = findViewById<View>(R.id.surfaceView) as CameraSurfaceView
        mSurfaceView!!.setOnCameraListener(this)
        mSurfaceView!!.setupGLSurafceView(mGLSurfaceView, true, mCameraMirror, mCameraRotate)
        mSurfaceView!!.debug_print_fps(true, false)

        //snap
        mTextView = findViewById<View>(R.id.textView) as TextView
        mTextView!!.text = ""
        mTextView1 = findViewById<View>(R.id.textView1) as TextView
        mTextView1!!.text = ""

        mImageView = findViewById<View>(R.id.imageView) as ImageView
        mImageButton = findViewById<View>(R.id.imageButton) as ImageButton
        mImageButton!!.setOnClickListener(this)

        var err = engine.AFT_FSDK_InitialFaceEngine(FaceDB.appid, FaceDB.ft_key, AFT_FSDKEngine.AFT_OPF_0_HIGHER_EXT, 16, 5)
        Log.d(TAG, "AFT_FSDK_InitialFaceEngine =" + err.code)
        err = engine.AFT_FSDK_GetVersion(version)
        Log.d(TAG, "AFT_FSDK_GetVersion:" + version.toString() + "," + err.code)

        var error = mAgeEngine.ASAE_FSDK_InitAgeEngine(FaceDB.appid, FaceDB.age_key)
        Log.d(TAG, "ASAE_FSDK_InitAgeEngine =" + error.code)
        error = mAgeEngine.ASAE_FSDK_GetVersion(mAgeVersion)
        Log.d(TAG, "ASAE_FSDK_GetVersion:" + mAgeVersion.toString() + "," + error.code)

        var error1 = mGenderEngine.ASGE_FSDK_InitgGenderEngine(FaceDB.appid, FaceDB.gender_key)
        Log.d(TAG, "ASGE_FSDK_InitgGenderEngine =" + error1.code)
        error1 = mGenderEngine.ASGE_FSDK_GetVersion(mGenderVersion)
        Log.d(TAG, "ASGE_FSDK_GetVersion:" + mGenderVersion.toString() + "," + error1.code)
        mFRAbsLoop = FRAbsLoop()
        mFRAbsLoop!!.start()
    }

    /* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
    override fun onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy()
        mFRAbsLoop!!.shutdown()
        val err = engine.AFT_FSDK_UninitialFaceEngine()
        Log.d(TAG, "AFT_FSDK_UninitialFaceEngine =" + err.code)

        val err1 = mAgeEngine.ASAE_FSDK_UninitAgeEngine()
        Log.d(TAG, "ASAE_FSDK_UninitAgeEngine =" + err1.code)

        val err2 = mGenderEngine.ASGE_FSDK_UninitGenderEngine()
        Log.d(TAG, "ASGE_FSDK_UninitGenderEngine =" + err2.code)
    }

    override fun setupCamera(): Camera? {
        // TODO Auto-generated method stub
        mCamera = Camera.open(mCameraID)
        try {
            val parameters = mCamera!!.parameters
            parameters.setPreviewSize(mWidth, mHeight)
            parameters.previewFormat = mFormat

            for (size in parameters.supportedPreviewSizes) {
                Log.d(TAG, "SIZE:" + size.width + "x" + size.height)
            }
            for (format in parameters.supportedPreviewFormats) {
                Log.d(TAG, "FORMAT:" + format!!)
            }

            val fps = parameters.supportedPreviewFpsRange
            for (count in fps) {
                Log.d(TAG, "T:")
                for (data in count) {
                    Log.d(TAG, "V=$data")
                }
            }
            //parameters.setPreviewFpsRange(15000, 30000);
            //parameters.setExposureCompensation(parameters.getMaxExposureCompensation());
            //parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            //parameters.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
            //parmeters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            //parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
            //parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);
            mCamera!!.parameters = parameters
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (mCamera != null) {
            mWidth = mCamera!!.parameters.previewSize.width
            mHeight = mCamera!!.parameters.previewSize.height
        }
        return mCamera
    }

    override fun setupChanged(format: Int, width: Int, height: Int) {

    }

    override fun startPreviewImmediately(): Boolean {
        return true
    }

    override fun onPreview(data: ByteArray, width: Int, height: Int, format: Int, timestamp: Long): Any {
        val err = engine.AFT_FSDK_FaceFeatureDetect(data, width, height, AFT_FSDKEngine.CP_PAF_NV21, result)
        Log.d(TAG, "AFT_FSDK_FaceFeatureDetect =" + err.code)
        Log.d(TAG, "Face=" + result.size)
        for (face in result) {
            Log.d(TAG, "Face:" + face.toString())
        }
        if (mImageNV21 == null) {
            if (!result.isEmpty()) {
                mAFT_FSDKFace = result[0].clone()
                mImageNV21 = data.clone()
            } else {
                if (!isPostted) {
                    mHandler?.removeCallbacks(hide)
                    mHandler?.postDelayed(hide, 2000)
                    isPostted = true
                }
            }
        }
        //copy rects
        val rects = arrayOfNulls<Rect>(result.size)
        for (i in result.indices) {
            rects[i] = Rect(result[i].rect)
        }
        //clear result.
        result.clear()
        //return the rects for render.
        return rects
    }

    override fun onBeforeRender(data: CameraFrameData) {

    }

    override fun onAfterRender(data: CameraFrameData) {
        mGLSurfaceView!!.gleS2Render.draw_rect(data.params as Array<Rect>, Color.GREEN, 2)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        CameraHelper.touchFocus(mCamera!!, event, v, this)
        return false
    }

    override fun onAutoFocus(success: Boolean, camera: Camera) {
        if (success) {
            Log.d(TAG, "Camera Focus SUCCESS!")
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.imageButton) {
            if (mCameraID == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT
                mCameraRotate = 90
                mCameraMirror = GLES2Render.MIRROR_X
            } else {
                mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK
                mCameraRotate = 270
                mCameraMirror = GLES2Render.MIRROR_NONE
            }
            mSurfaceView!!.resetCamera()
            mGLSurfaceView!!.setRenderConfig(mCameraRotate, mCameraMirror)
            mGLSurfaceView!!.gleS2Render.setViewDisplay(mCameraMirror, mCameraRotate)
        }
    }


    fun login(userName: String, userPWD: String) {
        val intent = Intent()
        if (userName == "admin" && !dbManager!!.isAccountExist(userName)) {
            //first time login with admin - admin/admin
            if (userPWD == "admin") {
                val strUserId = "00001"
                val strAccount = "admin"
                val strPassword = "admin"
                val iPower = SC_Const.ADMIN
                val account = UserAccount(strUserId, strAccount, strPassword, iPower, "admin", "", "0")
                dbManager?.addAccount(account)
                scApp?.setUserInfo(strUserId, strAccount, strPassword, iPower, "admin", "", "0")
                intent.setClass(this@DetecterActivity, MainActivity::class.java)
                startActivity(intent)
//                overridePendingTransition(0, 0)
            } else {
                val dialog = TopFalseDialog(this)
                dialog.window.setDimAmount(0f)
                dialog.show()
                dialog.window.setGravity(Gravity.TOP)
                val t = Timer()
                t.schedule(timerTask {
                    dialog.dismiss()
                    t.cancel()
                }, 3000)
                return
            }
        }
        if (!dbManager!!.isAccountExist(userName, userPWD)) {
            val dialog = TopFalseDialog(this)
            dialog.window.setDimAmount(0f)
            dialog.show()
            dialog.window.setGravity(Gravity.TOP)
            val t = Timer(true)
            t.schedule(timerTask {
                dialog.dismiss()
                t.cancel()
            }, 3000)
        } else {
            val userInfo = dbManager?.getUserAccount(userName, userPWD)
            scApp?.userInfo = userInfo

            //upload user login record to server


            if (userInfo?.statue != "1") {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val curDate = Date(System.currentTimeMillis())
                val str = formatter.format(curDate)
                if (dbManager!!.cabinetNo.size != 0) {
                    val upload: UploadRecordManager = UploadRecordManager(this)
                    upload.getCode(dbManager!!.cabinetNo.get(0).cabinetNo, "登陆", scApp!!.userInfo.userName, str,
                            "", "", "", "", "", "")

                }
//                val it = Intent(this, MyService::class.java)
//                startService(it)
                intent.setClass(this@DetecterActivity, MainActivity::class.java)
//                saveUserName(userName)
                startActivity(intent)
                overridePendingTransition(0, 0)

            } else {
                val dialog = TopFalseDialog(this)
                dialog.setTitle("该用户已被禁用")
                dialog.setMessage(" ")
                dialog.window.setDimAmount(0f)
                dialog.show()
                dialog.window.setGravity(Gravity.TOP)
                val t = Timer(true)
                t.schedule(timerTask {
                    dialog.dismiss()
                    t.cancel()
                }, 3000)
            }
        }
    }

}
