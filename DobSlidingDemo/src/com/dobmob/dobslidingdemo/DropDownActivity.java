package com.dobmob.dobslidingdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

public class DropDownActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private View mHolder;

	final String tag = DropDownActivity.class.getSimpleName();

	TextView btnClassfic, btnSort;

	ListView listView = null;

	DataAdapter adapter = null;

	ArrayList<String> dataList1 = new ArrayList<String>();
	ArrayList<String> dataList2 = new ArrayList<String>();
	ArrayList<String> dataList = new ArrayList<String>();

	Map<String, Integer> currentMap = new HashMap<String, Integer>();
	final String leibie = "leibie", sort = "sort";
	boolean leibieIsShow = false, sortIsShow = false;

	View hiddenview = null;

	ImageView iv = null;

	// Handler hanlder = new Handler();
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				dataList.clear();
				dataList.addAll(dataList1);
				adapter.setSelectedPosition(currentMap.get(leibie));
				adapter.notifyDataSetChanged();
				animateExpanding(hiddenview, 400);
				sortIsShow = false;
				leibieIsShow = true;
				break;
			case 2:
				dataList.clear();
				dataList.addAll(dataList2);
				adapter.setSelectedPosition(currentMap.get(sort));
				adapter.notifyDataSetChanged();
				animateExpanding(hiddenview, 400);
				sortIsShow = true;
				leibieIsShow = false;
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_drop_down_example);

		initView();

		getData();
	}

	private void getData() {
		dataList1.add("全部");
		dataList1.add("川菜");
		dataList1.add("粤菜");
		dataList1.add("湘菜");
		dataList1.add("东北菜");
		dataList1.add("上海菜");

		dataList2.add("综合排序");
		dataList2.add("距离排序");
		dataList2.add("价格排序");
		dataList2.add("评分排序");

		currentMap.put(leibie, 0);
		currentMap.put(sort, 0);

		adapter = new DataAdapter(this, dataList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	private void initView() {
		mHolder = findViewById(R.id.holder);

		hiddenview = findViewById(R.id.hiddenview);

		listView = (ListView) findViewById(R.id.listView);
		mHolder.setOnClickListener(this);

		btnClassfic = (TextView) findViewById(R.id.btnClassfic);
		btnSort = (TextView) findViewById(R.id.btnSort);
		btnClassfic.setOnClickListener(this);
		btnSort.setOnClickListener(this);

		iv = (ImageView) findViewById(R.id.img);
		iv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					Toast.makeText(DropDownActivity.this,
							"--------up----------", 2).show();
					setAlphaAnimation(1, 0.5f, 100, R.color.white);
					leibieIsShow = false;
					sortIsShow = false;
					animateCollapsing(hiddenview, 100);
					return true;
				}
				return false;
			}
		});

		iv.setOnClickListener(this);
	}

	public static ValueAnimator createHeightAnimator(final View view,
			int start, int end, int duration) {
		ValueAnimator animator = ValueAnimator.ofInt(start, end);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				int value = (Integer) valueAnimator.getAnimatedValue();
				ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
				layoutParams.height = value;
				view.setLayoutParams(layoutParams);
			}
		});
		animator.setDuration(duration);
		// Interpolator
		animator.setInterpolator(new AccelerateDecelerateInterpolator());

		return animator;
	}

	/**
	 * view展开
	 * 
	 * @param view
	 * @param duration
	 */
	public static void animateExpanding(final View view, int duration) {
		view.setVisibility(View.VISIBLE);
		final int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(widthSpec, heightSpec);
		ValueAnimator animator = createHeightAnimator(view, 0,
				view.getMeasuredHeight(), duration);
		animator.start();
	}

	/**
	 * view 折叠
	 * 
	 * @param view
	 * @param duration
	 */
	public void animateCollapsing(final View view, int duration) {
		int origHeight = view.getHeight();
		ValueAnimator animator = createHeightAnimator(view, origHeight, 0,
				duration);
		animator.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator animation) {
				view.setVisibility(View.GONE);
			};
		});
		animator.start();
	}

	/**
	 * view 折叠
	 * 
	 * @param view
	 * @param duration
	 * @param flag
	 *            发送消息
	 */
	public void animateCollapsing(final View view, int duration, final int flag) {
		int origHeight = view.getHeight();
		ValueAnimator animator = createHeightAnimator(view, origHeight, 0,
				duration);
		animator.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator animation) {
				view.setVisibility(View.GONE);
				handler.sendEmptyMessage(flag);
			};
		});
		animator.start();
	}

	@Override
	public void onClick(View v) {
		if (v == mHolder) {
			if (View.GONE == findViewById(R.id.hiddenview1).getVisibility()) {
				animateExpanding(findViewById(R.id.hiddenview1), 400);
			} else {
				animateCollapsing(findViewById(R.id.hiddenview1), 400);
			}
		} else if (v == btnClassfic) {
			// 假如类别已经show，消失
			if (leibieIsShow) {
				animateCollapsing(hiddenview, 200);
				setAlphaAnimation(1, 0.5f, 100, R.color.white);
				leibieIsShow = false;
			}
			// 排序弹出
			else if (sortIsShow) {
				animateCollapsing(hiddenview, 200, 1);
			} else {
				handler.sendEmptyMessage(1);
				setAlphaAnimation(0.5f, 1, 400, R.color.bg_yinying);
			}
		} else if (v == btnSort) {
			// 假如类别已经show，消失
			if (leibieIsShow) {
				animateCollapsing(hiddenview, 100, 2);
			}
			// 排序弹出
			else if (sortIsShow) {
				animateCollapsing(hiddenview, 100);
				setAlphaAnimation(1, 0.5f, 100, R.color.white);
				sortIsShow = false;
			} else {
				setAlphaAnimation(0.5f, 1, 400, R.color.bg_yinying);
				handler.sendEmptyMessage(2);
			}
		} else if (v == iv) {
			setAlphaAnimation(1, 0.5f, 100, R.color.white);
			leibieIsShow = false;
			sortIsShow = false;
			animateCollapsing(hiddenview, 100);
		}
	}

	/**
	 * 设置背景渐变----------没有实现（AlphaAnimation 不可以 ，是否可以通过设置一组
	 * iv.setBackgroundResource(color);来实现呢？）
	 * 
	 * @param fromAlpha
	 * @param toAlpha
	 * @param duration
	 * @param color
	 */
	private void setAlphaAnimation(float fromAlpha, float toAlpha,
			int duration, final int color) {
		AlphaAnimation animation = new AlphaAnimation(fromAlpha, toAlpha);
		animation.setDuration(duration);// 设置动画持续时间
		animation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
		iv.setAnimation(animation);
		iv.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				iv.setBackgroundResource(color);
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		adapter.setSelectedPosition(position);
		adapter.notifyDataSetChanged();
		setAlphaAnimation(1, 0.5f, 100, R.color.white);
		// 更改选中的位置
		if (leibieIsShow) {
			currentMap.put(leibie, position);
		} else if (sortIsShow) {
			currentMap.put(sort, position);
		}
		leibieIsShow = false;
		sortIsShow = false;
		animateCollapsing(hiddenview, 100);
		// 其他操作-----------
	}

}
