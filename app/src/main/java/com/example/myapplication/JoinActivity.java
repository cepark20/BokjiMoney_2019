package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;

public class JoinActivity extends AppCompatActivity {

    LinearLayout l_preg,l_baby,l_stud,l_adult,l_middle,l_old,l_disa,l_one,l_multi,l_low;
    ImageView i_preg,i_baby,i_stud,i_adult,i_middle,i_old,i_disa,i_one,i_multi,i_low;
    TextView tv_preg,tv_baby,tv_stud,tv_adult,tv_middle,tv_old,tv_disa,tv_one,tv_multi,tv_low;
    EditText et_name,et_age,et_uid,et_pwd;
    Button bt_join;
    String uid, pwd, name, age, state, city, address;
    int gender2,age2,dis,ped,preg2,baby2,kid,chung,jung,no,low2,multi2,han;
    RadioGroup radioGroup,radioGroup2,radioGroup3;
    RadioButton rd_btn1,rd_btn2;
    boolean preg,baby,stud,adult,middle,old;
    boolean disa,one,multi,low;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        getSupportActionBar().hide();
        initialize();

        l_preg.setOnClickListener(v -> {
            if(preg){
                tv_preg.setTextColor(Color.parseColor("#484848"));
                i_preg.setImageResource(R.drawable.pregnancy_mint);
                l_preg.setBackground(getResources().getDrawable(R.drawable.r_corner_gray));
                preg=false;
                preg2= 0;
            }else{
                tv_preg.setTextColor(Color.parseColor("#ffffff"));
                i_preg.setImageResource(R.drawable.pregnancy_white);
                l_preg.setBackground(getResources().getDrawable(R.drawable.r_corner_mint));
                preg=true;
                preg2 = 1;
            }

        });

        l_baby.setOnClickListener(v -> {
            if(baby){
                tv_baby.setTextColor(Color.parseColor("#484848"));
                i_baby.setImageResource(R.drawable.baby_mint);
                l_baby.setBackground(getResources().getDrawable(R.drawable.r_corner_gray));
                baby=false;
                baby2 = 0;
            }else{
                tv_baby.setTextColor(Color.parseColor("#ffffff"));
                i_baby.setImageResource(R.drawable.baby_white);
                l_baby.setBackground(getResources().getDrawable(R.drawable.r_corner_mint));
                baby=true;
                baby2 = 1;
            }

        });

        l_stud.setOnClickListener(v -> {
            if(stud){
                tv_stud.setTextColor(Color.parseColor("#484848"));
                i_stud.setImageResource(R.drawable.teen_mint);
                l_stud.setBackground(getResources().getDrawable(R.drawable.r_corner_gray));
                stud=false;
                kid = 0;
            }else{
                tv_stud.setTextColor(Color.parseColor("#ffffff"));
                i_stud.setImageResource(R.drawable.teen_white);
                l_stud.setBackground(getResources().getDrawable(R.drawable.r_corner_mint));
                stud=true;
                kid = 1;
            }

        });

        l_adult.setOnClickListener(v -> {
            if(adult){
                tv_adult.setTextColor(Color.parseColor("#484848"));
                i_adult.setImageResource(R.drawable.adult_mint);
                l_adult.setBackground(getResources().getDrawable(R.drawable.r_corner_gray));
                adult=false;
                chung = 0;
            }else{
                tv_adult.setTextColor(Color.parseColor("#ffffff"));
                i_adult.setImageResource(R.drawable.adult_white);
                l_adult.setBackground(getResources().getDrawable(R.drawable.r_corner_mint));
                adult=true;
                chung=1;
            }

        });
        l_middle.setOnClickListener(v -> {
            if(middle){
                tv_middle.setTextColor(Color.parseColor("#484848"));
                i_middle.setImageResource(R.drawable.middle_mint);
                l_middle.setBackground(getResources().getDrawable(R.drawable.r_corner_gray));
                middle=false;
                jung = 0;
            }else{
                tv_middle.setTextColor(Color.parseColor("#ffffff"));
                i_middle.setImageResource(R.drawable.middle_white);
                l_middle.setBackground(getResources().getDrawable(R.drawable.r_corner_mint));
                middle=true;
                jung = 1;
            }
        });
        //생애주기 - 노년
        l_old.setOnClickListener(v -> {
            if(old){
                tv_old.setTextColor(Color.parseColor("#484848"));
                i_old.setImageResource(R.drawable.old_mint);
                l_old.setBackground(getResources().getDrawable(R.drawable.r_corner_gray));
                old=false;
                no = 0;
            }else{
                tv_old.setTextColor(Color.parseColor("#ffffff"));
                i_old.setImageResource(R.drawable.old_white);
                l_old.setBackground(getResources().getDrawable(R.drawable.r_corner_mint));
                old=true;
                no = 1;
            }
        });

        //장애여부 설정
        l_disa.setOnClickListener(v -> {
            if(disa){ //비대상
                tv_disa.setTextColor(Color.parseColor("#484848"));
                i_disa.setImageResource(R.drawable.disabled_mint);
                l_disa.setBackground(getResources().getDrawable(R.drawable.r_corner_gray));
                disa=false;
                dis = 0;
            }else{ //대상
                tv_disa.setTextColor(Color.parseColor("#ffffff"));
                i_disa.setImageResource(R.drawable.disabled_white);
                l_disa.setBackground(getResources().getDrawable(R.drawable.r_corner_mint));
                disa=true;
                dis = 1;
            }

        });

        l_one.setOnClickListener(v -> {
            if(one){
                tv_one.setTextColor(Color.parseColor("#484848"));
                i_one.setImageResource(R.drawable.oneparent_mint);
                l_one.setBackground(getResources().getDrawable(R.drawable.r_corner_gray));
                one=false;
                han = 0;
            }else{
                tv_one.setTextColor(Color.parseColor("#ffffff"));
                i_one.setImageResource(R.drawable.oneparent_white);
                l_one.setBackground(getResources().getDrawable(R.drawable.r_corner_mint));
                one=true;
                han = 1;
            }

        });

        l_multi.setOnClickListener(v -> {
            if(multi){
                tv_multi.setTextColor(Color.parseColor("#484848"));
                i_multi.setImageResource(R.drawable.multi_mint);
                l_multi.setBackground(getResources().getDrawable(R.drawable.r_corner_gray));
                multi=false;
                multi2 = 0;
            }else{
                tv_multi.setTextColor(Color.parseColor("#ffffff"));
                i_multi.setImageResource(R.drawable.multi_white);
                l_multi.setBackground(getResources().getDrawable(R.drawable.r_corner_mint));
                multi=true;
                multi2 = 1;
            }
        });

        l_low.setOnClickListener(v -> {
            if(low){
                tv_low.setTextColor(Color.parseColor("#484848"));
                i_low.setImageResource(R.drawable.low_mint);
                l_low.setBackground(getResources().getDrawable(R.drawable.r_corner_gray));
                low=false;
                low2 = 0;
            }else{
                tv_low.setTextColor(Color.parseColor("#ffffff"));
                i_low.setImageResource(R.drawable.low_white);
                l_low.setBackground(getResources().getDrawable(R.drawable.r_corner_mint));
                low=true;
                low2 = 1;
            }
        });

        //성별 설정
        radioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId){
                case R.id.rd_btn1: //여자
                    gender2 = 0; break;
                case R.id.rd_btn2:
                    gender2 = 1; break; //남자
            }
        });

        //보훈 설정
        radioGroup3.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i){
                case R.id.vet_btn1: //대상
                    ped = 1; break;
                case R.id.vet_btn2: //비대상
                    ped = 0; break;
            }
        });

        // 가입 버튼 클릭
        bt_join.setOnClickListener(v -> {
            uid = et_uid.getText().toString().trim(); // 아이디
            pwd = et_pwd.getText().toString().trim(); //비밀번호
            name = et_name.getText().toString().trim(); //이름
            age = et_age.getText().toString().trim();
            age2 = Integer.parseInt(age); //나이
            address = state+" "+city;

            joinUser task = new joinUser();
            task.execute(uid,pwd,name,address);

        });

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("서울특별시");
        arrayList.add("부산광역시");
        arrayList.add("대구광역시");
        arrayList.add("인천광역시");
        arrayList.add("광주광역시");
        arrayList.add("대전광역시");
        arrayList.add("울산광역시");
        arrayList.add("세종특별자치시");
        arrayList.add("경기도");
        arrayList.add("강원도");
        arrayList.add("충청북도");
        arrayList.add("충청남도");
        arrayList.add("전라북도");
        arrayList.add("전라남도");
        arrayList.add("경상북도");
        arrayList.add("경상남도");
        arrayList.add("제주특별자치도");

        final ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList2.add("강남구");
        arrayList2.add("강동구");
        arrayList2.add("강북구");
        arrayList2.add("강서구");
        arrayList2.add("관악구");
        arrayList2.add("광진구");
        arrayList2.add("구로구");
        arrayList2.add("금천구");
        arrayList2.add("노원구");
        arrayList2.add("도봉구");
        arrayList2.add("동대문구");
        arrayList2.add("동작구");
        arrayList2.add("마포구");
        arrayList2.add("서대문구");
        arrayList2.add("서초구");
        arrayList2.add("성동구");
        arrayList2.add("성북구");
        arrayList2.add("송파구");
        arrayList2.add("양천구");
        arrayList2.add("영등포구");
        arrayList2.add("용산구");
        arrayList2.add("은평구");
        arrayList2.add("종로구");
        arrayList2.add("중구");
        arrayList2.add("중랑구");

        final ArrayList<String> arrGyung = new ArrayList<>();
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

        Spinner spinner1 = findViewById(R.id.spinner1);
        final Spinner spinner2 = findViewById(R.id.spinner2);
        final ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                state = arrayAdapter.getItem(position).toString();

                if(state.equals("서울특별시")){
                    final ArrayAdapter arrayAdapter2 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList2);
                    spinner2.setAdapter(arrayAdapter2);
                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            city = arrayAdapter2.getItem(position).toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            //city = "강남구";
                        }
                    });
                }else if(state.equals("경기도")){
                    final ArrayAdapter arrayAdapter3 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrGyung);
                    spinner2.setAdapter(arrayAdapter3);
                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            city = arrayAdapter3.getItem(position).toString();
                            Log.d("city",city);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            //city = "고양시";
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // state = "서울특별시";
            }
        });
    }

    public void initialize(){
        bt_join=findViewById(R.id.btn_join);
        et_name=findViewById(R.id.et_name);
        et_age=findViewById(R.id.et_age);
        et_uid = findViewById(R.id.et_uid);
        et_pwd = findViewById(R.id.et_pwd);
        l_preg=findViewById(R.id.l_pregnant);
        l_baby=findViewById(R.id.l_baby);
        l_stud=findViewById(R.id.l_student);
        l_adult=findViewById(R.id.l_adult);
        l_middle=findViewById(R.id.l_middle);
        l_old=findViewById(R.id.l_old);
        l_disa=findViewById(R.id.l_disable);
        l_one=findViewById(R.id.l_oneparent);
        l_multi=findViewById(R.id.l_multiculture);
        l_low=findViewById(R.id.l_lowmoney);
        tv_preg=findViewById(R.id.tv_pregnant);
        tv_baby=findViewById(R.id.tv_baby);
        tv_stud=findViewById(R.id.tv_student);
        tv_adult=findViewById(R.id.tv_adult);
        tv_middle=findViewById(R.id.tv_middle);
        tv_old=findViewById(R.id.tv_old);
        tv_disa=findViewById(R.id.tv_disable);
        tv_one=findViewById(R.id.tv_oneparent);
        tv_multi=findViewById(R.id.tv_multiculture);
        tv_low=findViewById(R.id.tv_lowmoney);
        i_preg=findViewById(R.id.i_pregnant);
        i_baby=findViewById(R.id.i_baby);
        i_stud=findViewById(R.id.i_student);
        i_adult=findViewById(R.id.i_adult);
        i_middle=findViewById(R.id.i_middle);
        i_old=findViewById(R.id.i_old);
        i_disa=findViewById(R.id.i_disable);
        i_one=findViewById(R.id.i_oneparent);
        i_multi=findViewById(R.id.i_multiculture);
        i_low=findViewById(R.id.i_lowmoney);
        radioGroup =findViewById(R.id.radioGroup);
        radioGroup2 =findViewById(R.id.radioGroup2);
        radioGroup3 =findViewById(R.id.radioGroup3);
        rd_btn1 = findViewById(R.id.rd_btn1);
        rd_btn2 = findViewById(R.id.rd_btn2);
    }

    private void showMessage(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("회원가입이 완료되었습니다.\n" +
                "이제 로그인합니다.");
        builder.setPositiveButton("확인", (dialog, which) -> {
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }
        );
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    @SuppressLint("StaticFieldLeak")
    public class joinUser extends AsyncTask<String,Void,String>{
        OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(String s) {
            showMessage();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = "http://52.79.72.52:5000/signup";
            String uid = params[0];
            String pwd = params[1];
            String name = params[2];
            String address = params[3];
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            urlBuilder.addEncodedQueryParameter("uid", uid);
            urlBuilder.addEncodedQueryParameter("name", name);
            urlBuilder.addEncodedQueryParameter("pwd", pwd);
            urlBuilder.addEncodedQueryParameter("age", age);
            urlBuilder.addQueryParameter("gender", Integer.toString(gender2));
            urlBuilder.addEncodedQueryParameter("loc", address);
            urlBuilder.addQueryParameter("freg", Integer.toString(preg2));
            urlBuilder.addQueryParameter("baby", Integer.toString(baby2));
            urlBuilder.addQueryParameter("kid", Integer.toString(kid));
            urlBuilder.addQueryParameter("chung", Integer.toString(chung));
            urlBuilder.addQueryParameter("jung", Integer.toString(jung));
            urlBuilder.addQueryParameter("no", Integer.toString(no));
            urlBuilder.addQueryParameter("handi", Integer.toString(dis));
            urlBuilder.addQueryParameter("hanbumo", Integer.toString(han));
            urlBuilder.addQueryParameter("damunhwa", Integer.toString(multi2));
            urlBuilder.addQueryParameter("lowsodek", Integer.toString(low2));
            urlBuilder.addQueryParameter("bohun", Integer.toString(ped));
            String requestUrl = urlBuilder.build().toString();
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
                }
            });
            return null;
        }
    }
}
