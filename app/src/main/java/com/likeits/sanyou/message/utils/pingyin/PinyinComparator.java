package com.likeits.sanyou.message.utils.pingyin;


import com.likeits.sanyou.message.model.FriendBean;

import java.util.Comparator;

/**
 * 
 * @author 
 *
 */
public class PinyinComparator implements Comparator<FriendBean> {


	public static PinyinComparator instance = null;

	public static PinyinComparator getInstance() {
		if (instance == null) {
			instance = new PinyinComparator();
		}
		return instance;
	}

	public int compare(FriendBean o1, FriendBean o2) {
		if (o1.getLetters().equals("@")
				|| o2.getLetters().equals("#")) {
			return -1;
		} else if (o1.getLetters().equals("#")
				|| o2.getLetters().equals("@")) {
			return 1;
		} else {
			return o1.getLetters().compareTo(o2.getLetters());
		}
	}

}
