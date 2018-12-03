package com.cyztc.app.views.home.tribe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.cyztc.app.R;
import com.cyztc.app.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by ywl on 2016/11/20.
 */

public class CreateTribeFragment3 extends BaseFragment{

    @BindView(R.id.et_disrc)
    EditText etDisrc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_tribe_step3_layout);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public String getTribeDisrc()
    {
        return etDisrc.getText().toString().trim();
    }
}
