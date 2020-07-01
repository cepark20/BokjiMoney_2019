package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ServiceDetail extends AppCompatActivity {

    private TextView serv_obj, serv_detail, sv_Name;
    private String category_mid, sharedString;
    private String detail, score="0";
    private String page_url="http://bokjiro.go.kr/nwel/bokjiroMain.do";
    private ArrayList<String> u_servi;
    private RatingBar rating_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        getSupportActionBar().hide();
        String service_Name = getIntent().getStringExtra("service_name");
        ImageView svDetail_menu = findViewById(R.id.svDetail_menu);
        svDetail_menu.setOnClickListener(v -> finish());
        sv_Name= findViewById(R.id.sv_Name);
        serv_obj= findViewById(R.id.serv_object);
        serv_detail= findViewById(R.id.serv_detail);
        Button url_button = findViewById(R.id.url_button);
        Button u_score_btn = findViewById(R.id.u_score_btn);
        rating_score = findViewById(R.id.rating_score);
        rating_score.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> score = String.valueOf((int)rating));

        serv_obj.setText("정보를 불러오는 중입니다,,,");
        serv_detail.setText("정보를 불러오는 중입니다,,,");

        // 서비스 상세 내용 가져오기
        if(service_Name != null && !service_Name.equals("")){
            sv_Name.setText(service_Name);
            getServDetail task = new getServDetail();
            task.execute(service_Name);
        }

        u_score_btn.setOnClickListener(v -> {
            for(int i=0 ; i<u_servi.size(); i++){
                String[] str = u_servi.get(i).split("&&&");
                // str[0] = 서비스명, str[1] = 평점, str[2] = 카테고리
                if(str[0].equals(sv_Name.getText().toString())){ // 저장된 u_servi에 서비스가 있는 경우 중복 검사
                    u_servi.set(i,sv_Name.getText().toString()+"&&&"+score+"&&&"+category_mid);
                    setStringArrayPref(getApplicationContext(),"u_servi",u_servi);
                    Toast.makeText(getApplicationContext(),"등록되었습니다.",Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        });

        // 복지 웹 사이트로 이동 (서버에서 웹 페이지 URL 못 받아오는 경우 메인 페이지로 이동)
        url_button.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(page_url));
            startActivity(intent);
        });

    }

    // 사용자 서비스 조회 기록 저장
    public void saveUserHistory(){
        u_servi = new ArrayList<>();
        u_servi = getStringArrayPref(this,"u_servi");

        if(u_servi.size()==0){ // 사용자의 조회기록이 아예 없는 경우
            u_servi.add(sv_Name.getText().toString()+"&&&1"+"&&&"+category_mid);
            setStringArrayPref(this,"u_servi",u_servi);

        }else{// 사용자의 조회기록이 1개이상인 경우
            for(int i=0 ; i<u_servi.size(); i++){
                String[] str = u_servi.get(i).split("&&&");

                // str[0] = 서비스명, str[1] = 평점, str[2] = 카테고리
                if(str[0].equals(sv_Name.getText().toString())){// 저장된 u_servi에 조회한 서비스가 있는 경우 중복검사
                    float rating = Float.parseFloat(str[1]);
                    rating_score.setRating(rating);
                    break;
                }

                else if(i==u_servi.size()-1){// 저장된 u_servi에 조회한 서비스가 없는 경우
                    u_servi.add(sv_Name.getText().toString()+"&&&"+"1"+"&&&"+category_mid);
                    setStringArrayPref(this,"u_servi",u_servi);//저장된 u_servi에 조회한 서비스 추가
                }
            }
        }

        for(int i=0 ; i< u_servi.size(); i++){
            sharedString = sharedString + u_servi.get(i)+"\n";
        }
        if(u_servi.size() %3 ==0){
            sendServiceRating();
        }
    }

    // 서비스명,평점 가져오기
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
        }else{
            Log.d("getStringArrayPref","null");
        }
        return urls;
    }

    // 서비스명, 평점 수정/저장
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

    // 서버에서 서비스 내용 가져오기
    @SuppressLint("StaticFieldLeak")
    public class getServDetail extends AsyncTask<String,Void,String> {
        String r="";
        OkHttpClient client = new OkHttpClient();
        String[] sv_content, sv_content2;
        int i=0;

        @Override
        protected String doInBackground(String... params) {
            String url = "http://52.79.72.52:5000/findbyname";
            String name = params[0]; //서비스명
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addEncodedQueryParameter("name", name);
            String requestUrl = urlBuilder.build().toString();
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("sendCategory:","fail"+e);
                }
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                    String data = responseBodyCopy.string();

                    JsonParser Parser = new JsonParser();
                    JsonObject jsonObj = (JsonObject) Parser.parse(data);
                    JsonArray svArray = (JsonArray) jsonObj.get("result");

                    // 서버에서 보내주는 서비스 상세 내용 split 해서 지원대상, 상세내용, 페이지 URL 정보 가져오기
                    if(svArray != null && svArray.size() != 0){
                        String test = svArray.get(0).toString();
                        test = test.substring(2, test.length()-2);

                        sv_content2 = new String[2];
                        sv_content = test.split("', '|\\\\, '|', \\\\\"|\\\\\",'|\\\\\", '|\\\\\",|'\n'|', \\\\|',\n'|'\n,'|, \\\\\"");
                        //', \"
                        if(sv_content.length > 5){
                            category_mid = sv_content[1];
                            sv_content2[0] = sv_content[3];
                            sv_content2[1] = sv_content[4];

                            for(i = 5; i < sv_content.length-2; i++){
                                StringBuilder sb = new StringBuilder();
                                r = sb.append(r).append(sv_content[i]).append("\n").toString();
                                r = r + sv_content[i]+"\n";
                            }
                            detail = r;
                        }
                    }else{
                        Log.d("sendCategory:","result = null");
                    }
                    new Handler(getMainLooper()).post(() -> {
                        if(sv_content2 != null && sv_content2.length != 0){
                            if(sv_content2[0] != null && sv_content2[1] != null){
                                serv_obj.setText(sv_content2[0]+"\n"+sv_content2[1]);
                                serv_detail.setText(detail);
                                page_url = sv_content[sv_content.length-2];
                                saveUserHistory();
                            }
                        }
                    });
                }
            });
            return r;
        }
        @Override
        protected void onPostExecute(String result) {
            if(result !=null && !result.equals("")){
                Log.d("SendCategory","Success");
            }else{
                Log.d("SendCategory","Fail");
            }
        }
    }

    // 저장된 평점 데이터 서버로 전송
    public void sendServiceRating(){
        ArrayList<String> study_servi;
        study_servi = getStringArrayPref(getApplicationContext(),"u_servi");
        JSONArray arrlist = new JSONArray();

        for(int i=0 ; i<study_servi.size(); i++){
            String[] str = study_servi.get(i).split("&&&");
            JSONObject arr = makeJsonObject(str);
            arrlist.put(arr);
        }

        String url = "http://52.79.72.52:5000/putjson";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                com.android.volley.Request.Method.POST,
                url,
                arrlist,
                response -> Log.d("협업 필터링 모델 학습 결과:",response.toString()),
                error -> VolleyLog.e("Error: ", error.getMessage()));
        requestQueue.add(arrayRequest);
    }

    private JSONObject makeJsonObject(String[] str){
        JSONObject arr = new JSONObject();
        SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        String userId = pref.getString("uid","");
        String name = str[0];
        String rate = str[1];

        try{
            arr.put("name",userId);
            arr.put("servnm",name);
            arr.put("rate",rate);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return arr;
    }
}
