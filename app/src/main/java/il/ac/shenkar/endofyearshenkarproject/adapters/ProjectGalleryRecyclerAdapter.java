package il.ac.shenkar.endofyearshenkarproject.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import il.ac.shenkar.endofyearshenkarproject.R;
import il.ac.shenkar.endofyearshenkarproject.utils.DownloadImageTask;

public class ProjectGalleryRecyclerAdapter extends RecyclerView.Adapter<ProjectGalleryRecyclerAdapter.CustomViewHolder> {
    private final ImageView mMainImage;
    private List<String> mImages;
    private Context mContext;
    private String currentMainImageUrl;

    public ProjectGalleryRecyclerAdapter(Context context, ImageView mainImage, List<String> mImages) {
        this.mImages = mImages;
        this.mContext = context;
        this.mMainImage = mainImage;
    }

    public String getCurrentMainImageUrl()
    {
        return currentMainImageUrl;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.project_tumb, viewGroup, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        customViewHolder.imgUrl = mImages.get(i);
        new DownloadImageTask(customViewHolder.imgTumb).execute(mImages.get(i));
    }

    @Override
    public int getItemCount() {
        return (null != mImages ? mImages.size() : 0);
    }

    public void refresh(final List<String> images) {
        boolean isNew = false;

        for (String imageUrl : images) {
            if (!mImages.contains(imageUrl)) {
                isNew = true;
            }
        }

        if (isNew && !images.isEmpty()) {
            new DownloadImageTask(mMainImage).execute(images.get(0));
            currentMainImageUrl = images.get(0);
            mImages = images;
        }

        notifyDataSetChanged();

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView imgTumb;
        protected String imgUrl;

        public CustomViewHolder(View view) {
            super(view);
            this.imgTumb = (ImageView) view.findViewById(R.id.imgTumb);
            imgTumb.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ImageView image = (ImageView) v;
            BitmapDrawable drawableBitmap = (BitmapDrawable) image.getDrawable();

            if (drawableBitmap != null) {
                Bitmap drawable = drawableBitmap.getBitmap();
                mMainImage.setImageBitmap(drawable);
            }
            ProjectGalleryRecyclerAdapter.this.currentMainImageUrl = imgUrl;
        }
    }
}