package trikita.anvil;

import android.animation.Animator;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class BaseDSL {

	//
	// Common attrs
	//
	
	// weight constants
	public final static int FILL = ViewGroup.LayoutParams.MATCH_PARENT;
	public final static int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;
	public final static int WRAP = ViewGroup.LayoutParams.WRAP_CONTENT;

	// gravity constants
	public final static int TOP = Gravity.TOP;
	public final static int BOTTOM = Gravity.BOTTOM;
	public final static int LEFT = Gravity.LEFT;
	public final static int RIGHT = Gravity.RIGHT;
	public final static int CENTER_VERTICAL = Gravity.CENTER_VERTICAL;
	public final static int GROW_VERTICAL = Gravity.FILL_VERTICAL;
	public final static int CENTER_HORIZONTAL = Gravity.CENTER_HORIZONTAL;
	public final static int GROW_HORIZONTAL = Gravity.FILL_HORIZONTAL;
	public final static int CENTER = CENTER_VERTICAL|CENTER_HORIZONTAL;
	public final static int GROW = GROW_VERTICAL|GROW_HORIZONTAL;
	public final static int CLIP_VERTICAL = Gravity.CLIP_VERTICAL;
	public final static int CLIP_HORIZONTAL = Gravity.CLIP_HORIZONTAL;
	public final static int START = Gravity.START;
	public final static int END = Gravity.END;

	public static Resources R() {
		return Anvil.currentView().getResources();
	}

	public static boolean isPortrait() {
		return R().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}

	public static float dip(float value) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
				R().getDisplayMetrics());
	}

	public static int dip(int value) {
		return Math.round(dip((float) value));
	}

	public static Void size(int w, int h) {
		return attr(LayoutSizeFunc.instance,
				new AbstractMap.SimpleImmutableEntry<Integer, Integer>(w, h));
	}

	private final static class LayoutSizeFunc
			implements Anvil.AttrFunc<Map.Entry<Integer, Integer>> {
		private final static LayoutSizeFunc instance = new LayoutSizeFunc();
		public void apply(View v, Map.Entry<Integer, Integer> arg,
				Map.Entry<Integer, Integer> old) {
			ViewGroup.LayoutParams p = v.getLayoutParams();
			p.width = arg.getKey();
			p.height = arg.getValue();
			v.setLayoutParams(p);
		}
	}

	public static Void padding(int p) {
		return padding(p, p, p, p);
	}

	public static Void padding(int h, int v) {
		return padding(h, v, h, v);
	}

	public static Void padding(int l, int t, int r, int b) {
		List<Integer> list = new ArrayList<>(4);
		list.add(l);
		list.add(t);
		list.add(r);
		list.add(b);
		return attr(PaddingFunc.instance, list);
	}

	private final static class PaddingFunc implements Anvil.AttrFunc<List<Integer>> {
		private final static PaddingFunc instance = new PaddingFunc();
		public void apply(View v, List<Integer> arg, List<Integer> old) {
			v.setPadding(arg.get(0), arg.get(1), arg.get(2), arg.get(3));
		}
	}

	public static Void margin(int w) {
		return margin(w, w, w, w);
	}

	public static Void margin(int h, int v) {
		return margin(h, v, h, v);
	}

	public static Void margin(int l, int t, int r, int b) {
		List<Integer> list = new ArrayList<>(4);
		list.add(l);
		list.add(t);
		list.add(r);
		list.add(b);
		return attr(LayoutMarginFunc.instance, list);
	}

	private final static class LayoutMarginFunc implements Anvil.AttrFunc<List<Integer>> {
		private final static LayoutMarginFunc instance = new LayoutMarginFunc();
		public void apply(View v, List<Integer> arg, List<Integer> old) {
			ViewGroup.LayoutParams p = v.getLayoutParams();
			if (p instanceof ViewGroup.MarginLayoutParams) {
				ViewGroup.MarginLayoutParams mp = (ViewGroup.MarginLayoutParams) p;
				mp.leftMargin = arg.get(0);
				mp.topMargin = arg.get(1);
				mp.rightMargin = arg.get(2);
				mp.bottomMargin = arg.get(3);
				v.setLayoutParams(mp);
			}
		}
	}

	public static Void weight(float w) {
		return attr(LayoutWeightFunc.instance, w);
	}

	private final static class LayoutWeightFunc implements Anvil.AttrFunc<Float> {
		private final static LayoutWeightFunc instance = new LayoutWeightFunc();
		public void apply(View v, Float arg, Float old) {
			ViewGroup.LayoutParams p = v.getLayoutParams();
			if (p instanceof LinearLayout.LayoutParams) {
				((LinearLayout.LayoutParams) p).weight = arg;
				v.setLayoutParams(p);
			}
		}
	}

	public static Void layoutGravity(int g) {
		return attr(LayoutGravityFunc.instance, g);
	}

	private final static class LayoutGravityFunc implements Anvil.AttrFunc<Integer> {
		private final static LayoutGravityFunc instance = new LayoutGravityFunc();
		public void apply(View v, Integer arg, Integer old) {
			ViewGroup.LayoutParams p = v.getLayoutParams();
			if (p instanceof LinearLayout.LayoutParams) {
				((LinearLayout.LayoutParams) p).gravity = arg;
				v.setLayoutParams(p);
			}
			if (p instanceof FrameLayout.LayoutParams) {
				((FrameLayout.LayoutParams) p).gravity = arg;
				v.setLayoutParams(p);
			}
		}
	}

	public static Void align(int verb) {
		return align(verb, -1);
	}

	public static Void align(int verb, int anchor) {
		return attr(LayoutAlignFunc.instance,
				new AbstractMap.SimpleImmutableEntry<>(verb, anchor));
	}

	public static Void above(int anchor) {
		return align(RelativeLayout.ABOVE, anchor);
	}

	public static Void alignBaseline(int anchor) {
		return align(RelativeLayout.ALIGN_BASELINE, anchor);
	}

	public static Void alignBottom(int anchor) {
		return align(RelativeLayout.ALIGN_BOTTOM, anchor);
	}

	public static Void alignEnd(int anchor) {
		return align(RelativeLayout.ALIGN_END, anchor);
	}

	public static Void alignLeft(int anchor) {
		return align(RelativeLayout.ALIGN_LEFT, anchor);
	}

	public static Void alignParentBottom() {
		return align(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
	}

	public static Void alignParentEnd() {
		return align(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
	}

	public static Void alignParentLeft() {
		return align(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
	}

	public static Void alignParentRight() {
		return align(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
	}

	public static Void alignParentStart() {
		return align(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
	}

	public static Void alignParentTop() {
		return align(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
	}

	public static Void alignRight(int anchor) {
		return align(RelativeLayout.ALIGN_RIGHT, anchor);
	}

	public static Void alignStart(int anchor) {
		return align(RelativeLayout.ALIGN_START, anchor);
	}

	public static Void alignTop(int anchor) {
		return align(RelativeLayout.ALIGN_TOP, anchor);
	}

	public static Void below(int anchor) {
		return align(RelativeLayout.BELOW, anchor);
	}

	public static Void centerHorizontal() {
		return align(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
	}

	public static Void centerInParent() {
		return align(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
	}

	public static Void centerVertical() {
		return align(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
	}

	public static Void toEndOf(int anchor) {
		return align(RelativeLayout.END_OF, anchor);
	}

	public static Void toLeftOf(int anchor) {
		return align(RelativeLayout.LEFT_OF, anchor);
	}

	public static Void toRightOf(int anchor) {
		return align(RelativeLayout.RIGHT_OF, anchor);
	}

	public static Void toStartOf(int anchor) {
		return align(RelativeLayout.START_OF, anchor);
	}

	private final static class LayoutAlignFunc
			implements Anvil.AttrFunc<Map.Entry<Integer, Integer>> {
		private final static LayoutAlignFunc instance = new LayoutAlignFunc();
		public void apply(View v, Map.Entry<Integer, Integer> e,
				Map.Entry<Integer, Integer> old) {
			ViewGroup.LayoutParams p = v.getLayoutParams();
			if (p instanceof RelativeLayout.LayoutParams) {
				((RelativeLayout.LayoutParams) p).addRule(e.getKey(), e.getValue());
				v.setLayoutParams(p);
			}
		}
	}

	public static Void typeface(String font) {
		return attr(TypefaceFunc.instance, font);
	}

	private final static class TypefaceFunc implements Anvil.AttrFunc<String> {
		private final static TypefaceFunc instance = new TypefaceFunc();
		public void apply(View v, String font, String old) {
			if (v instanceof TextView) {
				((TextView) v).setTypeface(Typeface.createFromAsset(v.getContext().getAssets(), font));
			}
		}
	}

	public static Void visibility(boolean visible) {
		if (visible) {
			return trikita.anvil.DSL.visibility(View.VISIBLE);
		} else {
			return trikita.anvil.DSL.visibility(View.GONE);
		}
	}

	// -------------
	public static Void tag(int id, Object value) {
		return attr(TagFunc.instance,
				new AbstractMap.SimpleImmutableEntry<Integer, Object>(id, value));
	}
	private final static class TagFunc implements Anvil.AttrFunc<Map.Entry<Integer, Object>> {
		private final static TagFunc instance = new TagFunc();
		public void apply(View v, Map.Entry<Integer, Object> p, Map.Entry<Integer, Object> q) {
			v.setTag(p.getKey(), p.getValue());
		}
	}

	public static Void shadowLayer(float radius, float dx, float dy, int color) {
		List<Number> list = new ArrayList<>(4);
		list.add(radius);
		list.add(dx);
		list.add(dy);
		list.add(color);
		return attr(ShadowLayerFunc.instance, list);
	}
	private final static class ShadowLayerFunc implements Anvil.AttrFunc<List<Number>> {
		private final static ShadowLayerFunc instance = new ShadowLayerFunc();
		public void apply(View v, List<Number> arg, List<Number> old) {
			if (v instanceof TextView) {
				((TextView) v).setShadowLayer(arg.get(0).floatValue(),
						arg.get(1).floatValue(), arg.get(2).floatValue(),
						arg.get(3).intValue());
			}
		}
	}

	// Custom and simplified listeners
	private static Map<StringBuilder, String> hasUserInput =
		new WeakHashMap<StringBuilder, String>();

	public static Void text(final StringBuilder sb) {
		if (!sb.toString().equals(hasUserInput.get(sb))) {
			trikita.anvil.DSL.text(sb.toString());
			hasUserInput.put(sb, sb.toString());
		}
		onTextChanged(new TextWatcherProxy(sb) {
			@Override
			public void	afterTextChanged(Editable s) {
				boolean shouldRender = !s.toString().equals(hasUserInput.get(sb));
				sb.replace(0, sb.length(), s.toString());
				hasUserInput.put(sb, s.toString());
				if (shouldRender) {
					Anvil.render();
				}
			}
		});
		return null;
	}

	private static class TextWatcherProxy implements TextWatcher {
		private final Object object;
		public TextWatcherProxy(Object o) {
			this.object = o;
		}
		public int hashCode() { return object.hashCode(); }
		public boolean equals(Object o) {
			if (o == null || !o.getClass().equals(getClass())) {
				return false;
			}
			return ((TextWatcherProxy) o).equals(object);
		}
		public void	afterTextChanged(Editable s) {}
		public void	beforeTextChanged(CharSequence s, int from, int n, int after) {}
		public void	onTextChanged(CharSequence s, int from, int before, int n) {}
	}

	public static Void onTextChanged(TextWatcher w) {
		return attr(TextWatcherFunc.instance, w);
	}
	private final static class TextWatcherFunc implements Anvil.AttrFunc<TextWatcher> {
		private final static TextWatcherFunc instance = new TextWatcherFunc();
		public void apply(final View v, final TextWatcher w, TextWatcher old) {
			if (v instanceof TextView) {
				TextView tv = (TextView) v;
				if (old != null) {
					tv.removeTextChangedListener(old);
				}
				tv.addTextChangedListener(new TextWatcherProxy(w) {
					public void	afterTextChanged(Editable s) {
						w.afterTextChanged(s);
					}
					public void	beforeTextChanged(CharSequence s, int from, int n,
						int after) {
						w.beforeTextChanged(s, from, n, after);
					}
					public void	onTextChanged(CharSequence s,
						int from, int before, int n) {
						w.onTextChanged(s, from, before, n);
					}
				});
			}
		}
	}

	public interface SimpleItemSelectedListener {
		void onItemSelected(AdapterView a, View v, int pos, long id);
	}

	public static Void onItemSelected(SimpleItemSelectedListener l) {
		return attr(ItemSelectedFunc.instance, l);
	}
	private final static class ItemSelectedFunc
			implements Anvil.AttrFunc<SimpleItemSelectedListener> {
		private final static ItemSelectedFunc instance = new ItemSelectedFunc();
		public void apply(View v, final SimpleItemSelectedListener l,
				SimpleItemSelectedListener old) {
			AdapterView.OnItemSelectedListener wrapper =
				new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView a0, View a1, int a2, long a3) {
						l.onItemSelected(a0, a1, a2, a3);
						Anvil.render();
					}
					@Override
					public void onNothingSelected(AdapterView a0) {}
					@Override
					public int hashCode() { return l.hashCode(); }
					@Override
					public boolean equals(Object o) { return l.equals(o); }
				};
			if (v instanceof AdapterView) {
				((AdapterView) v).setOnItemSelectedListener(wrapper);
			} else if (v instanceof AutoCompleteTextView) {
				((AutoCompleteTextView) v).setOnItemSelectedListener(wrapper);
			}
		}
	}

	private final static class AnimatorPair {
		public Animator animator;
		public boolean trigger;
		public AnimatorPair(Animator a, boolean trigger) {
			this.animator = a;
			this.trigger = trigger;
		}
		public int hashCode() {
			return trigger ? 0 : 1;
		}
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			AnimatorPair pair = (AnimatorPair) o;
			if (pair.trigger != this.trigger) {
				if (pair.animator != null && pair.animator.isRunning()) {
					pair.animator.cancel();
				}
				return false;
			}
			this.animator = pair.animator;
			return true;
		}
	}

	private static class AnimatorFunc implements Anvil.AttrFunc<AnimatorPair> {
		private final static AnimatorFunc instance = new AnimatorFunc();
		public void apply(View v, AnimatorPair a, AnimatorPair b) {
			// If new animation is set to true and old one was false
			// then start new animation
			if (a.trigger) {
				a.animator.setTarget(v);
				a.animator.start();
			}
		}
	}

	public static Void anim(boolean trigger, Animator a) {
		return attr(AnimatorFunc.instance, new AnimatorPair(a, trigger));
	}

	protected static final class ViewClassResult {}

	public static ViewClassResult v(Class<? extends View> c) {
		Anvil.currentMount().push(c);
		return null;
	}

	private static Void end() {
		Anvil.currentMount().pop();
		return null;
	}

	public static Void x(ViewClassResult c, Object ...args) { return end(); }
	public static Void o(ViewClassResult c, Object ...args) { return end(); }

	public static Void v(Class<? extends View> c, Anvil.Renderable r) {
		v(c);
		r.view();
		return end();
	}

	public static <T> Void attr(Anvil.AttrFunc<T> f, T value) {
		Anvil.currentMount().attr(f, value);
		return null;
	}
}

