/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fitsum.posenet
import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.miguelrochefort.fitnesscamera.*
import com.miguelrochefort.fitnesscamera.lib.BodyPart
import com.miguelrochefort.fitnesscamera.lib.Person
import com.miguelrochefort.fitnesscamera.lib.Posenet
import org.fitsum.API.BadgeAPI
import org.fitsum.API.ChangeAPI
import org.fitsum.API.ExerciseAPI
import org.fitsum.API.ProfileAPI
import org.fitsum.Dto.BadgeDto
import org.fitsum.Dto.CommonResult
import org.fitsum.Dto.SingleResult
import org.fitsum.Dto.UserDto
import org.fitsum.MainActivity
import org.fitsum.RetrofitAPI.config.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class PosenetActivity :
  Fragment(),
  ActivityCompat.OnRequestPermissionsResultCallback {

  // 0 -> video + skeleton
  // 1 -> skeleton
  // 2 -> video
  private val PREVIEW_MODES = 3
  private var previewMode = 0

  /** List of body joints that should be connected.    */
  private val bodyJoints = listOf(
    Pair(BodyPart.LEFT_WRIST, BodyPart.LEFT_ELBOW),
    Pair(BodyPart.LEFT_ELBOW, BodyPart.LEFT_SHOULDER),
    Pair(BodyPart.LEFT_SHOULDER, BodyPart.RIGHT_SHOULDER),
    Pair(BodyPart.RIGHT_SHOULDER, BodyPart.RIGHT_ELBOW),
    Pair(BodyPart.RIGHT_ELBOW, BodyPart.RIGHT_WRIST),
    Pair(BodyPart.LEFT_SHOULDER, BodyPart.LEFT_HIP),
    Pair(BodyPart.LEFT_HIP, BodyPart.RIGHT_HIP),
    Pair(BodyPart.RIGHT_HIP, BodyPart.RIGHT_SHOULDER),
    Pair(BodyPart.LEFT_HIP, BodyPart.LEFT_KNEE),
    Pair(BodyPart.LEFT_KNEE, BodyPart.LEFT_ANKLE),
    Pair(BodyPart.RIGHT_HIP, BodyPart.RIGHT_KNEE),
    Pair(BodyPart.RIGHT_KNEE, BodyPart.RIGHT_ANKLE)
  )



  private var textView: TextView? = null
  private var fin_squart: Button? = null
  private var fin_pushup: Button? = null

  //푸쉬업 갯수를 api로 보낼때 필요한 변수 - 260
  private var pushup : Int ? =null
  private var squart : Int ?= null

  //코인 및 아이템 변경을 위한 변수
  private var item_s: String? = null
  private var coin_s: String? = null
  private var squart_s: String? = null
  private var pushup_s: String? = null


  private var item: Int? = null
  private var coin: Int? = null

  private var counter: RepetitionCounter? = null

  /** Threshold for confidence score. */
  private val minConfidence = 0.5

  /** Radius of circle used to draw keypoints.  */
  private val circleRadius = 8.0f

  /** Paint class holds the style and color information to draw geometries,text and bitmaps. */
  private var paint = Paint()

  /** A shape for extracting frame data.   */
  private val PREVIEW_WIDTH = 640
  private val PREVIEW_HEIGHT = 480

  /** An object for the Posenet library.    */
  private lateinit var posenet: Posenet

  /** ID of the current [CameraDevice].   */
  private var cameraId: String? = null

  /** A [SurfaceView] for camera preview.   */
  private var surfaceView: SurfaceView? = null

  /** A [CameraCaptureSession] for camera preview.   */
  private var captureSession: CameraCaptureSession? = null

  /** A reference to the opened [CameraDevice].    */
  private var cameraDevice: CameraDevice? = null

  /** The [android.util.Size] of camera preview.  */
  private var previewSize: Size? = null

  /** The [android.util.Size.getWidth] of camera preview. */
  private var previewWidth = 0

  /** The [android.util.Size.getHeight] of camera preview.  */
  private var previewHeight = 0

  /** A counter to keep count of total frames.  */
  private var frameCounter = 0

  /** An IntArray to save image data in ARGB8888 format  */
  private lateinit var rgbBytes: IntArray

  /** A ByteArray to save image data in YUV format  */
  private var yuvBytes = arrayOfNulls<ByteArray>(3)

  /** An additional thread for running tasks that shouldn't block the UI.   */
  private var backgroundThread: HandlerThread? = null

  /** A [Handler] for running tasks in the background.    */
  private var backgroundHandler: Handler? = null

  /** An [ImageReader] that handles preview frame capture.   */
  private var imageReader: ImageReader? = null

  /** [CaptureRequest.Builder] for the camera preview   */
  private var previewRequestBuilder: CaptureRequest.Builder? = null

  /** [CaptureRequest] generated by [.previewRequestBuilder   */
  private var previewRequest: CaptureRequest? = null

  /** A [Semaphore] to prevent the app from exiting before closing the camera.    */
  private val cameraOpenCloseLock = Semaphore(1)

  /** Whether the current camera device supports Flash or not.    */
  private var flashSupported = false

  /** Orientation of the camera sensor.   */
  private var sensorOrientation: Int? = null

  /** Abstract interface to someone holding a display surface.    */
  private var surfaceHolder: SurfaceHolder? = null

  /** [CameraDevice.StateCallback] is called when [CameraDevice] changes its state.   */
  private val stateCallback = object : CameraDevice.StateCallback() {

    override fun onOpened(cameraDevice: CameraDevice) {
      cameraOpenCloseLock.release()
      this@PosenetActivity.cameraDevice = cameraDevice
      createCameraPreviewSession()
    }

    override fun onDisconnected(cameraDevice: CameraDevice) {
      cameraOpenCloseLock.release()
      cameraDevice.close()
      this@PosenetActivity.cameraDevice = null
    }

    override fun onError(cameraDevice: CameraDevice, error: Int) {
      onDisconnected(cameraDevice)
      this@PosenetActivity.activity?.finish()
    }
  }

  /**
   * A [CameraCaptureSession.CaptureCallback] that handles events related to JPEG capture.
   */
  private val captureCallback = object : CameraCaptureSession.CaptureCallback() {
    override fun onCaptureProgressed(
      session: CameraCaptureSession,
      request: CaptureRequest,
      partialResult: CaptureResult
    ) {
    }

    override fun onCaptureCompleted(
      session: CameraCaptureSession,
      request: CaptureRequest,
      result: TotalCaptureResult
    ) {
    }
  }

  /**
   * Shows a [Toast] on the UI thread.
   *
   * @param text The message to show
   */
  private fun showToast(text: String) {
    val activity = activity
    activity?.runOnUiThread { Toast.makeText(activity, text, Toast.LENGTH_SHORT).show() }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? = inflater.inflate(R.layout.tfe_pn_activity_posenet, container, false)




  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    fin_squart = view.findViewById(R.id.fin_squart)       //스쿼트 종료버튼
    fin_pushup = view.findViewById(R.id.fin_pushup)       //푸쉬업 종료버튼
    textView = view.findViewById(R.id.textView)           // 숫자 나오는 텍스트뷰
    surfaceView = view.findViewById(R.id.surfaceView)
    surfaceHolder = surfaceView!!.holder
    counter = RepetitionCounter(this.requireContext())



    //숫자 누르면 1씩 증가 나중에 주석처리 해야함.
    textView?.setOnClickListener { view ->
      counter?.plus()
    }

    //스쿼트 종료 버튼을 누르면
    fin_squart?.setOnClickListener {

      squart_s = counter?.count.toString()
      try{
        squart = squart_s!!.toInt()
      }catch (e: NumberFormatException){
        print("Not number: $squart_s")
      }



      val builder = AlertDialog.Builder(activity)
        .setTitle("스쿼트 종료")
        .setMessage("운동을 종료하시겠습니까?")
        .setPositiveButton("종료"){ dialog, which ->
          //ExerciseAPI 를 이용해 값을 전송하는 메서드 호출
          val exerciseAPI = RetrofitBuilder.getRetrofit().create(ExerciseAPI::class.java)
          var profileAPI = RetrofitBuilder.getRetrofit().create(ProfileAPI::class.java)
          var changeAPI = RetrofitBuilder.getRetrofit().create(ChangeAPI::class.java)

          var changeUserItemDto = UserDto.ChangeUserItemDto() //유저 현재상태 옷 변경할때 필요
          var changeUserCoinDto = UserDto.ChangeUserCoinDto() //유저 코인 갯수 차감시 필요

          val badgeAPI = RetrofitBuilder.getRetrofit().create(BadgeAPI::class.java)
          val changeBadge2 = BadgeDto.ChangeBadge2()
          changeBadge2.setnewBadge2(true)
          badgeAPI.badge2(changeBadge2).enqueue(object : Callback<CommonResult?> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult?>) {}
            override fun onFailure(call: Call<CommonResult?>, t: Throwable) {}
          })



          profileAPI.user_item.enqueue(object : Callback<SingleResult<*>> {
            override fun onResponse(
              call: Call<SingleResult<*>>,
              response: Response<SingleResult<*>>
            ) {
              if (response.isSuccessful) {
                val data = response.body()!!
                item_s = data.data.toString() //item_s 는 그냥 데이터를 받기위한 변수 String으로 받음
                item = item_s!!.toDouble()
                  .toInt() //item 은 그것을 받아서 정수형으로 바꿔주는 역할. (실직적 item 숫자가 들어있음)
                Log.d("item", item.toString())

                //현재 캐릭터의 옷상태가 기본 옷이고 남자라면 3번으로 변경
                if (item == 1 || item == 2 || item == 3) {
                  changeUserItemDto.curUserItem = item
                  changeUserItemDto.newUserItem = 3
                  //유저 옷 상태 변경
                  changeAPI.updateUserItem(changeUserItemDto)
                    .enqueue(object : Callback<SingleResult<*>?> {
                      override fun onResponse(
                        call: Call<SingleResult<*>?>,
                        response: Response<SingleResult<*>?>
                      ) {
                      }

                      override fun onFailure(
                        call: Call<SingleResult<*>?>,
                        t: Throwable
                      ) {
                      }
                    })
                }
                //현재 캐릭터의 옷상태가 운동복이고 남자라면 6번으로 변경
                else if (item == 4 || item == 5 || item == 6) {
                  //남자 옷 기본 옷으로 바꾸기
                  changeUserItemDto.curUserItem = item
                  changeUserItemDto.newUserItem = 6
                  //유저 옷상태를 변경하기 위한 API
                  changeAPI.updateUserItem(changeUserItemDto)
                    .enqueue(object : Callback<SingleResult<*>?> {
                      override fun onResponse(
                        call: Call<SingleResult<*>?>,
                        response: Response<SingleResult<*>?>
                      ) {
                      }

                      override fun onFailure(
                        call: Call<SingleResult<*>?>,
                        t: Throwable
                      ) {
                      }
                    })
                }
                //만약 트레이닝복에 남자라면 9번으로 변경
                else if (item == 7 || item == 8 || item == 9) {
                  changeUserItemDto.curUserItem = item
                  changeUserItemDto.newUserItem = 9
                  //유저 옷상태를 변경하기 위한 API
                  changeAPI.updateUserItem(changeUserItemDto)
                    .enqueue(object : Callback<SingleResult<*>?> {
                      override fun onResponse(
                        call: Call<SingleResult<*>?>,
                        response: Response<SingleResult<*>?>
                      ) {
                      }

                      override fun onFailure(
                        call: Call<SingleResult<*>?>,
                        t: Throwable
                      ) {
                      }
                    })
                }
                //기본옷에 여자라면 12번으로 변경
                else if (item == 10 || item == 11 || item == 12) {
                  changeUserItemDto.curUserItem = item
                  changeUserItemDto.newUserItem = 12
                  //유저 옷상태를 변경하기 위한 API
                  changeAPI.updateUserItem(changeUserItemDto)
                    .enqueue(object : Callback<SingleResult<*>?> {
                      override fun onResponse(
                        call: Call<SingleResult<*>?>,
                        response: Response<SingleResult<*>?>
                      ) {
                      }

                      override fun onFailure(
                        call: Call<SingleResult<*>?>,
                        t: Throwable
                      ) {
                      }
                    })
                }
                //운동복 에 여자라면 15번으로 변경
                else if (item == 13 || item == 14 || item == 15) {
                  changeUserItemDto.curUserItem = item
                  changeUserItemDto.newUserItem = 15
                  //유저 옷상태를 변경하기 위한 API
                  changeAPI.updateUserItem(changeUserItemDto)
                    .enqueue(object : Callback<SingleResult<*>?> {
                      override fun onResponse(
                        call: Call<SingleResult<*>?>,
                        response: Response<SingleResult<*>?>
                      ) {
                      }

                      override fun onFailure(
                        call: Call<SingleResult<*>?>,
                        t: Throwable
                      ) {
                      }
                    })
                }
                //트레이닝복에 여자라면 18번으로 변경
                else if (item == 16 || item == 17 || item == 18) {
                  changeUserItemDto.curUserItem = item
                  changeUserItemDto.newUserItem = 18
                  //유저 옷상태를 변경하기 위한 API
                  changeAPI.updateUserItem(changeUserItemDto)
                    .enqueue(object : Callback<SingleResult<*>?> {
                      override fun onResponse(
                        call: Call<SingleResult<*>?>,
                        response: Response<SingleResult<*>?>
                      ) {
                      }

                      override fun onFailure(
                        call: Call<SingleResult<*>?>,
                        t: Throwable
                      ) {
                      }
                    })
                }
              }
            }

            override fun onFailure(call: Call<SingleResult<*>>, t: Throwable) {}
          })
          exerciseAPI.squart(squart_s).enqueue(object : Callback<CommonResult?> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult?>) {
              if (response.isSuccessful) {
                val data = response.body()
              }
            }

            override fun onFailure(call: Call<CommonResult?>, t: Throwable) {}
          })

          //코인값 받아오기
          profileAPI.user_coin.enqueue(object : Callback<SingleResult<*>> {
            override fun onResponse(
              call: Call<SingleResult<*>>,
              response: Response<SingleResult<*>>
            ) {
              if (response.isSuccessful) {
                val data = response.body()!!
                coin_s = data.data.toString() //coin_s 는 그냥 데이터를 받기위한 변수 String으로 받음
                coin = coin_s!!.toDouble().toInt() //coin 은 그것을 받아서 정수형으로 바꿔주는 역할.


                if(squart!! > 50){
                  changeUserCoinDto.setCurUserCoin(coin)
                  changeUserCoinDto.setNewUserCoin(coin!!+ 30)

                  changeAPI.updateUserCoin(changeUserCoinDto).enqueue(object : Callback<SingleResult<*>?> {
                    override fun onResponse(
                      call: Call<SingleResult<*>?>,
                      response: Response<SingleResult<*>?>
                    ) {
                    }

                    override fun onFailure(call: Call<SingleResult<*>?>, t: Throwable) {}
                  })
                }
                Log.d("coin", data.data.toString())
              }
            }
            override fun onFailure(call: Call<SingleResult<*>>, t: Throwable) {}
          })

          //메인 엑티비티로 전환
          val nextIntent = Intent(context, MainActivity::class.java)
          startActivity(nextIntent)
        }
        .setNegativeButton("취소",null).show()

    }


    //푸쉬업 종료 버튼을 누르면
    fin_pushup?.setOnClickListener {
      pushup_s = counter?.count.toString()
      try{
        pushup = pushup_s!!.toInt()
      }catch (e: NumberFormatException){
        print("Not number: $pushup_s")
      }

      val builder = AlertDialog.Builder(activity)
        .setTitle("푸쉬업 종료")
        .setMessage("운동을 종료하시겠습니까?")
        .setPositiveButton("종료"){ dialog, which ->
          //ExerciseAPI 를 이용해 값을 전송하는 메서드 호출
          val exerciseAPI = RetrofitBuilder.getRetrofit().create(ExerciseAPI::class.java)
          var profileAPI = RetrofitBuilder.getRetrofit().create(ProfileAPI::class.java)
          var changeAPI = RetrofitBuilder.getRetrofit().create(ChangeAPI::class.java)

          var changeUserItemDto = UserDto.ChangeUserItemDto() //유저 현재상태 옷 변경할때 필요
          var changeUserCoinDto = UserDto.ChangeUserCoinDto() //유저 코인 갯수 차감시 필요

          val badgeAPI = RetrofitBuilder.getRetrofit().create(BadgeAPI::class.java)
          val changeBadge2 = BadgeDto.ChangeBadge2()
          changeBadge2.setnewBadge2(true)
          badgeAPI.badge2(changeBadge2).enqueue(object : Callback<CommonResult?> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult?>) {}
            override fun onFailure(call: Call<CommonResult?>, t: Throwable) {}
          })

          //유저의 현재 옷상태를 가져와서 비교해야함.
          profileAPI.user_item.enqueue(object : Callback<SingleResult<*>> {
            override fun onResponse(
              call: Call<SingleResult<*>>,
              response: Response<SingleResult<*>>
            ) {
              if (response.isSuccessful) {
                val data = response.body()!!
                item_s = data.data.toString() //item_s 는 그냥 데이터를 받기위한 변수 String으로 받음
                item = item_s!!.toDouble()
                  .toInt() //item 은 그것을 받아서 정수형으로 바꿔주는 역할. (실직적 item 숫자가 들어있음)
                Log.d("item", item.toString())

                //현재 캐릭터의 옷상태가 기본 옷이고 남자라면 2번으로 변경
                if (item == 1 || item == 2 || item == 3) {
                  changeUserItemDto.curUserItem = item
                  changeUserItemDto.newUserItem = 2
                  //유저 옷 상태 변경
                  changeAPI.updateUserItem(changeUserItemDto)
                    .enqueue(object : Callback<SingleResult<*>?> {
                      override fun onResponse(
                        call: Call<SingleResult<*>?>,
                        response: Response<SingleResult<*>?>
                      ) {
                      }

                      override fun onFailure(
                        call: Call<SingleResult<*>?>,
                        t: Throwable
                      ) {
                      }
                    })
                } else if (item == 4 || item == 5 || item == 6) {
                  //남자 옷 기본 옷으로 바꾸기
                  changeUserItemDto.curUserItem = item
                  changeUserItemDto.newUserItem = 5
                  //유저 옷상태를 변경하기 위한 API
                  changeAPI.updateUserItem(changeUserItemDto)
                    .enqueue(object : Callback<SingleResult<*>?> {
                      override fun onResponse(
                        call: Call<SingleResult<*>?>,
                        response: Response<SingleResult<*>?>
                      ) {
                      }

                      override fun onFailure(
                        call: Call<SingleResult<*>?>,
                        t: Throwable
                      ) {
                      }
                    })
                }
                //만약 트레이닝복에 남자라면 8번으로 변경
                else if (item == 7 || item == 8 || item == 9) {
                  changeUserItemDto.curUserItem = item
                  changeUserItemDto.newUserItem = 8
                  //유저 옷상태를 변경하기 위한 API
                  changeAPI.updateUserItem(changeUserItemDto)
                    .enqueue(object : Callback<SingleResult<*>?> {
                      override fun onResponse(
                        call: Call<SingleResult<*>?>,
                        response: Response<SingleResult<*>?>
                      ) {
                      }

                      override fun onFailure(
                        call: Call<SingleResult<*>?>,
                        t: Throwable
                      ) {
                      }
                    })
                }
                //기본옷에 여자라면 11번으로 변경
                else if (item == 10 || item == 11 || item == 12) {
                  changeUserItemDto.curUserItem = item
                  changeUserItemDto.newUserItem = 11
                  //유저 옷상태를 변경하기 위한 API
                  changeAPI.updateUserItem(changeUserItemDto)
                    .enqueue(object : Callback<SingleResult<*>?> {
                      override fun onResponse(
                        call: Call<SingleResult<*>?>,
                        response: Response<SingleResult<*>?>
                      ) {
                      }

                      override fun onFailure(
                        call: Call<SingleResult<*>?>,
                        t: Throwable
                      ) {
                      }
                    })
                }
                //운동복 에 여자라면 14번으로 변경
                else if (item == 13 || item == 14 || item == 15) {
                  changeUserItemDto.curUserItem = item
                  changeUserItemDto.newUserItem = 14
                  //유저 옷상태를 변경하기 위한 API
                  changeAPI.updateUserItem(changeUserItemDto)
                    .enqueue(object : Callback<SingleResult<*>?> {
                      override fun onResponse(
                        call: Call<SingleResult<*>?>,
                        response: Response<SingleResult<*>?>
                      ) {
                      }

                      override fun onFailure(
                        call: Call<SingleResult<*>?>,
                        t: Throwable
                      ) {
                      }
                    })
                }
                //트레이닝복에 여자라면 17번으로 변경
                else if (item == 16 || item == 17 || item == 18) {
                  changeUserItemDto.curUserItem = item
                  changeUserItemDto.newUserItem = 17
                  //유저 옷상태를 변경하기 위한 API
                  changeAPI.updateUserItem(changeUserItemDto)
                    .enqueue(object : Callback<SingleResult<*>?> {
                      override fun onResponse(
                        call: Call<SingleResult<*>?>,
                        response: Response<SingleResult<*>?>
                      ) {
                      }

                      override fun onFailure(
                        call: Call<SingleResult<*>?>,
                        t: Throwable
                      ) {
                      }
                    })
                }
              }
            }

            override fun onFailure(call: Call<SingleResult<*>>, t: Throwable) {}
          })

          exerciseAPI.pushup(pushup_s).enqueue(object : Callback<CommonResult?> {
            override fun onResponse(call: Call<CommonResult?>, response: Response<CommonResult?>) {
              if (response.isSuccessful) {
                val data = response.body()
              }
            }

            override fun onFailure(call: Call<CommonResult?>, t: Throwable) {}
          })

          //코인값 받아오기
          profileAPI.user_coin.enqueue(object : Callback<SingleResult<*>> {
            override fun onResponse(
              call: Call<SingleResult<*>>,
              response: Response<SingleResult<*>>
            ) {
              if (response.isSuccessful) {
                val data = response.body()!!
                coin_s = data.data.toString() //coin_s 는 그냥 데이터를 받기위한 변수 String으로 받음
                coin = coin_s!!.toDouble().toInt() //coin 은 그것을 받아서 정수형으로 바꿔주는 역할.


                if(pushup!! >= 50){
                  changeUserCoinDto.setCurUserCoin(coin)
                  changeUserCoinDto.setNewUserCoin(coin!!+ 30)

                  changeAPI.updateUserCoin(changeUserCoinDto).enqueue(object : Callback<SingleResult<*>?> {
                    override fun onResponse(
                      call: Call<SingleResult<*>?>,
                      response: Response<SingleResult<*>?>
                    ) {
                    }

                    override fun onFailure(call: Call<SingleResult<*>?>, t: Throwable) {}
                  })
                }
                Log.d("coin", data.data.toString())
              }
            }
            override fun onFailure(call: Call<SingleResult<*>>, t: Throwable) {}
          })

          val nextIntent = Intent(context, MainActivity::class.java)
          startActivity(nextIntent)
        }
        .setNegativeButton("취소",null).show()

    }

    surfaceView?.setOnClickListener { view ->
      previewMode = (previewMode + 1) % PREVIEW_MODES
    }
  }




  override fun onResume() {
    super.onResume()
    startBackgroundThread()
  }

  override fun onStart() {
    super.onStart()
    openCamera()
    posenet = Posenet(this.requireContext())
  }

  override fun onPause() {
    closeCamera()
    stopBackgroundThread()
    super.onPause()
  }

  override fun onDestroy() {
    super.onDestroy()
    posenet.close()
  }

  private fun requestCameraPermission() {
    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
      ConfirmationDialog().show(childFragmentManager, FRAGMENT_DIALOG)
    } else {
      requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
    }
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    if (requestCode == REQUEST_CAMERA_PERMISSION) {
      if (allPermissionsGranted(grantResults)) {
        ErrorDialog.newInstance(getString(R.string.tfe_pn_request_permission))
          .show(childFragmentManager, FRAGMENT_DIALOG)
      }
    } else {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
  }

  private fun allPermissionsGranted(grantResults: IntArray) = grantResults.all {
    it == PackageManager.PERMISSION_GRANTED
  }

  /**
   * Sets up member variables related to camera.
   */
  private fun setUpCameraOutputs() {

    val activity = activity
    val manager = activity!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    try {
      for (cameraId in manager.cameraIdList) {
        val characteristics = manager.getCameraCharacteristics(cameraId)

        // We don't use a back facing camera in this sample.
        val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
        if (cameraDirection != null &&
          cameraDirection == CameraCharacteristics.LENS_FACING_BACK
        ) {
          continue
        }

        previewSize = Size(PREVIEW_WIDTH, PREVIEW_HEIGHT)

        imageReader = ImageReader.newInstance(
          PREVIEW_WIDTH, PREVIEW_HEIGHT,
          ImageFormat.YUV_420_888, /*maxImages*/ 2
        )

        sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!

        previewHeight = previewSize!!.height
        previewWidth = previewSize!!.width

        setVideoSize()

        // Initialize the storage bitmaps once when the resolution is known.
        rgbBytes = IntArray(previewWidth * previewHeight)

        // Check if the flash is supported.
        flashSupported =
          characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true

        this.cameraId = cameraId

        // We've found a viable camera and finished setting up member variables,
        // so we don't need to iterate through other available cameras.
        return
      }
    } catch (e: CameraAccessException) {
      Log.e(TAG, e.toString())
    } catch (e: NullPointerException) {
      // Currently an NPE is thrown when the Camera2API is used but not supported on the
      // device this code runs.
      ErrorDialog.newInstance(getString(R.string.tfe_pn_camera_error))
        .show(childFragmentManager, FRAGMENT_DIALOG)
    }
  }

  private fun setVideoSize() {
    //val ratio = previewWidth.toFloat() / previewHeight.toFloat()
    val screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels
    //val screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels
    surfaceHolder!!.setFixedSize(screenWidth, screenWidth)
  }

  /**
   * Opens the camera specified by [PosenetActivity.cameraId].
   */
  private fun openCamera() {
    val permissionCamera = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
    if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
      requestCameraPermission()
    }
    setUpCameraOutputs()
    val manager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
    try {
      // Wait for camera to open - 2.5 seconds is sufficient
      if (!cameraOpenCloseLock.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
        throw RuntimeException("Time out waiting to lock camera opening.")
      }
      manager.openCamera(cameraId!!, stateCallback, backgroundHandler)
    } catch (e: CameraAccessException) {
      Log.e(TAG, e.toString())
    } catch (e: InterruptedException) {
      throw RuntimeException("Interrupted while trying to lock camera opening.", e)
    }
  }

  /**
   * Closes the current [CameraDevice].
   */
  private fun closeCamera() {
    if (captureSession == null) {
      return
    }

    try {
      cameraOpenCloseLock.acquire()
      captureSession!!.close()
      captureSession = null
      cameraDevice!!.close()
      cameraDevice = null
      imageReader!!.close()
      imageReader = null
    } catch (e: InterruptedException) {
      throw RuntimeException("Interrupted while trying to lock camera closing.", e)
    } finally {
      cameraOpenCloseLock.release()
    }
  }

  /**
   * Starts a background thread and its [Handler].
   */
  private fun startBackgroundThread() {
    backgroundThread = HandlerThread("imageAvailableListener").also { it.start() }
    backgroundHandler = Handler(backgroundThread!!.looper)
  }

  /**
   * Stops the background thread and its [Handler].
   */
  private fun stopBackgroundThread() {
    backgroundThread?.quitSafely()
    try {
      backgroundThread?.join()
      backgroundThread = null
      backgroundHandler = null
    } catch (e: InterruptedException) {
      Log.e(TAG, e.toString())
    }
  }

  /** Fill the yuvBytes with data from image planes.   */
  private fun fillBytes(planes: Array<Image.Plane>, yuvBytes: Array<ByteArray?>) {
    // Row stride is the total number of bytes occupied in memory by a row of an image.
    // Because of the variable row stride it's not possible to know in
    // advance the actual necessary dimensions of the yuv planes.
    for (i in planes.indices) {
      val buffer = planes[i].buffer
      if (yuvBytes[i] == null) {
        yuvBytes[i] = ByteArray(buffer.capacity())
      }
      buffer.get(yuvBytes[i]!!)
    }
  }

  /** A [OnImageAvailableListener] to receive frames as they are available.  */
  private var imageAvailableListener = object : OnImageAvailableListener {
    override fun onImageAvailable(imageReader: ImageReader) {
      // We need wait until we have some size from onPreviewSizeChosen
      if (previewWidth == 0 || previewHeight == 0) {
        return
      }

      val image = imageReader.acquireLatestImage() ?: return
      fillBytes(image.planes, yuvBytes)

      ImageUtils.convertYUV420ToARGB8888(
        yuvBytes[0]!!,
        yuvBytes[1]!!,
        yuvBytes[2]!!,
        previewWidth,
        previewHeight,
        /*yRowStride=*/ image.planes[0].rowStride,
        /*uvRowStride=*/ image.planes[1].rowStride,
        /*uvPixelStride=*/ image.planes[1].pixelStride,
        rgbBytes
      )

      // Create bitmap from int array
      val imageBitmap = Bitmap.createBitmap(
        rgbBytes, previewWidth, previewHeight,
        Bitmap.Config.ARGB_8888
      )

      // Create rotated version for portrait display
      val rotateMatrix = Matrix()
      rotateMatrix.postRotate(270.0f)
      rotateMatrix.postScale(-1f, 1f) // Mirror

      val rotatedBitmap = Bitmap.createBitmap(
        imageBitmap, 0, 0, previewWidth, previewHeight,
        rotateMatrix, true
      )
      image.close()

      processImage(rotatedBitmap)
    }
  }

  /** Crop Bitmap to maintain aspect ratio of model input.   */
  private fun cropBitmap(bitmap: Bitmap): Bitmap {
    val bitmapRatio = bitmap.height.toFloat() / bitmap.width
    val modelInputRatio = MODEL_HEIGHT.toFloat() / MODEL_WIDTH
    var croppedBitmap = bitmap

    // Acceptable difference between the modelInputRatio and bitmapRatio to skip cropping.
    val maxDifference = 1e-5

    // Checks if the bitmap has similar aspect ratio as the required model input.
    when {
      abs(modelInputRatio - bitmapRatio) < maxDifference -> return croppedBitmap
      modelInputRatio < bitmapRatio -> {
        // New image is taller so we are height constrained.
        val cropHeight = bitmap.height - (bitmap.width.toFloat() / modelInputRatio)
        croppedBitmap = Bitmap.createBitmap(
          bitmap,
          0,
          (cropHeight / 2).toInt(),
          bitmap.width,
          (bitmap.height - cropHeight).toInt()
        )
      }
      else -> {
        val cropWidth = bitmap.width - (bitmap.height.toFloat() * modelInputRatio)
        croppedBitmap = Bitmap.createBitmap(
          bitmap,
          (cropWidth / 2).toInt(),
          0,
          (bitmap.width - cropWidth).toInt(),
          bitmap.height
        )
      }
    }
    return croppedBitmap
  }

  /** Set the paint color and size.    */
  private fun setPaint() {
    paint.color = Color.WHITE
    paint.textSize = 80.0f
    paint.strokeWidth = 8.0f
  }

  /** Draw bitmap on Canvas.   */
  private fun draw(canvas: Canvas, person: Person, bitmap: Bitmap) {
    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    // Draw `bitmap` and `person` in square canvas.
    val screenWidth: Int
    val screenHeight: Int
    val left: Int
    val right: Int
    val top: Int
    val bottom: Int
    if (canvas.height > canvas.width) {
      screenWidth = canvas.width
      screenHeight = canvas.width
      left = 0
      top = (canvas.height - canvas.width) / 2
    } else {
      screenWidth = canvas.height
      screenHeight = canvas.height
      left = (canvas.width - canvas.height) / 2
      top = 0
    }
    right = left + screenWidth
    bottom = top + screenHeight

    setPaint()
    if (previewMode == 0 || previewMode == 2) {
      canvas.drawBitmap(
        bitmap,
        Rect(0, 0, bitmap.width, bitmap.height),
        Rect(left, top, right, bottom),
        paint
      )
    }

    val widthRatio = screenWidth.toFloat() / MODEL_WIDTH
    val heightRatio = screenHeight.toFloat() / MODEL_HEIGHT

    if (previewMode == 0 || previewMode == 1) {
      // Draw key points over the image.
      for (keyPoint in person.keyPoints) {
        if (keyPoint.score > minConfidence) {
          val position = keyPoint.position
          val adjustedX: Float = position.x.toFloat() * widthRatio + left
          val adjustedY: Float = position.y.toFloat() * heightRatio + top
          canvas.drawCircle(adjustedX, adjustedY, circleRadius, paint)
        }
      }

      for (line in bodyJoints) {
        if (
          (person.keyPoints[line.first.ordinal].score > minConfidence) and
          (person.keyPoints[line.second.ordinal].score > minConfidence)
        ) {
          canvas.drawLine(
            person.keyPoints[line.first.ordinal].position.x.toFloat() * widthRatio + left,
            person.keyPoints[line.first.ordinal].position.y.toFloat() * heightRatio + top,
            person.keyPoints[line.second.ordinal].position.x.toFloat() * widthRatio + left,
            person.keyPoints[line.second.ordinal].position.y.toFloat() * heightRatio + top,
            paint
          )
        }
      }
    }

//    canvas.drawText(
//      "Score: %.2f".format(person.score),
//      (15.0f * widthRatio),
//      (30.0f * heightRatio + bottom),
//      paint
//    )
//    canvas.drawText(
//      "Device: %s".format(posenet.device),
//      (15.0f * widthRatio),
//      (50.0f * heightRatio + bottom),
//      paint
//    )
//    canvas.drawText(
//      "Time: %.2f ms".format(posenet.lastInferenceTimeNanos * 1.0f / 1_000_000),
//      (15.0f * widthRatio),
//      (70.0f * heightRatio + bottom),
//      paint
//    )

    // Draw!
    surfaceHolder!!.unlockCanvasAndPost(canvas)
  }

  /** Process image using Posenet library.   */
  private fun processImage(bitmap: Bitmap) {
    // Crop bitmap.
    val croppedBitmap = cropBitmap(bitmap)

    // Created scaled version of bitmap for model input.
    val scaledBitmap = Bitmap.createScaledBitmap(croppedBitmap, MODEL_WIDTH, MODEL_HEIGHT, true)

    // Perform inference.
    var person = posenet.estimateSinglePose(scaledBitmap)
    person = swapBodyParts(person)
    val canvas: Canvas = surfaceHolder!!.lockCanvas()
    val count = counter?.OnFrame(person)
    this.requireActivity().runOnUiThread{
      textView!!.text = (count).toString()
    }
    draw(canvas, person, scaledBitmap)
  }

  // To prevent horizontal miscategorization of body parts,
  // and assuming that the user is always facing the camera,
  // we make sure that body parts are on the right side of the body.
  private fun swapBodyParts(person: Person): Person {
    swap(person, BodyPart.LEFT_SHOULDER, BodyPart.RIGHT_SHOULDER);
    swap(person, BodyPart.LEFT_ANKLE, BodyPart.RIGHT_ANKLE);
    swap(person, BodyPart.LEFT_EAR, BodyPart.RIGHT_EAR);
    swap(person, BodyPart.LEFT_ELBOW, BodyPart.RIGHT_ELBOW);
    swap(person, BodyPart.LEFT_EYE, BodyPart.RIGHT_EYE);
    swap(person, BodyPart.LEFT_HIP, BodyPart.RIGHT_HIP);
    swap(person, BodyPart.LEFT_KNEE, BodyPart.RIGHT_KNEE);
    swap(person, BodyPart.LEFT_WRIST, BodyPart.RIGHT_WRIST);

    return person;
  }

  private fun swap(person: Person, left: BodyPart, right: BodyPart) {
    if (person.keyPoints[right.ordinal].score >= minConfidence
      && person.keyPoints[left.ordinal].score >= minConfidence
      && person.keyPoints[right.ordinal].position.x > person.keyPoints[left.ordinal].position.x) {
      //Log.i("posenet", "Swapping ${left} and ${right}.");
      val temp = person.keyPoints[right.ordinal]
      person.keyPoints[right.ordinal] = person.keyPoints[left.ordinal]
      person.keyPoints[left.ordinal] = temp
    }
  }

  /**
   * Creates a new [CameraCaptureSession] for camera preview.
   */
  private fun createCameraPreviewSession() {
    try {

      // We capture images from preview in YUV format.
      imageReader = ImageReader.newInstance(
        previewSize!!.width, previewSize!!.height, ImageFormat.YUV_420_888, 2
      )
      imageReader!!.setOnImageAvailableListener(imageAvailableListener, backgroundHandler)

      // This is the surface we need to record images for processing.
      val recordingSurface = imageReader!!.surface

      // We set up a CaptureRequest.Builder with the output Surface.
      previewRequestBuilder = cameraDevice!!.createCaptureRequest(
        CameraDevice.TEMPLATE_PREVIEW
      )
      previewRequestBuilder!!.addTarget(recordingSurface)

      // Here, we create a CameraCaptureSession for camera preview.
      cameraDevice!!.createCaptureSession(
        listOf(recordingSurface),
        object : CameraCaptureSession.StateCallback() {
          override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
            // The camera is already closed
            if (cameraDevice == null) return

            // When the session is ready, we start displaying the preview.
            captureSession = cameraCaptureSession
            try {
              // Auto focus should be continuous for camera preview.
              previewRequestBuilder!!.set(
                CaptureRequest.CONTROL_AF_MODE,
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
              )
              // Flash is automatically enabled when necessary.
              setAutoFlash(previewRequestBuilder!!)

              // Finally, we start displaying the camera preview.
              previewRequest = previewRequestBuilder!!.build()
              captureSession!!.setRepeatingRequest(
                previewRequest!!,
                captureCallback, backgroundHandler
              )
            } catch (e: CameraAccessException) {
              Log.e(TAG, e.toString())
            }
          }

          override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
            showToast("Failed")
          }
        },
        null
      )
    } catch (e: CameraAccessException) {
      Log.e(TAG, e.toString())
    }
  }

  private fun setAutoFlash(requestBuilder: CaptureRequest.Builder) {
    if (flashSupported) {
      requestBuilder.set(
        CaptureRequest.CONTROL_AE_MODE,
        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
      )
    }
  }

  /**
   * Shows an error message dialog.
   */
  class ErrorDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
      AlertDialog.Builder(activity)
        .setMessage(requireArguments().getString(ARG_MESSAGE))
        .setPositiveButton(android.R.string.ok) { _, _ -> requireActivity().finish() }
        .create()

    companion object {

      @JvmStatic
      private val ARG_MESSAGE = "message"

      @JvmStatic
      fun newInstance(message: String): ErrorDialog = ErrorDialog().apply {
        arguments = Bundle().apply { putString(ARG_MESSAGE, message) }
      }
    }
  }

  companion object {
    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private val ORIENTATIONS = SparseIntArray()
    private val FRAGMENT_DIALOG = "dialog"

    init {
      ORIENTATIONS.append(Surface.ROTATION_0, 90)
      ORIENTATIONS.append(Surface.ROTATION_90, 0)
      ORIENTATIONS.append(Surface.ROTATION_180, 270)
      ORIENTATIONS.append(Surface.ROTATION_270, 180)
    }

    /**
     * Tag for the [Log].
     */
    private const val TAG = "PosenetActivity"
  }
}