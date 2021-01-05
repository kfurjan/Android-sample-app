package hr.algebra.nasa

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.nasa.framework.applyAnimation
import hr.algebra.nasa.framework.getBooleanPreference
import hr.algebra.nasa.framework.isOnline
import hr.algebra.nasa.framework.startActivity
import kotlinx.android.synthetic.main.activity_splash_screen_launcher.*

private const val DELAY: Long = 3000
const val DATA_IMPORTED: String = "hr.algebra.nasa.data_imported"

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen_launcher)

        startAnimations()
        redirect()
    }

    private fun startAnimations() {
        ivSplash.applyAnimation(R.anim.rotate)
        tvSplash.applyAnimation(R.anim.blink)
    }

    private fun redirect() {
        if (getBooleanPreference(DATA_IMPORTED)) {
            Handler(Looper.getMainLooper()).postDelayed(
                { startActivity<HostActivity>() },
                DELAY
            )
        } else {
            if (isOnline()) {
                Intent(this, NasaService::class.java).apply {
                    NasaService.enqueueWork(this@SplashScreenActivity, this)
                }
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.please_connect_to_the_internet),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}
