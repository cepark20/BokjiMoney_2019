package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TalkItemView extends LinearLayout {
    private TextView talkView, title_1, title_2, title_3, content_1, content_2, content_3;
    LinearLayout talkViewLayout, serviceList, talkBokji_1, talkBokji_2, talkBokji_3;

    public TalkItemView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.talk_item, this, true);
        talkView = findViewById(R.id.talk_view);
        talkViewLayout = findViewById(R.id.talk_view_layout);
        talkBokji_1 = findViewById(R.id.talk_bokji1);
        talkBokji_2 = findViewById(R.id.talk_bokji2);
        talkBokji_3 = findViewById(R.id.talk_bokji3);
        serviceList= findViewById(R.id.service_list);
        title_1 = findViewById(R.id.talk_title1);
        title_2 = findViewById(R.id.talk_title2);
        title_3 = findViewById(R.id.talk_title3);
        content_1 = findViewById(R.id.talk_content1);
        content_2 = findViewById(R.id.talk_content2);
        content_3 = findViewById(R.id.talk_content3);
        Button chat_detail = findViewById(R.id.chat_detail);

        chat_detail.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ServiceList.class);
            intent.putExtra("listkey","추천");
            getContext().startActivity(intent);
        });
    }

    public void setTalk(String talk) { // 톡 내용 채우기
        talkView.setText(talk);
    }

    // 챗봇 응답하기
    public void setBokjiList(String[][] sv_list){
        if((sv_list!=null&&sv_list.length>=3&&sv_list[0]!=null)) {// 서버에서 "추천"에 대한 답이 왔을 때
            if(sv_list[0][0]!=null&& !sv_list[0][0].equals("")){

                title_1.setText(sv_list[0][0]);
                if(title_1.getText()!=null&&title_1.getText()!=""){
                    title_1.setOnClickListener(v -> {
                        Intent intent = new Intent(getContext(), ServiceDetail.class);
                        intent.putExtra("service_name",title_1.getText());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().getApplicationContext().startActivity(intent);
                    });
                }
                if(sv_list[0][1].equals("") ||sv_list[0][1].length()<=1){
                    content_1.setVisibility(GONE);
                }else{
                    if(sv_list[0][1].length()>25)content_1.setText(sv_list[0][1].substring(0,25)+"...");
                    else content_1.setText(sv_list[0][1]);
                }

                title_2.setText(sv_list[1][0]);
                if(title_2.getText()!=null&&title_2.getText()!=""){
                    title_2.setOnClickListener(v -> {
                        Intent intent = new Intent(getContext(), ServiceDetail.class);
                        intent.putExtra("service_name",title_2.getText());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().getApplicationContext().startActivity(intent);
                    });
                }
                if(sv_list[1][1].equals("") ||sv_list[1][1].length()<=1){
                    content_2.setVisibility(GONE);
                }else{
                    if(sv_list[1][1].length()>25)content_2.setText(sv_list[1][1].substring(0,25)+"...");
                    else content_2.setText(sv_list[1][1]);
                }

                title_3.setText(sv_list[2][0]);
                if(title_3.getText()!=null&&title_3.getText()!=""){
                    title_3.setOnClickListener(v -> {
                        Intent intent = new Intent(getContext(), ServiceDetail.class);
                        intent.putExtra("service_name",title_3.getText());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getContext().getApplicationContext().startActivity(intent);
                    });
                }
                if(sv_list[2][1].equals("") ||sv_list[2][1].length()<=1){
                    content_3.setVisibility(GONE);
                }else{
                    if(sv_list[2][1].length()>25)content_3.setText(sv_list[2][1].substring(0,25)+"...");
                    else content_3.setText(sv_list[2][1]);
                    serviceList.setVisibility(View.VISIBLE);
                }
            }else{
                talkView.setText("알맞는 서비스가 없습니다. 다시 입력해주세요");
                serviceList.setVisibility(View.GONE);
            }
        }else if(sv_list.length<3){
            if(sv_list[0][0]!=null&& !sv_list[0][0].equals("")){
                title_1.setText(sv_list[0][0]);
                if(sv_list[0][1].length()>25)content_1.setText(sv_list[0][1].substring(0,25)+"...");
                else content_1.setText(sv_list[0][1]);
                if(sv_list.length==2&&sv_list[1][0]!=null&& !sv_list[1][0].equals("")){
                    title_2.setText(sv_list[1][0]);
                    if(sv_list[1][1].length()>25)content_2.setText(sv_list[1][1].substring(0,25)+"...");
                    else content_2.setText(sv_list[1][1]);
                }else{
                    talkBokji_2.setVisibility(View.GONE);
                }
                talkBokji_3.setVisibility(View.GONE);
                serviceList.setVisibility(View.GONE);
            }
        }else{
            talkView.setText("알맞는 서비스가 없습니다. 다시 입력해주세요");
            serviceList.setVisibility(View.GONE);
        }
    }

    // 챗봇 응답 시 복지리스트 유무 설정하기
    public void setListVisible(Boolean visibility){
        if(visibility){
            serviceList.setVisibility(View.VISIBLE);
        }else{
            serviceList.setVisibility(View.GONE);
        }
    }

    // 챗 배경색상 변경
    public void setBackGroundWhite() { // 내가 대화 입력 시
        talkView.setBackgroundResource(R.drawable.round_background2);
        talkView.setTextColor(Color.BLACK);
        talkViewLayout.setGravity(Gravity.END);
        serviceList.setVisibility(View.GONE);
    }
}
