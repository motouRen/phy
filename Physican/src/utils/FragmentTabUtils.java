package utils;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.arvin.physican.R;

/**
 * ProjectName：XiaoLi Created by Administrator on 15-9-14. Detail：
 */
public class FragmentTabUtils implements RadioGroup.OnCheckedChangeListener {
	private List<Fragment> fragments; // 一个tab页面对应一个Fragment
	private RadioGroup rgs; // 用于切换tab
	private FragmentManager fragmentManager; // Fragment所属的Activity
	private int fragmentContentId; // Activity中所要被替换的区域的id
	private int currentTab; // 当前Tab页面索引

	/**
	 * @param fragmentManager
	 * @param fragments
	 * @param fragmentContentId
	 * @param rgs
	 */
	public FragmentTabUtils(FragmentManager fragmentManager,
			List<Fragment> fragments, int fragmentContentId, RadioGroup rgs) {
		this.fragments = fragments;
		this.rgs = rgs;
		this.fragmentManager = fragmentManager;
		this.fragmentContentId = fragmentContentId;
		rgs.setOnCheckedChangeListener(this);
		onCheckedChanged(rgs, R.id.radio_inquerymessage);

	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		for (int i = 0; i < rgs.getChildCount(); i++) {
			if (rgs.getChildAt(i).getId() == checkedId) {
				Fragment fragment = fragments.get(i);
				FragmentTransaction ft = obtainFragmentTransaction(i);
				getCurrentFragment().onStop(); // 暂停当前tab
				if (fragment.isAdded()) {
					fragment.onStart(); // 启动目标tab的fragment onStart()
				} else {
					ft.add(fragmentContentId, fragment);
					ft.commit();
				}
				showTab(i);
			}
		}
	}

	/**
	 * 切换tab
	 *
	 * @param idx
	 */
	private void showTab(int idx) {
		for (int i = 0; i < fragments.size(); i++) {
			Fragment fragment = fragments.get(i);
			FragmentTransaction ft = obtainFragmentTransaction(idx);
			if (idx == i) {
				ft.show(fragment);
			} else {
				fragmentManager.popBackStack();

				ft.remove(fragment);
			}
			ft.commit();
		}
		currentTab = idx; // 更新目标tab为当前tab
	}

	/**
	 * 获取一个带动画的FragmentTransaction
	 *
	 * @param index
	 * @return
	 */
	private FragmentTransaction obtainFragmentTransaction(int index) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		// 设置切换动画
		if (index > currentTab) {
			ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
		} else {
			ft.setCustomAnimations(R.anim.slide_right_in,
					R.anim.slide_right_out);
		}
		return ft;
	}

	public int getCurrentTab() {
		return currentTab;
	}

	public Fragment getCurrentFragment() {
		return fragments.get(currentTab);
	}

}