package com.cyztc.app.utils;

import com.cyztc.app.bean.ContactBean;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class ListIndexUtil {

	private HashMap<String, Integer> selector;// 存放含有索引字母的位置
	private ArrayList<ContactBean> datas = null;
	private String[] indexStr = { "↑", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z", "#" };
	
	public void setData(ArrayList<ContactBean> sourcedatas) {
		String[] allNames = sortIndex(sourcedatas);// 按昵称首字母排序
		datas = getAllLists(allNames, sourcedatas);// 根据排号的顺序对数据进行排序， 并返回
		selector = new HashMap<String, Integer>();
		// 遍历排好序的数据，获取每个字母的位置
		for (int i = 0; i < indexStr.length; i++) {
			for (int j = 0; j < datas.size(); j++) {
				if (datas
						.get(j)
						.getPinyin()
						.toLowerCase(Locale.getDefault())
						.startsWith(
								indexStr[i].toLowerCase(Locale.getDefault()))) {
					selector.put(indexStr[i], j);
					break;
				}
				String pinyin = datas.get(j).getPinyin();
				if (indexStr[i].equals("#")
						&& isNumeric(pinyin.substring(0, 1))) {
					selector.put(indexStr[i], j);
					return;
				}
			}
		}

	}

	/**
	 * 将汉字转换为全拼
	 * 
	 * @param src
	 * @return String
	 */
	public String getPinYin(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		// 设置汉字拼音输出的格式

		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// 判断能否为汉字字符
				if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 将汉字的几种全拼都存到t2数组中
					t4 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4后
				} else {
					// 如果不是汉字字符，间接取出字符并连接到字符串t4后
					t4 += Character.toString(t1[i]);
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return t4;
	}

	/**
	 * 把数据排序，并把A-Z顺序加进去
	 * 
	 * @return
	 */
	public String[] sortIndex(List<ContactBean> baseBeans) {
		TreeSet<String> set = new TreeSet<String>();
		for (ContactBean baseBean : baseBeans) {
			char ch = baseBean.getPinyin().charAt(0);
			set.add(String.valueOf(ch).toUpperCase(Locale.getDefault()));// 获取首字母
		}
		String[] names = new String[baseBeans.size() + set.size()];// 新数组，用于保存首字母
		int i = 0;
		for (String string : set) { // 把set中的字母添加到新数组中（前面）
			names[i] = string;
			i++;
		}

		String[] pyheader = new String[baseBeans.size()];
		for (int j = 0; j < baseBeans.size(); j++) {
			pyheader[j] = baseBeans.get(j).getPinyin();
		}

		System.arraycopy(pyheader, 0, names, set.size(), pyheader.length);// 将转换为拼音的数组加到新数组后面
		// 自动按照首字母排序
		Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);// 严格按照字母顺序排序，忽略字母大小写，结果为按拼音排序的数组返回
		return names;

	}

	/**
	 * 根据名字排序对数据进行排序 因为默认是数字在首位，为了把数字排到末尾，需要进行转换
	 * 
	 * @param arry
	 * @return
	 */
	public ArrayList<ContactBean> getAllLists(String[] arry,
			ArrayList<ContactBean> sourceDatas) {
		ArrayList<ContactBean> lists = new ArrayList<ContactBean>();// 保存排好序的数据
		ArrayList<ContactBean> lists2 = new ArrayList<ContactBean>();// 保存数字开头的数据
		ArrayList<ContactBean> lists3 = new ArrayList<ContactBean>();// 保存字母数据
		// 对数据进行排序
		for (int i = 0; i < arry.length; i++) {
			boolean isContanin = false;
			for (int j = 0; j < sourceDatas.size(); j++) {
				if (arry[i].equals(sourceDatas.get(j).getPinyin())) {
					lists.add(sourceDatas.get(j));
					isContanin = true;
					break;
				}
			}
			if(!isContanin)
			{
				ContactBean contactBean = new ContactBean();
				 contactBean.setPinyin(arry[i]);
				 contactBean.setStudentName(arry[i]);
				contactBean.setType(1);
				 lists.add(contactBean);
			}
		}
		// 分离出数字数据和字母数据
		int index = getLetter(lists);// 获取字母开头的位置
		for (int i = 0; i < lists.size(); i++) {
			if (i < index) {
				lists2.add(lists.get(i));
			} else {
				lists3.add(lists.get(i));
			}
		}
		lists.clear();
		lists.addAll(lists3);
		lists.addAll(lists2);

		return lists;
	}

	/**
	 * 是否是字母
	 * 
	 * @param str
	 * @return
	 */
	public boolean isLetter(String str) {
		Pattern pattern = Pattern.compile("[a-zA-Z]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 获取已字母开头的联系人的位置
	 * 
	 * @param arry
	 * @return
	 */
	public int getLetter(ArrayList<ContactBean> arry) {

		int length = arry.size();
		for (int i = 0; i < length; i++) {
			String pinyin = arry.get(i).getPinyin();
			if (isLetter(pinyin.substring(0, 1))) {
				return i;
			}
		}
		return length;
	}

	public HashMap<String, Integer> getSelector() {
		return selector;
	}

	public ArrayList<ContactBean> getDatas() {
		return datas;
	}

	public String[] getIndexStr() {
		return indexStr;
	}

}
