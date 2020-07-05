package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ServiceList extends AppCompatActivity {

    private ListView listview;
    private ArrayList<String> noti_list, date_list;
    private serviceAdapter serviAdapter;
    private String token, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);
        getSupportActionBar().hide();

        ImageView svLinst_menu = findViewById(R.id.svLinst_menu);
        svLinst_menu.setOnClickListener(v -> finish());

        listview = findViewById(R.id.more_List);
        serviAdapter = new serviceAdapter();

        SharedPreferences pref = getApplication().getSharedPreferences("pref", MODE_PRIVATE);
        uid = pref.getString("uid","");
        token = pref.getString("token","");

        noti_list=getStringArrayPref(this,"notiList");
        date_list = new ArrayList<>();

        SharedPreferences sp=getApplication().getSharedPreferences("Skey",MODE_PRIVATE);
        String key=sp.getString("text","");

        add_ListItem(key);
        listview.setAdapter(serviAdapter);
    }

    //리스트 더하기
    public void add_ListItem(String key){

        ArrayList<String> servlist;
        servlist = getStringArrayPref(getApplication(), "u_servi");
        if(key.equals("content")){ // 내용 기반 추천 서비스 가져오기
            if(servlist.size() != 0){
                String[] str = servlist.get(servlist.size()-1).split("&&&");
                String[][] s=new String[1][1];

                if(str[2]!=null&& !str[2].equals("")){
                    getContentBasedServices task = new getContentBasedServices();
                    task.execute(str[2]);
                    task.onPostExecute(s);
                }
            }
            else{
                Log.d("serviList","null");
                SharedPreferences sp=getApplication().getSharedPreferences("Skey", MODE_PRIVATE);
                String category = sp.getString("first_skey","");
                getContentBasedServices task= new getContentBasedServices();
                task.execute(category);
            }
        }else if(key.equals("collaborative_filtering")){ // 협업 필터링 기반 추천 서비스 가져오기
            getReclist task = new getReclist();
            task.execute(uid);
        } else{ // 복지 API  호출
            changeCode apiapi=new changeCode();
            String[][] str=apiapi.callApi(key);

            for (String[] strings : str) {
                if (strings != null && strings[0] != null) {
                    serviAdapter.addItem(new MoreItemList(strings[0], strings[1], strings[2], true));
                }
            }
        }
    }

    // FCM 전송을 위해 서버로 토큰, 서비스명 전달
    public class sendNoti extends AsyncTask<String,Void,String>{
        String url = "http://52.79.72.52:5000/androidNoti";
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String token = params[0];
            String service = params[1];

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id",token)
                    .addFormDataPart("service",service)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("서버 통신", "오류:");
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    Log.d("서버 통신", "서버에서 응답한 Body:"+body);
                }
            });
            return null;
        }
    }

    // 내용 기반 필터링의 경우
    @SuppressLint("StaticFieldLeak")
    public class getContentBasedServices extends AsyncTask<String,Void,String[][]> {
        OkHttpClient client = new OkHttpClient();
        String[][] three_svlist;

        @Override
        protected void onPostExecute(String[][] s) {
            if(s!=null&&s.length!=0){
                Log.d("내용 기반 서비스","Success");
                listview.setAdapter(serviAdapter);
            }else{
                Log.d("내용 기반 서비스","Null");
            }
        }

        @Override
        protected String[][] doInBackground(String... params) {
            String url = "http://52.79.72.52:5000/findinq";
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addEncodedQueryParameter("catemid", params[0]);
            String requestUrl = urlBuilder.build().toString();
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("Category_Data","urldatafail");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                    String data = responseBodyCopy.string();
                    data = data.replace("\"(","\"{");
                    data = data.replace(")\"","}\"");

                    JsonParser Parser = new JsonParser();
                    JsonObject jsonObj = (JsonObject) Parser.parse(data);
                    JsonArray svArray = (JsonArray) jsonObj.get("result");

                    for(int i=0;i<svArray.size();i++){
                        String str = svArray.get(i).toString();
                        String[] sv_content = str.split(", '");
                        String result1 = sv_content[3].substring(0, sv_content[3].length()-1);
                        String result2 = sv_content[4].substring(0, sv_content[4].length()-1);
                        serviAdapter.addItem(new MoreItemList(result1,result2,"content",true));

                    }
                    new Handler(getMainLooper()).post(() -> listview.setAdapter(serviAdapter));
                }
            });
            return three_svlist;
        }
    }

    //SharedPreferences에 JSON으로 저장하기
    private void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    //SharedPreferences에 있는 JSON값을 읽어오기
    private ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    //협업필터링 추천 서비스들 리턴
    @SuppressLint("StaticFieldLeak")
    public class getReclist extends AsyncTask<String,Void,String>{
        OkHttpClient client = new OkHttpClient();
        String[][] three_svlist;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = "http://52.79.72.52:5000/getrecommend";
            String userid = params[0];
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addEncodedQueryParameter("uid", userid);
            String requestUrl = urlBuilder.build().toString();
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                    String data = responseBodyCopy.string();
                    Log.d("협업 결과",data);
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = (JsonObject) jsonParser.parse(data);
                    jsonObject.get("result").toString();
                    String result = jsonObject.get("result").toString();
                    if(!result.equals("") && !result.equals("[]")){
                        result = result.substring(2,result.length()-2);
                        String[] result_list = result.split("\",\"");
                        if(result_list.length > 0){

                            three_svlist = new String[result_list.length][2];
                            for(int i=0;i<result_list.length;i++){
                                three_svlist[i][0]=result_list[i];
                                three_svlist[i][1]="------------------------>> 상세정보 확인 해 보기";

                                serviAdapter.addItem(new MoreItemList(three_svlist[i][0],three_svlist[i][1],"content",true));
                            }
                        }
                        new Handler(getMainLooper()).post(() -> listview.setAdapter(serviAdapter));
                    }else {
                        Log.d("협업 결과","result가 비어있음");
                    }
                }
            });
            return null;
        }
    }

    //리스트 뷰 어댑터 클래스
    class serviceAdapter extends BaseAdapter {
        ArrayList<MoreItemList> items = new ArrayList<>();
        //List<Pair<String, String>> noti_pair = new ArrayList<>();

        @RequiresApi(api = Build.VERSION_CODES.O)
        public String getCurrentDate(){ // 등록날짜 가져오기
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return formatter.format(currentDate);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void addItem(MoreItemList item){
            items.add(item);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public View getView(int i, View v, ViewGroup viewGroup) {
            MoreItemView view = new MoreItemView(getApplicationContext());
            MoreItemList item = items.get(i);
            final String serviceTitle = item.getMore_title();
            final ImageView notiIcon = view.more_noti;

            view.setMore_title(item.getMore_title());
            view.setMore_content(item.getMore_content());
            view.setService_Id(item.getMore_id());

            if(!item.isNoti()){ //알림설정 없을때
                view.setMore_noti(false);
            }else{//알림설정 있을때
                view.setMore_noti(true);
            }

            // 알림 아이콘 클릭 시
            view.more_noti.setOnClickListener(v1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ServiceList.this);
            builder.setTitle("알림 등록");
            builder.setMessage("알림 등록이 가능합니다! 하시겠습니까? ");
            builder.setNegativeButton("아니오",
                    (dialog, which) -> { });
            builder.setPositiveButton("예",
                    (dialog, which) -> {
                        if(noti_list.size()==0){
                            sendNoti task = new sendNoti();
                            task.execute(token,serviceTitle);

                            // 현재 날짜 가져오기
                            String regDay = getCurrentDate();
                            //noti_pair.add(new Pair<>(serviceTitle, regDay));

                            Toast.makeText(getApplicationContext(),"알림이 등록되었습니다.",Toast.LENGTH_SHORT).show();
                            noti_list.add(serviceTitle);
                            date_list.add(regDay);
                            setStringArrayPref(getApplicationContext(),"notiList",noti_list);
                            setStringArrayPref(getApplicationContext(),"dateList", date_list);
                            notiIcon.setImageResource(R.drawable.icon_noti3);
                        }else{
                            for(int j = 0; j <noti_list.size(); j++){
                                if(noti_list.get(j).equals(serviceTitle)){// 이미 알람에 등록되어있는 경우
                                    Toast.makeText(getApplicationContext(),"이미 등록된 알람입니다.",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                else if(j == noti_list.size()-1){// 알람에 등록된 서비스가 아닌 경우
                                    sendNoti task = new sendNoti();
                                    task.execute(token,serviceTitle);
                                    Toast.makeText(getApplicationContext(),"알림이 등록되었습니다.",Toast.LENGTH_SHORT).show();
                                    noti_list.add(serviceTitle);
                                    String regDay = getCurrentDate();
                                    date_list.add(regDay);
                                    setStringArrayPref(getApplicationContext(),"notiList",noti_list);
                                    setStringArrayPref(getApplicationContext(),"dateList", date_list);
                                    notiIcon.setImageResource(R.drawable.icon_noti3);
                                }
                            }
                        }
                    });
            builder.show();
        });
            return view;
        }
    }
}
