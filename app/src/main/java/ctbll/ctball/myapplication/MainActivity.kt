package ctbll.ctball.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.onesignal.OneSignal
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.UnityAds
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize
import kotlin.math.absoluteValue

const val ONESIGNAL_APP_ID = "########-####-####-####-############"

class MainActivity : AppCompatActivity(), IUnityAdsInitializationListener {


    var unityGameID = "4245531"
    var testMode = true
    var topAdUnitId = "CpuBanner"

    private val model: QuicklyViewModel by viewModels()
    lateinit var imBall: ImageView
    lateinit var imPipe: ImageView
    lateinit var stctch: ImageView
    var scorress = 0


    // Listener for banner events:
    private val bannerListener: BannerView.IListener = object : BannerView.IListener {
        override fun onBannerLoaded(bannerAdView: BannerView) {
            // Called when the banner is loaded.
            //Log.v("UnityAdsExample", "onBannerLoaded: " + bannerAdView.placementId)
            // Enable the correct button to hide the ad
            bannerAdView.visibility = View.VISIBLE

        }

        override fun onBannerFailedToLoad(bannerAdView: BannerView, errorInfo: BannerErrorInfo) {
       }

        override fun onBannerClick(bannerAdView: BannerView) {
        }

        override fun onBannerLeftApplication(bannerAdView: BannerView) {
        }
    }


    lateinit var topBanner: BannerView
    lateinit var topBannerView: RelativeLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

        // Initialize Unity Ads:
        UnityAds.initialize(applicationContext, unityGameID, testMode, this);



        setTitle("Score: 0")
        model.widthDp = (resources.displayMetrics.run { widthPixels  }).toFloat() /// density
        model.heightDp = (resources.displayMetrics.run { heightPixels  }).toFloat()/// density


        imBall = findViewById(R.id.imageViewBall)
        //imPipe = findViewById(R.id.imageViewPipe)
        stctch = findViewById(R.id.imageViewStCacth)
        stctch.setImageResource(R.drawable.ttbfs)

        stctch.setOnClickListener{
            if (model.gameoverover.value!!){finish()}
            if (!model.gameoverover.value!!) {
                model.onCatch()
                scorress = scorress + model.directionX.toInt().absoluteValue
                setTitle("+Scores: $scorress !!!")
                stctch.isVisible = false
            }
            

        }
        imBall.setOnClickListener{
            stctch.isVisible = true
            stctch.setImageResource(R.drawable.ctch)
        }

        val ballXobs = Observer<Float> { x ->

            imBall.x = x.toFloat()
        }
        val ballYobs = Observer<Float> { y ->

            imBall.y = y.toFloat()
        }
        val tMobs = Observer<Int> { i ->

            if (i>=1) setTitle("Score: $scorress  |  Time: $i")
            if (i<1) {
                stctch.isVisible = true
                imBall.isVisible = false
                stctch.setImageResource(R.drawable.goo)
            }
        }
         val timeLoadBanMobs = Observer<Int> { t ->
             if (t>=5) loadBannerOnTime()
        }

        model.ballX.observe(this, ballXobs)
        model.ballY.observe(this, ballYobs)
        model.tm.observe(this, tMobs)
        model.timeToLoadBan.observe(this, timeLoadBanMobs)


    }
    fun loadBannerOnTime(){
        // Create the top banner view object:
        topBanner =  BannerView(this, topAdUnitId,  UnityBannerSize(320, 50));
        // Set the listener for banner lifecycle events:
        topBanner.listener = bannerListener;
        topBannerView = findViewById(R.id.topBanner)
        LoadBannerAd(topBanner, topBannerView)
        //hideTopBannerButton.setEnabled(true)
    }

    fun LoadBannerAd( bannerView: BannerView,  bannerLayout: RelativeLayout) {
        // Request a banner ad:
        bannerView.load();
        // Associate the banner view object with the banner view:
        bannerLayout.addView(bannerView);
    }

    override fun onInitializationComplete() {
    }

    override  fun onInitializationFailed( error:UnityAds.UnityAdsInitializationError ,  message:String) {

    }


}