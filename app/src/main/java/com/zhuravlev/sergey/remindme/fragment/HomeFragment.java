package com.zhuravlev.sergey.remindme.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuravlev.sergey.remindme.R;
import com.zhuravlev.sergey.remindme.adapter.LotListAdapter;
import com.zhuravlev.sergey.remindme.dto.LotDTO;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_history;

    public static HomeFragment getInstance(Context context) {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle(context.getString(R.string.tab_item_home));

        return fragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new LotListAdapter(getRemindDTOList()));
        return view;
    }

    private List<LotDTO> getRemindDTOList() {
        List<LotDTO> data = new ArrayList<>();
        data.add(new LotDTO("Носки", "Очень мягонькие", 5.44));
        data.add(new LotDTO("Колготки", "Вкусные", 12.63));
        data.add(new LotDTO("Игровой компьютер", "Мощный. Видеокарта gt4400, процессор Intel Celeron", 7.42));
        return data;
    }

}
