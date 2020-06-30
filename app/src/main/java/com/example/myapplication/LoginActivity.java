package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends Activity {

    Button btnLogin;
    EditText edtId,edtPw;
    ArrayList<String> list;
    TextView tv_name,tv_join;
    Boolean loginChecked;
    CheckBox cb;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        list = new ArrayList<>();
        btnLogin = findViewById(R.id.btnLogin);
        edtId = findViewById(R.id.loginId);
        edtPw = findViewById(R.id.loginPw);
        tv_name = findViewById(R.id.tv_name);
        tv_join=findViewById(R.id.textViewforJoin);
        cb = findViewById(R.id.checkboxlogin);
        cb.setChecked(false);

        //체크박스 리스너
        cb.setOnCheckedChangeListener((compoundButton, isChecked) -> loginChecked= isChecked);

        SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        if(pref.getBoolean("autoLogin",false)){
            edtId.setText(pref.getString("uid", ""));
            edtPw.setText(pref.getString("pwd", ""));
            cb.setChecked(true);
        }


        //서버에서 회원정보 가져온 후 메인페이지로 이동
        btnLogin.setOnClickListener(view -> {
            id = edtId.getText().toString().trim();
            String pwd = edtPw.getText().toString().trim();
            if(id.equals("") || (pwd.equals(""))){
                Toast.makeText(getApplicationContext(),"아이디 또는 비밀번호가 입력되지 않았습니다..",Toast.LENGTH_SHORT).show();
            }else{
                getUserInfo task=new getUserInfo();
                task.execute(id);
                edtId.setText("");
                edtPw.setText("");
            }
        });

        //회원 가입 페이지로 이동
        tv_join.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), join.class);
            startActivity(intent);
        });
    }

    private void startMain(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    //회원정보 가져오기
    @SuppressLint("StaticFieldLeak")
    public class getUserInfo extends AsyncTask<String,Void,String> {
        OkHttpClient client = new OkHttpClient();

        SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;

        @Override
        protected String doInBackground(String... params) {
            String url = "http://52.79.72.52:5000/signin";
            String userid = params[0];
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addEncodedQueryParameter("uid", userid);
            String requestUrl = urlBuilder.build().toString();
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) { }

                @Override
                public void onResponse(Call call, Response response) {
                    try{
                        ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                        String data = responseBodyCopy.string();
                        if(data.contains("[]")){
                            editor = pref.edit();
                            editor.clear();
                            editor.apply();
                        }else if(data.length()>12){
                            data = data.replaceAll("[\\[\\]]", "");
                            StringTokenizer st = new StringTokenizer(data,",");
                            while(st.hasMoreTokens()){
                                String token=st.nextToken();
                                list.add(token);
                            }
                            String uid = list.get(0);
                            uid = uid.substring(1);
                            uid = uid.substring(0,uid.length()-1);
                            Log.d("uid:",uid);

                            String pwd = list.get(1);
                            pwd = pwd.substring(1);
                            pwd = pwd.substring(0,pwd.length()-1);

                            String name = list.get(2);
                            name = name.substring(1);
                            name = name.substring(0,name.length()-1);

                            int age = Integer.parseInt(list.get(3));
                            int gender = Integer.parseInt(list.get(4));

                            String loc = list.get(5);
                            loc = loc.substring(1);
                            loc = loc.substring(0, loc.length()-1);

                            int freg = Integer.parseInt(list.get(6));
                            int baby = Integer.parseInt(list.get(7));
                            int kid = Integer.parseInt(list.get(8));
                            int chung = Integer.parseInt(list.get(9));
                            int jung = Integer.parseInt(list.get(10));
                            int no = Integer.parseInt(list.get(11));
                            int handi = Integer.parseInt(list.get(12));
                            int hanbumo = Integer.parseInt(list.get(13));
                            int damunhwa = Integer.parseInt(list.get(14));
                            int low = Integer.parseInt(list.get(15));
                            String bohun = list.get(16);

                            editor = pref.edit();
                            editor.putString("uid",uid);editor.putString("pwd",pwd);editor.putString("name",name);editor.putInt("age",age);editor.putInt("gender",gender);editor.putString("loc",loc);
                            editor.putInt("freg",freg);editor.putInt("baby",baby);editor.putInt("kid",kid);editor.putInt("chung",chung);
                            editor.putInt("jung",jung);editor.putInt("no",no);editor.putInt("handi",handi);
                            editor.putInt("hanbumo",hanbumo);editor.putInt("damunhwa",damunhwa);editor.putInt("lowsodek",low);editor.putString("bohun",bohun);
                            editor.apply();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    new Handler(getMainLooper()).post(() -> {
                        if(loginChecked!=null && loginChecked){
                            //체크박스가 선택된 상태이면
                            editor = pref.edit();
                            editor.putBoolean("autoLogin",true);
                            editor.apply();
                        }else{
                            editor = pref.edit();
                            editor.putBoolean("autoLogin",false);
                            editor.apply();
                        }
                        if(pref.getString("uid","").equals("")){
                            Toast.makeText(getApplicationContext(),"아이디 또는 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                        }else if(pref.getString("uid","").equals(id)){
                            Toast.makeText(getApplicationContext(),"환영합니다 "+pref.getString("name","")+"님",Toast.LENGTH_SHORT).show();
                            startMain();
                        }
                    });

                }
            });
            return null;
        }
    }
}