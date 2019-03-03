package com.my3w.farm.activity.circle.adapter;

import java.util.ArrayList;

import com.my3w.farm.R;
import com.my3w.farm.activity.circle.entity.CircleListCommentListEntity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CircleAdapterNoScrollListAdapter extends BaseAdapter {

	private ArrayList<CircleListCommentListEntity> data;
	private LayoutInflater mInflater;

	private onclickItemListener listener;

	public interface onclickItemListener {
		public void clickItem(int index);

		public void clickUser(int uid);
	}

	CircleAdapterNoScrollListAdapter(Context context, ArrayList<CircleListCommentListEntity> data) {
		this.mInflater = LayoutInflater.from(context);
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final CircleListCommentListEntity getData = data.get(position);
		if (getData != null) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.activity_circle_list_content_view_comment_list_view, null);
				holder.comment = (TextView) convertView.findViewById(R.id.comment);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			String htmls = "";
			if (getData.getUsertoid() == 0) {
				htmls = "<a href=\"" + getData.getUserid() + "\">" + getData.getUser() + "</a>" + "：" + getData.getContent();
			} else {
				htmls = "<a href=\"" + getData.getUserid() + "\">" + getData.getUser() + "</a>" + "：回复" + "<a href=\""
						+ getData.getUsertoid() + "\"> " + getData.getUserto() + "</a>" + getData.getContent();
			}
			holder.comment.setText(Html.fromHtml(htmls));
			holder.comment.setMovementMethod(LinkMovementMethod.getInstance());
			CharSequence text = holder.comment.getText();
			if (text instanceof Spannable) {
				int end = text.length();
				Spannable sp = (Spannable) holder.comment.getText();
				URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
				SpannableStringBuilder style = new SpannableStringBuilder(text);
				style.clearSpans();// should clear old spans
				for (URLSpan url : urls) {
					style.setSpan(new LinkURLSpan(url.getURL()), sp.getSpanStart(url), sp.getSpanEnd(url),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				holder.comment.setText(style);
			}

			holder.comment.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (listener != null)
						listener.clickItem(position);
				}
			});
		}
		return convertView;
	}

	public void setOnClickItemListener(onclickItemListener listener) {
		this.listener = listener;
	}

	private class LinkURLSpan extends ClickableSpan {

		private String mUrl;

		LinkURLSpan(String url) {
			mUrl = url;
		}

		@Override
		public void onClick(View widget) {
			if (listener != null)
				listener.clickUser(Integer.parseInt(mUrl));
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(Color.parseColor("#ff58bf42"));
			ds.setFakeBoldText(true);
			ds.setUnderlineText(false);
		}
	}

	public class ViewHolder {
		private TextView comment;
	}

}
