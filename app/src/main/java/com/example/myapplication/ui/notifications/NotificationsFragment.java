package com.example.myapplication.ui.notifications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import org.json.JSONArray;
import org.json.JSONException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private NotiAdapter adapter;
    private RecyclerView notiRecyclerView;
    private ArrayList<String> noti_list, date_list;
    private ArrayList<NotiItem> items;
    private int[] images = {R.drawable.heart2, R.drawable.preg, R.drawable.milk, R.drawable.medicine};

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        items = new ArrayList<>();
        noti_list = new ArrayList<>();
        date_list = new ArrayList<>();
        notiRecyclerView = root.findViewById(R.id.notiRecyclerView);

        getNotiList(); // 관심 서비스에 등록된 서비스 리스트 가져오기
        setRecyclerView(); // 복지 서비스 리사이클러뷰 설정하기

        // 복지서비스 길게 눌렀을 때
        adapter.setOnItemLongClickListener((holder, view, position) -> {
            NotiItem item = adapter.getItem(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("알림 해제");
            builder.setMessage("알림을 해제 하시겠습니까?");
            builder.setNegativeButton("아니오",
                    (dialog, which) -> { });
            builder.setPositiveButton("예",
                    (dialog, which) -> {
                        Toast.makeText(getContext(),item.getTitle() + " 알림이 해제되었습니다.",Toast.LENGTH_SHORT).show();
                        noti_list.remove(position);
                        date_list.remove(position);
                        setStringArrayPref(getContext(),"notiList",noti_list);
                        setStringArrayPref(getContext(),"dateList",date_list);
                        items.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, items.size());
                        // adapter.notifyDataSetChanged();
                        // notiRecyclerView.setAdapter(adapter); // UI 갱신 시 프레그먼트는 setAdapter 해주기
                    });
            builder.show();
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getNotiList(){
        noti_list=getStringArrayPref(getContext(),"notiList");
        date_list = getStringArrayPref(getContext(),"dateList");
        for(int i=0;i<noti_list.size();i++){
            switch (noti_list.get(i)) {
                case "산모.신생아 건강관리 지원": {
                    String dday = getDDay(date_list.get(i), 30, 0);
                    //adapter.addItem(new NotiItem(images[1], noti_list.get(i),  "등록일자 : " + date_list.get(i), dday));
                    items.add(new NotiItem(images[1], noti_list.get(i),  "등록일자 : " + date_list.get(i), dday));
                    break;
                }
                case "저소득층 기저귀.조제분유 지원": {
                    String dday = getDDay(date_list.get(i), 60, 0);
                    //adapter.addItem(new NotiItem(images[2], noti_list.get(i),  "등록일자 : " + date_list.get(i), dday));
                    items.add(new NotiItem(images[2], noti_list.get(i),  "등록일자 : " + date_list.get(i), dday));
                    break;
                }
                case "고위험 임산부 의료비 지원": {
                    String dday = getDDay(date_list.get(i), 0, 6);
                    //adapter.addItem(new NotiItem(images[3], noti_list.get(i),  "등록일자 : " + date_list.get(i), dday));
                    items.add(new NotiItem(images[3], noti_list.get(i),  "등록일자 : " + date_list.get(i), dday));
                    break;
                }
                default:
                    //adapter.addItem(new NotiItem(images[0], noti_list.get(i),  "등록일자 : " + date_list.get(i), "(상시접수"));
                    items.add(new NotiItem(images[1], noti_list.get(i),  "등록일자 : " + date_list.get(i), "(상시접수)"));
                    break;
            }
        }
    }

    public void setRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        notiRecyclerView.setLayoutManager(layoutManager);
        adapter = new NotiAdapter(items);
        notiRecyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDDay(String regDate, int plusDate, int plusMonth){
        LocalDate startDate =  LocalDate.parse(regDate, DateTimeFormatter.ISO_DATE);
        long betDate = 0;
        if(plusDate == 0){
            LocalDate endDate = startDate.plusMonths(plusMonth);
            betDate = ChronoUnit.DAYS.between(startDate, endDate);
        }else if(plusMonth == 0){
            LocalDate endDate =  startDate.plusDays(plusDate);
            betDate = ChronoUnit.DAYS.between(startDate, endDate);
        }
        return String.valueOf(betDate);
    }

    // SharedPreferences에 있는 ArrayList값 읽어오기
    private ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> list = new ArrayList<>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String ele = a.optString(i);
                    list.add(ele);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    // SharedPreferences에 ArrayList 저장하기
    private void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            jsonArray.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, jsonArray.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

}