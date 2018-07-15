package natto.com.settlelunch

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import kotlinx.android.synthetic.main.activity_select.*
import android.widget.Toast
import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE


class SelectActivity : AppCompatActivity() {

    var selectGrid: GridView?=null
    var selectAdapter:SelectAdapter?=null
    var okBtn: Button?=null

    var selectedItems:ArrayList<Int>?=null

    var cntSelect=0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        selectGrid=findViewById(R.id.grid)
        selectAdapter= SelectAdapter(this)
        okBtn=findViewById(R.id.btn_ok)

        selectedItems= ArrayList()

        selectGrid?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val item=selectAdapter?.getItem(position)
            if(item?.isChecked!!){
                item.isChecked=false
                cntSelect--
                selectedItems?.remove(position)
            }else{
                if(cntSelect<3) {
                    item.isChecked = true
                    cntSelect++
                    selectedItems?.add(position)
                }
            }
            okBtn?.text="$cntSelect/3\nすすむ"
            selectAdapter?.setItem(item,position)
            selectAdapter?.notifyDataSetChanged()
        }

        val unit=Unit()
        for(food in unit.getAllFoods()){
            val item=SelectModel(food)
            selectAdapter?.add(item)
        }
        selectGrid?.adapter=selectAdapter

        okBtn?.setOnClickListener {
            saveSelectItems(selectedItems!!)
            finish()
        }
    }

    fun saveSelectItems(items:ArrayList<Int>){
        val data = getSharedPreferences("SelectFood", Context.MODE_PRIVATE)
        val editor = data.edit()
        for(i in 0 until items.size) {
            editor.putInt(i.toString(), items[i])
        }
        editor.apply()
    }
}
