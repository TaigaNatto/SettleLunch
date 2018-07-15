package natto.com.settlelunch

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class ResultActivity : AppCompatActivity() {

    var settleCategory=""

    var textV:TextView?=null
    var btnMap:Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        settleCategory=intent.getStringExtra("category")

        textV=findViewById(R.id.text_category)
        textV?.text=settleCategory

        btnMap=findViewById(R.id.btn_map)
        btnMap?.text="近くの${settleCategory}を探す"
        btnMap?.setOnClickListener {
            val uri = Uri.parse("geo:0,0?q=$settleCategory")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}
