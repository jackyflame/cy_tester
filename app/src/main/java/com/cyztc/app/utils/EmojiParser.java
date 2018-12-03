package com.cyztc.app.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.EditText;

import com.cyztc.app.R;
import com.cyztc.app.bean.EmojiBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ywl on 2016/12/23.
 */

public class EmojiParser {

    private static EmojiParser sInstance;

    public static EmojiParser getInstance() {
        return sInstance;
    }

    public static void init(Context context) {
        sInstance = new EmojiParser(context);
    }

    private final Context mContext;
    private final String[] mSmileyTexts;
    private final Pattern mPattern;
    private final HashMap<String, Integer> mSmileyToRes;

    private EmojiParser(Context context) {
        mContext = context;
        mSmileyTexts = mContext.getResources().getStringArray(
                DEFAULT_SMILEY_TEXTS);
        mSmileyToRes = buildSmileyToRes();
        mPattern = buildPattern();
    }

    public static final int[] DEFAULT_SMILEY_RES_IDS = new int[]{
            Smileys.getSmileyResource(0),
            Smileys.getSmileyResource(1),
            Smileys.getSmileyResource(2),
            Smileys.getSmileyResource(3),
            Smileys.getSmileyResource(4),
            Smileys.getSmileyResource(5),
            Smileys.getSmileyResource(6),
            Smileys.getSmileyResource(7),
            Smileys.getSmileyResource(8),
            Smileys.getSmileyResource(9),
            Smileys.getSmileyResource(10),
            Smileys.getSmileyResource(11),
            Smileys.getSmileyResource(12),
            Smileys.getSmileyResource(13),
            Smileys.getSmileyResource(14),
            Smileys.getSmileyResource(15),
            Smileys.getSmileyResource(16),
            Smileys.getSmileyResource(17),
            Smileys.getSmileyResource(18),
            Smileys.getSmileyResource(19),
            Smileys.getSmileyResource(20),
            Smileys.getSmileyResource(21),
            Smileys.getSmileyResource(22),
            Smileys.getSmileyResource(23),
            Smileys.getSmileyResource(24),
            Smileys.getSmileyResource(25),
            Smileys.getSmileyResource(26),
            Smileys.getSmileyResource(27),
            Smileys.getSmileyResource(28),
            Smileys.getSmileyResource(29),
            Smileys.getSmileyResource(30),
            Smileys.getSmileyResource(31),
            Smileys.getSmileyResource(32),
            Smileys.getSmileyResource(33),
            Smileys.getSmileyResource(34),
            Smileys.getSmileyResource(35),
            Smileys.getSmileyResource(36),
            Smileys.getSmileyResource(37),
            Smileys.getSmileyResource(38),
            Smileys.getSmileyResource(39),
            Smileys.getSmileyResource(40),
            Smileys.getSmileyResource(41),
            Smileys.getSmileyResource(42),
            Smileys.getSmileyResource(43),
            Smileys.getSmileyResource(44),
            Smileys.getSmileyResource(45),
            Smileys.getSmileyResource(46),
            Smileys.getSmileyResource(47),
            Smileys.getSmileyResource(48),
            Smileys.getSmileyResource(49),
            Smileys.getSmileyResource(50),
            Smileys.getSmileyResource(51),
            Smileys.getSmileyResource(52),
            Smileys.getSmileyResource(53),
            Smileys.getSmileyResource(54),
            Smileys.getSmileyResource(55),
            Smileys.getSmileyResource(56),
            Smileys.getSmileyResource(57),
            Smileys.getSmileyResource(58),
            Smileys.getSmileyResource(59),
            Smileys.getSmileyResource(60),
            Smileys.getSmileyResource(61),
            Smileys.getSmileyResource(62),
            Smileys.getSmileyResource(63),
            Smileys.getSmileyResource(64),
            Smileys.getSmileyResource(65),
            Smileys.getSmileyResource(66),
            Smileys.getSmileyResource(67),
            Smileys.getSmileyResource(68),
            Smileys.getSmileyResource(69),
            Smileys.getSmileyResource(70),
            Smileys.getSmileyResource(71),
            Smileys.getSmileyResource(72),
            Smileys.getSmileyResource(73)};

    static class Smileys {
        // 表情图片集合
        private static final int[] sIconIds = {
                R.drawable.emoji0, R.drawable.emoji1,
                R.drawable.emoji2, R.drawable.emoji3,
                R.drawable.emoji4, R.drawable.emoji5,
                R.drawable.emoji6, R.drawable.emoji7,
                R.drawable.emoji8, R.drawable.emoji9,
                R.drawable.emoji10, R.drawable.emoji11,
                R.drawable.emoji12, R.drawable.emoji13,
                R.drawable.emoji14, R.drawable.emoji15,
                R.drawable.emoji16, R.drawable.emoji17,
                R.drawable.emoji18, R.drawable.emoji19,
                R.drawable.emoji20, R.drawable.emoji21,
                R.drawable.emoji22, R.drawable.emoji23,
                R.drawable.emoji24, R.drawable.emoji25,
                R.drawable.emoji26, R.drawable.emoji27,
                R.drawable.emoji28, R.drawable.emoji29,
                R.drawable.emoji30, R.drawable.emoji31,
                R.drawable.emoji32, R.drawable.emoji33,
                R.drawable.emoji34, R.drawable.emoji35,
                R.drawable.emoji36, R.drawable.emoji37,
                R.drawable.emoji38, R.drawable.emoji39,
                R.drawable.emoji40, R.drawable.emoji41,
                R.drawable.emoji42, R.drawable.emoji43,
                R.drawable.emoji44, R.drawable.emoji45,
                R.drawable.emoji46, R.drawable.emoji47,
                R.drawable.emoji48, R.drawable.emoji49,
                R.drawable.emoji50, R.drawable.emoji51,
                R.drawable.emoji51, R.drawable.emoji52,
                R.drawable.emoji54, R.drawable.emoji55,
                R.drawable.emoji56, R.drawable.emoji57,
                R.drawable.emoji58, R.drawable.emoji59,
                R.drawable.emoji60, R.drawable.emoji61,
                R.drawable.emoji62, R.drawable.emoji63,
                R.drawable.emoji64, R.drawable.emoji65,
                R.drawable.emoji66, R.drawable.emoji67,
                R.drawable.emoji68, R.drawable.emoji69,
                R.drawable.emoji70, R.drawable.emoji71,
                R.drawable.emoji72, R.drawable.emoji73
        };

        // 得到图片表情 根据id
        public static int getSmileyResource(int which) {
            return sIconIds[which];
        }
    }


    public static final int DEFAULT_SMILEY_TEXTS = R.array.emoji_arry;

    private HashMap<String, Integer> buildSmileyToRes() {
        if (DEFAULT_SMILEY_RES_IDS.length != mSmileyTexts.length) {
            throw new IllegalStateException("Smiley resource ID/text mismatch");
        }
        HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>(
                mSmileyTexts.length);
        for (int i = 0; i < mSmileyTexts.length; i++) {
            smileyToRes.put(mSmileyTexts[i], DEFAULT_SMILEY_RES_IDS[i]);
        }
        return smileyToRes;
    }

    public List<EmojiBean> getEmojis()
    {
        if (DEFAULT_SMILEY_RES_IDS.length != mSmileyTexts.length) {
            throw new IllegalStateException("Smiley resource ID/text mismatch");
        }
        List<EmojiBean> datas = new ArrayList<>();
        for (int i = 0; i < mSmileyTexts.length; i++) {
            EmojiBean emojiBean = new EmojiBean();
            emojiBean.setName(mSmileyTexts[i]);
            emojiBean.setSrcid(DEFAULT_SMILEY_RES_IDS[i]);
            datas.add(emojiBean);
        }
        return datas;
    }

    // 构建正则表达式
    private Pattern buildPattern() {
        StringBuilder patternString = new StringBuilder(mSmileyTexts.length * 3);
        patternString.append('(');
        for (String s : mSmileyTexts) {
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        patternString.replace(patternString.length() - 1, patternString
                .length(), ")");
        return Pattern.compile(patternString.toString());
    }

    // 根据文本替换成图片
    public CharSequence strToSmiley(CharSequence text, int size) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = mPattern.matcher(text);
        while (matcher.find()) {
            int resId = mSmileyToRes.get(matcher.group());
            Drawable drawable = mContext.getResources().getDrawable(resId);
            drawable.setBounds(0, 0, 50, 50);//这里设置图片的大小
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            builder.setSpan(imageSpan, matcher.start(),
                    matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    public void strToSmiley(CharSequence text, EditText editText, int size) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = mPattern.matcher(text);
        while (matcher.find()) {
            int resId = mSmileyToRes.get(matcher.group());
            Drawable drawable = mContext.getResources().getDrawable(resId);
            drawable.setBounds(0, 0, size, size);//这里设置图片的大小
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            builder.setSpan(imageSpan, matcher.start(),
                    matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        editText.setText(builder);
        editText.setSelection(builder.length());
    }

}
