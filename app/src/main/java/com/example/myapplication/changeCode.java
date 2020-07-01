package com.example.myapplication;

import android.os.StrictMode;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.net.URL;

public class changeCode {

    private String KEY="WU3mzjeOk9dyTh0TGhzyPJgVTF896mvmogKUt4Pxmnt87Xuixv2VhA3Sfa52ZGNVSh0rhtUjiwLspxg%2Fm4hYvQ%3D%3D";

    //가구유형 trgterIndvdlArray
    private String one="&trgterIndvdlArray=002",multi="&trgterIndvdlArray=003";

    //charTrgterArray
    private String disa="&charTrgter=004",preg="&charTrgterArray=003",nat="&charTrgterArray=005",find="&charTrgterArray=006";
    private String safe="&desireArray=0000000",health="&desireArray=1000000";

    private boolean inJurMnofNm = false;
    private boolean inJurOrgNm = false;
    private boolean inServDgst = false;
    private boolean inServDtlLink = false;
    private boolean inServId = false, inServNm = false, inSvcfrstRegTs = false;
    private boolean in2servId,in2servNm,in2jurMnofNm,in2servDgst,in2tgtrDtlCn,in2slctCritCn,in2alwServCn,in2servSeCode =false;

    private String servDgst, servId, servNm, servNm2, jurMnofNm2, servDgst2, tgtrDtlCn2, slctCritCn2, alwServCn2;
    private String servSeCode2 = "0";

    public String change(String in){
        //생애주기 lifeArray
        String baby = "&lifeArray=001";
        String adult = "&lifeArray=004";
        String child = "&lifeArray=002";
        String stud = "&lifeArray=003";
        String mid = "&lifeArray=005";
        String old = "&lifeArray=006";

        switch (in) {
            case "영유아":
                return baby;
            case "아동":
                return child;
            case "청소년":
                return stud;
            case "청년":
                return adult;
            case "중장년":
                return mid;
            case "노년":
                return old;
            case "한부모":
                return one;
            case "다문화":
                return multi;
            case "장애인":
                return disa;
            case "국가유공자":
                return nat;
            case "임신":
                return preg;
            case "안전":
                return safe;
            case "건강":
                return health;
        }
        return "&searchWrd="+in;
    }

    public String[][] callApi(String str) {

        String[][] outS=new String[50][3];
        StrictMode.enableDefaults();
        String ss=change(str);

        int i=0;
        Log.d("changegegege",ss);
        try {
            URL url = new URL("http://www.bokjiro.go.kr/openapi/rest/gvmtWelSvc?crtiKey=" + KEY +
                    "&callTp=L&pageNum=1&numOfRows=50"+ss); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT) {

                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("jurMnofNm")) { //title 만나면 내용을 받을수 있게 하자
                            inJurMnofNm = true;
                        }
                        if (parser.getName().equals("jurOrgNm")) { //address 만나면 내용을 받을수 있게 하자
                            inJurOrgNm = true;
                        }
                        if (parser.getName().equals("servDgst")) { //mapx 만나면 내용을 받을수 있게 하자
                            inServDgst = true;
                        }
                        if (parser.getName().equals("servDtlLink")) { //mapy 만나면 내용을 받을수 있게 하자
                            inServDtlLink = true;
                        }
                        if (parser.getName().equals("servId")) { //mapy 만나면 내용을 받을수 있게 하자
                            inServId = true;
                        }
                        if (parser.getName().equals("servNm")) { //mapy 만나면 내용을 받을수 있게 하자
                            inServNm = true;
                        }
                        if (parser.getName().equals("svcfrstRegTs")) { //mapy 만나면 내용을 받을수 있게 하자
                            inSvcfrstRegTs = true;
                        }
                        if (parser.getName().equals("message")) { //message 태그를 만나면 에러 출력
                            Log.d("ERROR", "ERRORRRR");
                        }
                        break;


                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (inJurMnofNm) {
                            String jurMnofNm = parser.getText();
                            inJurMnofNm = false;
                        }
                        if (inJurOrgNm) { //isAddress이 true일 때 태그의 내용을 저장.
                            String jurOrgNm = parser.getText();
                            inJurOrgNm = false;
                        }
                        if (inServDgst) { //isMapx이 true일 때 태그의 내용을 저장.
                            servDgst = parser.getText();
                            inServDgst = false;
                        }
                        if (inServDtlLink) { //isMapy이 true일 때 태그의 내용을 저장.
                            String servDtlLink = parser.getText();
                            inServDtlLink = false;
                        }
                        if (inServId) { //isMapy이 true일 때 태그의 내용을 저장.
                            servId = parser.getText();
                            inServId = false;
                        }
                        if (inServNm) { //isMapy이 true일 때 태그의 내용을 저장.
                            servNm = parser.getText();
                            inServNm = false;
                        }
                        if (inSvcfrstRegTs) { //isMapy이 true일 때 태그의 내용을 저장.
                            String svcfrstRegTs = parser.getText();
                            inSvcfrstRegTs = false;
                        }

                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("servList")) {

                            outS[i][0]=servNm;
                            outS[i][1]=servDgst;
                            outS[i][2]=servId;

                            i++;
                        }
                        break;
                }

                parserEvent = parser.next();

            }
        } catch (Exception e) {
            //tv.setText("에러가..났습니다...");
        } return outS;
    }

    ServiceModel callDetailServ(String str) {

        StrictMode.enableDefaults();
        ServiceModel sm;

        try {
            URL url = new URL("http://www.bokjiro.go.kr/openapi/rest/gvmtWelSvc?crtiKey=" + KEY +
                    "&callTp=D&servId="+ str); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {

                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("servNm")) { //title 만나면 내용을 받을수 있게 하자
                            inServNm = true;
                            in2servNm=true;
                        }
                        if (parser.getName().equals("tgtrDtlCn")) { //address 만나면 내용을 받을수 있게 하자
                            boolean inTgtrDtlCn = true;
                            in2tgtrDtlCn = true;
                        }
                        if (parser.getName().equals("servSeDetailNm")) { //mapx 만나면 내용을 받을수 있게 하자
                            boolean inServSeDetailNm = true;
                        }
                        if (parser.getName().equals("servSeDetailLink")) { //mapy 만나면 내용을 받을수 있게 하자
                            boolean inServSeDetailLink = true;
                        }
                        if (parser.getName().equals("servId")) { //mapy 만나면 내용을 받을수 있게 하자
                            in2servId = true;
                        }
                        if (parser.getName().equals("jurMnofNm")) { //mapy 만나면 내용을 받을수 있게 하자
                            in2jurMnofNm = true;
                        }
                        if (parser.getName().equals("servDgst")) { //mapy 만나면 내용을 받을수 있게 하자
                            in2servDgst = true;
                        }
                        if (parser.getName().equals("slctCritCn")) { //mapy 만나면 내용을 받을수 있게 하자
                            in2slctCritCn = true;
                        }
                        if (parser.getName().equals("alwServCn")) { //mapy 만나면 내용을 받을수 있게 하자
                            in2alwServCn = true;
                        }
                        if (parser.getName().equals("servSeCode")) { //mapy 만나면 내용을 받을수 있게 하자
                            in2servSeCode = true;
                        }

                        if (parser.getName().equals("message")) { //message 태그를 만나면 에러 출력
                            Log.d("ERROR", "ERRORRRR");

                        }
                        break;


                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if (in2servNm) {
                            servNm2=parser.getText();
                            if(servNm2==null)
                                servNm2="0";
                            in2servNm = false;
                        }if (in2tgtrDtlCn) {
                        tgtrDtlCn2 =parser.getText();
                        if(tgtrDtlCn2==null)
                            tgtrDtlCn2="0";
                        in2tgtrDtlCn =false;
                    }if (in2servId) { //isMapy이 true일 때 태그의 내용을 저장.
                        String servId2 = parser.getText();
                        if(servId2 ==null) servId2 ="0";
                        in2servId = false;
                    }if (in2jurMnofNm) { //isMapy이 true일 때 태그의 내용을 저장.
                        jurMnofNm2 = parser.getText();
                        if(jurMnofNm2==null) jurMnofNm2="0";
                        in2jurMnofNm = false;
                    }if (in2servDgst) { //isMapy이 true일 때 태그의 내용을 저장.
                        servDgst2 = parser.getText();
                        if(servDgst2==null) servDgst2="0";
                        in2servDgst = false;
                    }if (in2slctCritCn) { //isMapy이 true일 때 태그의 내용을 저장.
                        slctCritCn2 = parser.getText();
                        if(slctCritCn2==null) slctCritCn2="0";
                        in2slctCritCn = false;
                    }if (in2alwServCn) { //isMapy이 true일 때 태그의 내용을 저장.
                        alwServCn2 = parser.getText();
                        if(alwServCn2==null) alwServCn2="0";
                        in2alwServCn = false;
                    }if (in2servSeCode) { //isMapy이 true일 때 태그의 내용을 저장.
                        servSeCode2 = parser.getText();
                        if(servSeCode2==null) servSeCode2="0";
                        in2servSeCode = false;
                    }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("servList")) {
                        }
                        break;
                }
                parserEvent = parser.next();

            }
        } catch (Exception e) {
            //tv.setText("에러가..났습니다...");
        }
        sm = new ServiceModel(1,servNm2, jurMnofNm2, servDgst2, tgtrDtlCn2, slctCritCn2, alwServCn2, servSeCode2);
        return sm;
    }

}