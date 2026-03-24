package cm_15318.systeminfo

import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val d_infotext = findViewById<EditText>(R.id.d_infotext)
        val info = buildString {
            append("Manufacture: ${Build.MANUFACTURER}\n")
            append("Model: ${Build.MODEL}\n")
            append("Brand: ${Build.BRAND}\n")
            append("Type: ${Build.TYPE}\n")
            append("User: ${Build.USER}\n")
            append("Base: ${Build.VERSION.BASE_OS}\n")
            append("Incremental: ${Build.MODEL}\n")
            append("SDK: ${Build.VERSION.SDK_INT}\n")
            append("Version Code: ${Build.VERSION.CODENAME}\n")
            append("Display: ${Build.DISPLAY}\n")
        }
        d_infotext.setText(info)
    }
}