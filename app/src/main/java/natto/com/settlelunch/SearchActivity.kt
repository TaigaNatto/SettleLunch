package natto.com.settlelunch

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView



class SearchActivity : Activity(),Runnable,UDPserver {

    private var udp: UDP?=null
    private var mHandler: MyHandler? = null

    var listV:ListView?=null
    var adapter:ArrayAdapter<String>?=null

    var foodList:ArrayList<Int>?=null
    var hostList:ArrayList<String>?=null

    override fun recv(host: String?, port: Int, data: String?) {
        val text= "$host,$data"
        val msg = Message.obtain()
        msg.obj = text
        mHandler?.sendMessage(msg)

        Thread(this).start()
    }

    override fun run() {
        udp=UDP(this)
        udp?.boot(50000)
    }

//    override fun recv(host: String, port: Int, data: String) {
//		val text=host+","+port+","+data
//		val msg = Message.obtain()
//		msg.obj = text
//		mHandler?.sendMessage(msg)
//
//        Thread(this).start()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        listV=findViewById(R.id.list_search)
        adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1)
        listV?.adapter=adapter

        mHandler=MyHandler()

        foodList= ArrayList()
        hostList= ArrayList()

        udp=UDP(this)
        Thread(this).start()
    }

    fun startSettle(v: View){
        //leaderの妥協も反映
        val data = getSharedPreferences("SelectFood", Context.MODE_PRIVATE)
        for(i in 0..2){
            foodList?.add(data.getInt(i.toString(), -1))
        }

        val intent=Intent(this,ResultActivity::class.java)
        intent.putExtra("category",getSettleCAtegory(foodList!!))
        startActivity(intent)
    }

    fun checkHosts(host: String?,hosts:ArrayList<String>):Boolean{
        for(h in hosts){
            if(h.equals(host)) return false
        }
        return true
    }

    fun getSettleCAtegory(foods:ArrayList<Int>):String{
        var settleCategory=""
        var settleCnt=0
        val unit=Unit()
        for(food in foods){
            val category=unit.getCategory(food)
            var cnt=0
            for(cFood in foods){
                val cCategory=unit.getCategory(cFood)
                if(category.equals(cCategory)){
                    cnt++
                }
            }
            if(settleCnt<cnt){
                settleCategory=category
                settleCnt=cnt
            }
        }
        return settleCategory
    }

    internal inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            val text = msg.obj.toString()
            val objs=text.split(",")
            val host=objs[0]
            if(checkHosts(host,hostList!!)) {
                for (i in 1..3) {
                    foodList?.add(objs[i].toInt())
                }
                hostList?.add(host)
                adapter?.add("${host}さんから受信しました")
                adapter?.notifyDataSetChanged()
            }
        }
    }
}
