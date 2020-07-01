package com.example.myapplication.ui.benefit

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import kotlinx.android.synthetic.main.fragment_benefit.*


class BenefitFragment : Fragment() {

    lateinit var name :String
    var ageint = 0
    var jang = 0
    var bo = 0
    var items = arrayListOf<ServiceModel>()
    var demotest = false
    lateinit var adap: BenefitAdapter
    var servList= ArrayList<ServiceModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var pref = context!!.getSharedPreferences("pref", Context.MODE_PRIVATE)
        name = pref.getString("name","")!!
        var baby = pref.getInt("baby",0)
        var kid = pref.getInt("kid",0)
        var chung = pref.getInt("chung",0)
        var jung = pref.getInt("jung",0)
        var no = pref.getInt("no",0)
        var freg = pref.getInt("freg",0)

        if (freg == 1 || chung == 1) {
            ageint = 4 //청년
        } else if (baby == 1) {
            ageint = 1 //영유아
        } else if (kid == 1) {
            ageint = 2 //아동
        } else if (jung == 1) {
            ageint = 5 //중장년
        } else if (no == 1) {
            ageint = 6 //노년
        }
        jang = pref.getInt("handi", 0) //장애여부
        //보훈여부
        var bostr=pref.getString("bohun", "0")
        bo = bostr!!.subSequence(0,1).toString().toInt()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_benefit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("catemid", ageint.toString())
        view.apply {
            tv_show.setText(name+"님이 받을 혜택은 다음과 같습니다.")
            adap = BenefitAdapter(activity as MainActivity, items)
            list1.adapter = adap
            var layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            list1.layoutManager = layoutManager

            bt_show.setOnClickListener {

                var lists = ServAsync().execute(ageint.toString(), jang.toString(), bo.toString()).get()//목록에서 id값 추출

                var idlist = lists.get(0)
                var namelist = lists.get(1)
                var detaillist = lists.get(2)
                var countbenefit = 0

                for(i in 1..idlist.size){
                    Log.d("idlist", idlist.get(i-1))
                    if(idlist.get(i-1).length > 7){
                        servList.add(ServiceModel(0,namelist.get(i-1),"",detaillist.get(i-1),"","","",""))
                        countbenefit += 0
                    }
                    else{
                        servList.add(ServiceModel(idlist.get(i-1).toInt(),namelist.get(i-1),"",detaillist.get(i-1),"","","",""))
                        countbenefit += idlist.get(i - 1).toInt()
                    }
                }
                items=servList
                animateTextView(0,countbenefit,tv_money)
                adap.replaceAll(getNewData())

            }
        }
    }

    fun getServDetail(srvid:String) : ServiceModel{
        var apiapi = changeCode()
        var detailserv = apiapi.callDetailServ(srvid)
        return detailserv
    }

    fun animateTextView(initialValue: Int, finalValue: Int, textview: TextView) {

        val valueAnimator = ValueAnimator.ofInt(initialValue, finalValue)
        valueAnimator.duration = 1500

        valueAnimator.addUpdateListener { valueAnimator ->
            textview.text = valueAnimator.animatedValue.toString()
        }
        valueAnimator.start()

    }

    fun getNewData(): ArrayList<ServiceModel> {
        if (demotest){//걍데모값 넣을때
            return makedemo()
        }
        else if (items.size != 0) {
            return items
        }
        else
            return (arrayListOf<ServiceModel>())

    }

    fun makedemo(): ArrayList<ServiceModel> {
        var arr= arrayListOf<ServiceModel>()
        for(i in 1..10) {
            arr.add(
                    ServiceModel(1, "1", "1", "1", "1", "1", "1", "1")
            )
        }
        return arr
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                BenefitFragment()
    }
}
