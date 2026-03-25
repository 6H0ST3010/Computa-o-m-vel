package cm_15318.myapplication

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
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
        println(getString(R.string.activity_oncreate_msg, this@MainActivity.localClassName))

        val button = findViewById<Button>(R.id.button)
        val mainLayout = findViewById<ConstraintLayout>(R.id.main)
        val colors = listOf(
            Color.WHITE, Color.RED, Color.GREEN, Color.YELLOW,
            Color.BLUE, Color.CYAN, Color.DKGRAY, Color.MAGENTA)
        button.setOnClickListener {
            val randomColor = colors.random()
            mainLayout.setBackgroundColor(randomColor)
        }
    }
}
