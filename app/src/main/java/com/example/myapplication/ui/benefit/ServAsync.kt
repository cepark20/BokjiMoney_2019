package com.example.myapplication.ui.benefit

import android.os.AsyncTask
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

class ServAsync : AsyncTask<String, Void, ArrayList<ArrayList<String>>>() {

    lateinit var fileurl:URL
    // param으로 나중에 받아오기

    override fun doInBackground(vararg params: String?): ArrayList<java.util.ArrayList<String>> {
        Log.d("doinback", params[0].toString())
        return listparse(params[0]!!,params[1]!!,params[2]!!)
    }

    fun listparse(ageint: String, jang: String, bo: String): ArrayList<java.util.ArrayList<String>> {
        val parserCreator = XmlPullParserFactory.newInstance()
        val parser = parserCreator.newPullParser()

        Log.d("lists", ageint)
        var servid=false
        var servDgst=false
        var servNm =false

        fileurl = URL("http://52.79.72.52:5000/findbenefit?ageint=${ageint}&jang=${jang}&bo=${bo}")

        Log.d("lists", fileurl.toString())
        //url잘가져옴
        parser.setInput(fileurl.openStream(), null)

        var parserEvent = parser.getEventType()
        var idlist:ArrayList<String> = ArrayList()
        var namelist:ArrayList<String> = ArrayList()
        var detaillist:ArrayList<String> = ArrayList()

        while (parserEvent != XmlPullParser.END_DOCUMENT) {
            when(parserEvent) {
                XmlPullParser.START_TAG -> {//parser가 시작 태그를 만나면 실행
                    Log.d("parser1",parser.name)
                    if (parser.name.equals("benefit")) {
                        servid=true
                    }
                    if (parser.name.equals("brief")) {
                        servDgst=true
                    }
                    if (parser.name.equals("name")) {
                        servNm=true
                    }
                }
                XmlPullParser.TEXT ->{
                    if(servid) {
                        Log.d("parser",parser.text)
                        idlist.add(parser.text)
                        servid=false
                    }
                    if(servDgst) {
                        Log.d("parser",parser.text)
                        detaillist.add(parser.text)
                        servDgst=false
                    }
                    if(servNm) {
                        Log.d("parser",parser.text)
                        namelist.add(parser.text)
                        servNm=false
                    }
                }
            }
            parserEvent = parser.next()
        }
        var arrs = arrayListOf(idlist,namelist,detaillist)

        return arrs
    }
}