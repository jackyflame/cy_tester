package com.cyztc.app.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.cyztc.app.R;
import com.cyztc.app.bean.EmojiBean;
import com.cyztc.app.dialog.adapter.EmojiAdapter;
import com.cyztc.app.log.MyLog;
import com.cyztc.app.utils.EmojiParser;
import com.cyztc.app.widget.ywl5320.pickphoto.dialog.BaseDialog;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ywl on 2016/6/14.
 */
public class EmojiDialog extends BaseDialog {

    private GridView gridView;
    private Context context;
    private EmojiAdapter emojiAdapter;
    private List<EmojiBean> datas;
    private RelativeLayout rlRoot;

    private OnEmojiListener onEmojiListener;


    public EmojiDialog(Context context) {
        super(context, R.style.StyleDialogTransparent);
        this.context = context;
    }

    public void setOnEmojiListener(OnEmojiListener onEmojiListener) {
        this.onEmojiListener = onEmojiListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = this.getLayoutInflater().inflate(R.layout.dialog_emoji_layout, null);
        AutoUtils.auto(v);
        setContentView(v);
        gridView = (GridView) v.findViewById(R.id.gridview);
        rlRoot = (RelativeLayout) v.findViewById(R.id.rl_root);

        setAdapter();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmojiBean emojiBean = (EmojiBean) gridView.getItemAtPosition(position);
                if(emojiBean != null)
                {
                    if(onEmojiListener != null)
                    {
                        onEmojiListener.onEmoji(emojiBean);
                    }
                }
            }
        });

        rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setAdapter()
    {
        datas = new ArrayList<>();
        EmojiParser.init(context);
        datas.addAll(EmojiParser.getInstance().getEmojis());
        emojiAdapter = new EmojiAdapter(context, datas);
        gridView.setAdapter(emojiAdapter);
    }

    public interface OnEmojiListener
    {
        void onEmoji(EmojiBean emojiBean);
    }

}
