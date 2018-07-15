package natto.com.settlelunch

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.util.AndroidRuntimeException
import com.google.zxing.WriterException
import com.google.zxing.BarcodeFormat
import android.graphics.Bitmap
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.google.zxing.integration.android.IntentResult
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.content.SharedPreferences
import android.net.Uri


class MainActivity : AppCompatActivity() {

    var qrV: ImageView? = null
    var scanBtn: Button? = null
    var startBtn:Button?=null
    var ipTextV:TextView?=null

    var isSender=false

    var ipAd=""

    var settleTextV:TextView?=null
    var containerInput:TextView?=null

    var foods:ArrayList<Int>?=null

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
            foods=ArrayList()
            val data = getSharedPreferences("SelectFood", Context.MODE_PRIVATE)
            for(i in 0..2){
                foods?.add(data.getInt(i.toString(), 0))
            }
        val unit=Unit()
        if(foods!![0]==0){
            containerInput?.visibility=View.VISIBLE
        }else {
            containerInput?.visibility=View.GONE
            settleTextV?.text =
                    "${unit.getFood(foods!![0])}," +
                    "${unit.getFood(foods!![1])}," +
                    "${unit.getFood(foods!![2])}"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        qrV = findViewById(R.id.image_qr)
        scanBtn = findViewById(R.id.btn_scan)
        startBtn=findViewById(R.id.btn_start)
        ipTextV=findViewById(R.id.text_ip)
        settleTextV=findViewById(R.id.text_settle)
        containerInput=findViewById(R.id.container_input)

        val ip=getIp()
        ipTextV?.text="IP:$ip"
        val qrBmp=getQRBmp(ip)
        qrBmp?.let {
            qrV?.setImageBitmap(qrBmp)
        }

        scanBtn?.setOnClickListener {
            IntentIntegrator(this).initiateScan()
        }

        startBtn?.setOnClickListener {
            if(!isSender) {

                val intent=Intent(this,SearchActivity::class.java)
                startActivity(intent)
            }else{
                UDP().send(ipAd, 50000, "${foods!![0]},${foods!![1]},${foods!![2]}")
            }
        }
    }

    @SuppressLint("ShowToast", "SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            Toast.makeText(this,result.contents+"に送信可能",Toast.LENGTH_SHORT).show()
            ipAd=result.contents
            startBtn?.text= ipAd + "に送信"
            isSender=true
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun getIp(): String {
        val manager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = manager.connectionInfo
        val ipAdr = info.ipAddress
        val ip = String.format("%02d.%02d.%02d.%02d", ipAdr shr 0 and 0xff, ipAdr shr 8 and 0xff, ipAdr shr 16 and 0xff, ipAdr shr 24 and 0xff)
        return ip
    }

    fun getQRBmp(str: String): Bitmap? {
        //QRコード画像の大きさを指定(pixel)
        val size = 500

        try {
            val barcodeEncoder = BarcodeEncoder()
            //QRコードをBitmapで作成
            return barcodeEncoder.encodeBitmap(str, BarcodeFormat.QR_CODE, size, size)

        } catch (e: WriterException) {
            //throw AndroidRuntimeException("Barcode Error.", e)
            return null
        }
    }

    fun pushInput(v: View){
        val intent=Intent(this,SelectActivity::class.java)
        startActivity(intent)
    }
}
