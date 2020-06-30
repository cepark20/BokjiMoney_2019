package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.android.GsonFactory;
import ai.api.android.AIDataService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean isPageopen = false;
    LinearLayout slide_page;
    TextView tv_name;
    CheckBox cb_1,cb_2,cb_3,cb_4,cb_n;
    RadioGroup radioGroup,radioGroup2,radioGroup3;
    RadioButton rad_fe,rad_ma,rad_pat_o,rad_pat_x,rad_dis_o,rad_dis_x;
    ImageView menu_img,bar_logout,bar_jujac;
    SharedPreferences pref,sp;
    SharedPreferences.Editor editor,editor2;
    String name,loc,state,town,loc2,bohun,uid,ages,city;
    BottomNavigationView navView;
    Button update;
    Spinner spinner1,spinner2,spinner3;
    ArrayAdapter arrayAdapter,arrayAdapter2,arrayAdapter3,arrayAdapter5;
    EditText edtage;
    int age, freg, baby, kid, chung, jung, no,low,gender, bohun2, handi,hanbumo,damunhwa;
    ArrayList<String> u_servi, arrayList2, arrGyung;
    JSONArray arrlist;

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //액션 버튼 클릭 시
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        Intent intent = new Intent(this, inform.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref=getSharedPreferences("pref", Context.MODE_PRIVATE);
        initialize();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        getSupportActionBar().hide();
        setSpinner();
        setUser();
        u_servi = new ArrayList<>();
        u_servi = getStringArrayPref(this,"u_servi");// 저장된 서비스+평점 리스트 가져오기


        //정보 수정 버튼 클릭 시
        update.setOnClickListener(view -> {
            //사이드바 - 가구상황 설정
            if(cb_1.isChecked()){
                handi = 1;
            }else{
                handi = 0;
            }
            if(cb_2.isChecked()){
                hanbumo = 1;
            }else{
                hanbumo = 0;
            }
            if(cb_3.isChecked()){
                damunhwa = 1;
            }else{
                damunhwa = 0;
            }
            if(cb_4.isChecked()){
                low = 1;
            }else{
                low = 0;
            }
            if(cb_n.isChecked()){
                handi = 0;
                hanbumo = 0;
                damunhwa = 0;
                low = 0;
            }

            uid = pref.getString("uid",uid);
            ages = edtage.getText().toString();

            updateUser task=new updateUser();
            task.execute(uid,name,state,town,ages);

        });

        menu_img.setOnClickListener(v -> {
            if(!isPageopen){ // 사이드 페이지가 닫혀있는 경우
                menu_img.setImageResource(R.drawable.arrow3);
                slide_page.setVisibility(View.VISIBLE);
                isPageopen=true;
            }
            else { // 사이드 페이지가 열려있는 경우
                menu_img.setImageResource(R.drawable.menu3);
                slide_page.setVisibility(View.GONE);
                isPageopen=false;
            }
        });

        //로그아웃버튼 클릭 시
        bar_logout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("로그아웃");
            builder.setMessage("로그아웃 하시겠습니까?");
            builder.setNegativeButton("아니오",
                    (dialog, which) -> {
                    });
            builder.setPositiveButton("예",
                    (dialog, which) -> {
                        // 평점 정보 서버로 전송
                        u_servi = new ArrayList<>();
                        u_servi = getStringArrayPref(getApplicationContext(),"u_servi");
                        arrlist = new JSONArray();

                        for(int i=0 ; i<u_servi.size(); i++){
                            String[] str = u_servi.get(i).split("&&&");
                            JSONObject arr = makeJsonObject(str);
                            arrlist.put(arr);
                        }

                        String url = "http://52.79.72.52:5000/putjson";
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                                com.android.volley.Request.Method.POST,
                                url,
                                arrlist,
                                response -> {
                                    // 협업 필터링 모델 학습 결과 리턴
                                    setStringArrayPref(getApplicationContext(),"u_servi",u_servi);
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences sp=getApplicationContext().getSharedPreferences("Skey", Context.MODE_PRIVATE);
                                    editor = pref.edit();
                                    editor.clear();
                                    editor.apply();
                                    editor = prefs.edit();
                                    editor.clear();
                                    editor.apply();
                                    editor = sp.edit();
                                    editor.clear();
                                    editor.apply();
                                },
                                error -> VolleyLog.e("Error: ", error.getMessage())
                        );
                        requestQueue.add(arrayRequest);

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });
            builder.show();
        });

        // 아이콘 저작권 페이지
        bar_jujac.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), jujac.class);
            startActivity(intent);
        });

        //거주지 설정 - 시(state)
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                state = arrayAdapter.getItem(position).toString();
                if(state.equals("서울특별시")){
                    spinner2.setAdapter(arrayAdapter2);
                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            town = arrayAdapter2.getItem(position).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) { }
                    });
                }else if(state.equals("경기도")){
                    spinner2.setAdapter(arrayAdapter5);
                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            town = arrayAdapter5.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) { }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        // 거주지 설정 - 구(city)
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                city = arrayAdapter2.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });


        //사이드바 - 생애주기 설정
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                    case 0:
                        freg=1;baby=0;kid=0;chung=0;jung=0;no=0; break;
                    case 1:
                        freg=0;baby=1;kid=0;chung=0;jung=0;no=0; break;
                    case 2:
                        freg=0;baby=0;kid=1;chung=0;jung=0;no=0; break;
                    case 3:
                        freg=0;baby=0;kid=0;chung=1;jung=0;no=0; break;
                    case 4:
                        freg=0;baby=0;kid=0;chung=0;jung=1;no=0; break;
                    case 5:
                        freg=0;baby=0;kid=0;chung=0;jung=0;no=1; break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        //사이드바 - 성별
        radioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId){
                case R.id.rad_fe: //여자
                    gender = 0; break;
                case R.id.rad_ma:
                    gender = 1; break; //남자
            }
        });

        //사이드바 - 보훈대상
        radioGroup3.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId){
                case R.id.rad_pat_o: //대상
                    bohun2 = 1; break;
                case R.id.rad_pat_x:
                    bohun2 = 0; break; //비대상
            }
        });
    }

    public void initialize(){
        update = findViewById(R.id.btnUpdate);
        spinner1 = findViewById(R.id.spinner_inform1);spinner2 = findViewById(R.id.spinner_inform2);spinner3 = findViewById(R.id.spinner_inform3);
        slide_page = findViewById(R.id.slide_page);
        edtage = findViewById(R.id.edtage);
        tv_name = findViewById(R.id.tv_name);
        cb_1=findViewById(R.id.cb_handi);cb_2=findViewById(R.id.cb_hanbumo);
        cb_3 = findViewById(R.id.cb_damunhwa);cb_4=findViewById(R.id.cb_lowsodek);cb_n=findViewById(R.id.cb_none);
        radioGroup = findViewById(R.id.radiogroup);
        radioGroup2 = findViewById(R.id.radiogroup2);
        radioGroup3 = findViewById(R.id.radiogroup3);
        rad_fe = findViewById(R.id.rad_fe);rad_ma = findViewById(R.id.rad_ma);
        rad_dis_o = findViewById(R.id.rad_dis_o);rad_dis_x = findViewById(R.id.rad_dis_x);
        rad_pat_o = findViewById(R.id.rad_pat_o);rad_pat_x=findViewById(R.id.rad_pat_x);
        navView = findViewById(R.id.nav_view);
        menu_img =findViewById(R.id.bar_menu);
        bar_logout = findViewById(R.id.bar_logout);
        bar_jujac = findViewById(R.id.bar_jujac);
    }

    public void setSpinner(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("서울특별시");arrayList.add("경기도");
        //arrayList.add("부산광역시");arrayList.add("대구광역시");
        //arrayList.add("인천광역시");arrayList.add("광주광역시");arrayList.add("대전광역시");
        //arrayList.add("울산광역시");arrayList.add("세종특별자치시");arrayList.add("경기도");

        arrayList2 = new ArrayList<>();
        arrayList2.add("강남구");arrayList2.add("강동구");arrayList2.add("강북구");
        arrayList2.add("강서구");arrayList2.add("관악구");arrayList2.add("광진구");
        arrayList2.add("구로구");arrayList2.add("금천구");arrayList2.add("노원구");
        arrayList2.add("도봉구");arrayList2.add("동대문구");arrayList2.add("동작구");
        arrayList2.add("마포구");arrayList2.add("서대문구");arrayList2.add("서초구");
        arrayList2.add("성동구");arrayList2.add("성북구");arrayList2.add("송파구");
        arrayList2.add("양천구");arrayList2.add("영등포구");arrayList2.add("용산구");
        arrayList2.add("은평구");arrayList2.add("종로구");arrayList2.add("중구");arrayList2.add("중랑구");

        arrGyung = new ArrayList<>();
        arrGyung.add("고양시");
        arrGyung.add("구리시");
        arrGyung.add("광명시");
        arrGyung.add("김포시");
        arrGyung.add("동두천시");
        arrGyung.add("남양주시");
        arrGyung.add("수원시");
        arrGyung.add("부천시");
        arrGyung.add("의정부시");
        arrGyung.add("오산시");
        arrGyung.add("안산시");
        arrGyung.add("용인시");
        arrGyung.add("평택시");

        ArrayList<String> arrayList3 = new ArrayList<>();
        arrayList3.add("임신‧출산");arrayList3.add("영유아");
        arrayList3.add("아동·청소년");arrayList3.add("청년");
        arrayList3.add("중장년");arrayList3.add("노년");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinner1.setAdapter(arrayAdapter);
        arrayAdapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList2);
        arrayAdapter5 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrGyung);

        arrayAdapter3 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList3);
        spinner3.setAdapter(arrayAdapter3);
    }

    //사이드바에 회원정보 설정하기
    public void setUser(){
        name = pref.getString("name","데이터가 없어요");
        String str_name = name+"님 맞춤정보";
        tv_name.setText(str_name);
        //나이
        age = pref.getInt("age",0);
        String str = Integer.toString(age);
        edtage.setText(str);
        //성별
        gender = pref.getInt("gender",2);
        if(gender == 0){
            rad_fe.setChecked(true);
        }else if(gender==1){
            rad_ma.setChecked(true);
        }

        //거주지
        loc = pref.getString("loc","");
        int index = loc.indexOf(" ");
        state = loc.substring(0,index);
        town = loc.substring(index+1);
        if(state.equals("서울특별시")){
            spinner1.setSelection(0);
            arrayAdapter.notifyDataSetChanged();
            spinner2.setAdapter(arrayAdapter2);
            if(town.contains("강남")){
                spinner2.setSelection(0);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("강동")){
                spinner2.setSelection(1);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("강북")){
                spinner2.setSelection(2);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("강서")){
                spinner2.setSelection(3);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("관악")){
                spinner2.setSelection(4);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("광진")){
                spinner2.setSelection(5);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("구로")){
                spinner2.setSelection(6);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("금천")){
                spinner2.setSelection(7);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("노원")){
                spinner2.setSelection(8);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("도봉")){
                spinner2.setSelection(9);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("동대문")){
                spinner2.setSelection(10);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("동작")){
                spinner2.setSelection(11);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("마포")){
                spinner2.setSelection(12);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("서대문")){
                spinner2.setSelection(13);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("서초")){
                spinner2.setSelection(14);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("성동")){
                spinner2.setSelection(15);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("성북")){
                spinner2.setSelection(16);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("송파")){
                spinner2.setSelection(17);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("양천")){
                spinner2.setSelection(18);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("영등포")){
                spinner2.setSelection(19);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("용산")){
                spinner2.setSelection(20);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("은평")){
                spinner2.setSelection(21);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("종로")){
                spinner2.setSelection(22);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("중구")){
                spinner2.setSelection(23);
                arrayAdapter2.notifyDataSetChanged();
            }else if(town.contains("중랑")){
                spinner2.setSelection(24);
                arrayAdapter2.notifyDataSetChanged();
            }
        }else if(state.equals("경기도")){
            spinner1.setSelection(1);
            arrayAdapter.notifyDataSetChanged();
            spinner2.setAdapter(arrayAdapter5);
            if(town.contains("고양")){
                spinner2.setSelection(0);
                arrayAdapter5.notifyDataSetChanged();
            }else if(town.contains("구리")){
                spinner2.setSelection(1);
                arrayAdapter5.notifyDataSetChanged();
            }else if(town.contains("광명")){
                spinner2.setSelection(2);
                arrayAdapter5.notifyDataSetChanged();
            }else if(town.contains("김포")){
                spinner2.setSelection(3);
                arrayAdapter5.notifyDataSetChanged();
            }else if(town.contains("동두천")){
                spinner2.setSelection(4);
                arrayAdapter5.notifyDataSetChanged();
            }else if(town.contains("남양주")){
                spinner2.setSelection(5);
                arrayAdapter5.notifyDataSetChanged();
            }else if(town.contains("수원")){
                spinner2.setSelection(6);
                arrayAdapter5.notifyDataSetChanged();
            }else if(town.contains("부천")){
                spinner2.setSelection(7);
                arrayAdapter5.notifyDataSetChanged();
            }else if(town.contains("의정부")){
                spinner2.setSelection(8);
                arrayAdapter5.notifyDataSetChanged();
            }else if(town.contains("오산")){
                spinner2.setSelection(9);
                arrayAdapter5.notifyDataSetChanged();
            }else if(town.contains("안산")){
                spinner2.setSelection(10);
                arrayAdapter5.notifyDataSetChanged();
            }else if(town.contains("용인")){
                spinner2.setSelection(11);
                arrayAdapter5.notifyDataSetChanged();
            }else if(town.contains("평택")){
                spinner2.setSelection(12);
                arrayAdapter5.notifyDataSetChanged();
            }
        }

        //생애주기
        freg = pref.getInt("freg",2);
        baby = pref.getInt("baby",2);
        kid = pref.getInt("kid",2);
        chung = pref.getInt("chung",2);
        jung = pref.getInt("jung",2);
        no = pref.getInt("no",2);
        if(freg==1){
            spinner3.setSelection(0);
            arrayAdapter3.notifyDataSetChanged();
            setFirstSkey("임신·출산");
        }else if(baby==1){
            spinner3.setSelection(1);
            arrayAdapter3.notifyDataSetChanged();
            setFirstSkey("영유아");
        }else if(kid==1){
            spinner3.setSelection(2);
            arrayAdapter3.notifyDataSetChanged();
            setFirstSkey("아동·청소년");
        }else if(chung==1){
            spinner3.setSelection(3);
            arrayAdapter3.notifyDataSetChanged();
            setFirstSkey("청년");
        }else if(jung==1){
            spinner3.setSelection(4);
            arrayAdapter3.notifyDataSetChanged();
            setFirstSkey("중장년");
        }else if(no==1){
            spinner3.setSelection(5);
            arrayAdapter3.notifyDataSetChanged();
            setFirstSkey("노년");
        }

        //가구상황
        //장애인
        handi = pref.getInt("handi",2);
        if(handi ==1) {
            cb_1.setChecked(true);
            rad_pat_o.setChecked(true);
        }else if(handi==0){
            cb_1.setChecked(false);
            rad_pat_x.setChecked(true);
        }

        //한부모
        hanbumo = pref.getInt("hanbumo",2);
        if(hanbumo == 1){
            cb_2.setChecked(true);
        }else if(hanbumo ==0){
            cb_2.setChecked(false);
        }

        //다문화
        damunhwa = pref.getInt("damunhwa",2);
        if(damunhwa == 1){
            cb_3.setChecked(true);
        }else if(damunhwa==0){
            cb_3.setChecked(false);
        }

        //저소득층
        int lowso = pref.getInt("lowsodek",2);
        Log.d("lowwww",lowso+"");
        if(lowso ==1){
            cb_4.setChecked(true);
        }else if(lowso==0){
            cb_4.setChecked(false);
        }

        //해당없음
        if((handi==0)&&(hanbumo==0)&&(damunhwa==0)&&(lowso==0)){
            cb_n.setChecked(true);
        }

        //장애 여부
        if(handi == 1){
            rad_dis_o.setChecked(true);
        }else if(handi ==0){
            rad_dis_x.setChecked(true);
        }

        //보훈대상 여부
        String bohunn = pref.getString("bohun","");
        if(bohunn.contains("1")){
            rad_pat_o.setChecked(true);
            bohun2 = 1;
        }else if(bohunn.contains("0")){
            rad_pat_x.setChecked(true);
            bohun2 = 0;
        }
    }

    private JSONObject makeJsonObject(String[] str){
        JSONObject arr = new JSONObject();
        String uid2 = pref.getString("uid","hi");
        String name = str[0];
        String rate = str[1];
        try{
            arr.put("name",uid2);
            arr.put("servnm",name);
            arr.put("rate",rate);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return arr;
    }

    // SharedPreferences에서 "서비스명"과 "평점" 불러오기
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
            //Toast.makeText(this,"값없음",Toast.LENGTH_SHORT).show();
        }
        return urls;
    }

    // SharedPreferences에 "서비스명"과 "평점" 수정 or 저장
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

    //회원정보 수정
    @SuppressLint("StaticFieldLeak")
    public class updateUser extends AsyncTask<String,Void,String>{
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {
            String url = "http://52.79.72.52:5000/modusr";
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            String uid = params[0]; // 아이디
            String name = params[1]; //이름
            String stat = params[2];
            String cit = params[3];
            String age = params[4];
            loc2 = stat +" "+cit; //거주지
            urlBuilder.addEncodedQueryParameter("uid", uid);
            urlBuilder.addEncodedQueryParameter("name", name);
            urlBuilder.addEncodedQueryParameter("age", age);
            urlBuilder.addQueryParameter("gender", Integer.toString(gender));
            urlBuilder.addEncodedQueryParameter("loc", loc2);
            urlBuilder.addQueryParameter("freg", Integer.toString(freg));
            urlBuilder.addQueryParameter("baby", Integer.toString(baby));
            urlBuilder.addQueryParameter("kid", Integer.toString(kid));
            urlBuilder.addQueryParameter("chung", Integer.toString(chung));
            urlBuilder.addQueryParameter("jung", Integer.toString(jung));
            urlBuilder.addQueryParameter("no", Integer.toString(no));
            urlBuilder.addQueryParameter("handi", Integer.toString(handi));
            urlBuilder.addQueryParameter("hanbumo", Integer.toString(hanbumo));
            urlBuilder.addQueryParameter("damunhwa", Integer.toString(damunhwa));
            urlBuilder.addQueryParameter("lowsodek", Integer.toString(low));
            urlBuilder.addQueryParameter("bohun", Integer.toString(bohun2));
            String requestUrl = urlBuilder.build().toString();
            int ageNum = Integer.parseInt(age);
            Request request = new Request.Builder()
                    .url(requestUrl)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) { }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
                    String data = responseBodyCopy.string();
                    Log.d("response: ",data);

                    new Handler(getMainLooper()).post(() -> {
                        Toast.makeText(getApplicationContext(),"정보 수정이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                        editor = pref.edit();
                        editor.putInt("age",ageNum);
                        editor.putInt("gender",gender);
                        editor.putString("loc",loc2);
                        editor.putInt("freg",freg);editor.putInt("baby",baby);editor.putInt("kid",kid);editor.putInt("chung",chung); editor.putInt("jung",jung);editor.putInt("no",no);
                        editor.putInt("handi",handi);editor.putInt("hanbumo",hanbumo);editor.putInt("damunhwa",damunhwa);editor.putInt("lowsodek",low);
                        editor.apply();
                        if(bohun2 ==0){
                            bohun ="0";
                            editor = pref.edit();
                            editor.putString("bohun",bohun);
                            editor.apply();
                        }else if(bohun2 ==1){
                            bohun = "1";
                            editor = pref.edit();
                            editor.putString("bohun",bohun);
                            editor.apply();
                        }
                        setUser();
                    });
                }
            });
            return null;
        }
    }
    // 사용자의 가구상황 설정하기
    public void setFirstSkey(String category){
        sp=getSharedPreferences("Skey", MODE_PRIVATE);
        editor2=sp.edit();
        editor2.remove("first_skey");
        editor2.putString("first_skey",category);
        editor2.apply();
    }

}