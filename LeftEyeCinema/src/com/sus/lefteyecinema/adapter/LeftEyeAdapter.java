package com.sus.lefteyecinema.adapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sus.lefteyecinema.R;
import com.sus.lefteyecinema.bean.LeftEyeItem;
import com.sus.lefteyecinema.bean.LeftEyeItems;
import com.sus.lefteyecinema.utils.DisplayImageOptionsUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import java.util.ArrayList;

public class LeftEyeAdapter extends BaseAdapter {
    
    private DisplayImageOptions options;
    private LayoutInflater mInflater;
    private ArrayList<LeftEyeItem> items;
    private Context context;
    private int mNumColumns;
    
    private int mImageWidth;

    private double imgWidthParam = 0.5625;

    private int mItemHeight;
    private RelativeLayout.LayoutParams mImageViewLayoutParams;
    

    public LeftEyeAdapter(Context context) {
        super();
        this.context =context;
        mInflater = LayoutInflater.from(context);
        options = DisplayImageOptionsUtil.getOptions(R.drawable.video_bg_hor);
        items = new ArrayList<LeftEyeItem>();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.left_eye_cinema_item, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.web_normal_view_item_img);
          
            holder.title = (TextView) convertView.findViewById(R.id.web_normal_view_item_title);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder.image.getLayoutParams().width != mImageWidth) {
            holder.image.setLayoutParams(mImageViewLayoutParams);
        }
            ImageLoader.getInstance().displayImage(items.get(position).getCover_url(),
                    holder.image, options);

        holder.title.setText(items.get(position).getTitle());
        return convertView;
    }
    
    public void setNumColumns(int numColumns) {
        mNumColumns = numColumns;
    }

    public int getNumColumns() {
        return mNumColumns;
    }
    
    /**
     * Sets the item height. Useful for when we know the column width so the
     * height can be set to match.
     *
     * @param height
     */
    public void setItemHeight(int width) {
        mImageWidth = width;
        mItemHeight = (int) (mImageWidth * imgWidthParam);
        mImageViewLayoutParams = new RelativeLayout.LayoutParams(mImageWidth, mItemHeight);
        notifyDataSetChanged();
    }
    
    public double getImgWidthParam() {
        return imgWidthParam;
    }

    public void setImgWidthParam(double imgWidthParam) {
        this.imgWidthParam = imgWidthParam;
    }
    
    static class ViewHolder {
        ImageView image;

        TextView title;

    }

    public void setShowItems(LeftEyeItems items) {
        this.items =items.getItems();
        notifyDataSetChanged();
        
    }

}
