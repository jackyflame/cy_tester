package com.cyztc.app.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cyztc.app.R;
import com.cyztc.app.bean.BaseBean;
import com.cyztc.app.bean.OnlyStringBean;
import com.cyztc.app.dialog.adapter.SelectedTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ywl on 2017-5-20.
 */

public class SelectedTypeDialog extends BaseDialog{

    @BindView(R.id.listview)
    ListView listView;

    private SelectedTypeAdapter selectedTypeAdapter;
    private List<OnlyStringBean> items;
    private OnItemSelectdListener onItemSelectdListener;

    public void setOnItemSelectdListener(OnItemSelectdListener onItemSelectdListener) {
        this.onItemSelectdListener = onItemSelectdListener;
    }

    public SelectedTypeDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_selected_type_layout);
        setAdapter();
    }

    @OnClick(R.id.tv_cancel)
    public void onClicckCancel(View view)
    {
        dismiss();
    }

    private void setAdapter()
    {
        items = new ArrayList<>();
        selectedTypeAdapter = new SelectedTypeAdapter(context, items);
        listView.setAdapter(selectedTypeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OnlyStringBean onlyStringBean = (OnlyStringBean) listView.getItemAtPosition(position);
                if(onlyStringBean != null)
                {
                    if(onItemSelectdListener != null)
                    {
                        onItemSelectdListener.onItemSelected(onlyStringBean);
                    }
                }
                dismiss();
            }
        });
    }

    public void setDatas(List<OnlyStringBean> datas)
    {
        this.items.clear();
        this.items.addAll(datas);
        selectedTypeAdapter.notifyDataSetChanged();
    }

    public interface OnItemSelectdListener
    {
        void onItemSelected(OnlyStringBean onlyStringBean);
    }


}
