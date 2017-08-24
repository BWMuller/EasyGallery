package za.co.bwmuller.easygallery.ui.media;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import za.co.bwmuller.easygallery.R;
import za.co.bwmuller.easygallerycore.ui.RecyclerViewCursorAdapter;
import za.co.bwmuller.easygallerycore.data.loader.AlbumCallback;
import za.co.bwmuller.easygallerycore.model.Media;

public class MediaAdapter extends RecyclerViewCursorAdapter<MediaAdapter.ViewHolder> {

    private final AlbumCallback mListener;
    private final RecyclerView mRecyclerView;
    private final Drawable mPlaceholder;
    private final int mImageSize;

    public MediaAdapter(RecyclerView recyclerView, AlbumCallback listener) {
        super(null);
        this.mListener = listener;
        this.mRecyclerView = recyclerView;
        mPlaceholder = null;
        mImageSize = recyclerView.getResources().getDisplayMetrics().widthPixels / listener.getConfig().mediaGridColumnCount;
    }

    @Override protected void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.populate(Media.from(cursor));
//        mediaViewHolder.mMediaGrid.bindMedia(item);
//        mediaViewHolder.mMediaGrid.setOnMediaGridClickListener(this);
//        setCheckStatus(item, mediaViewHolder.mMediaGrid);
//
//
//        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);
//
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
    }

    @Override protected int getItemViewType(int position, Cursor cursor) {
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.media_list_item, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final SimpleDraweeView mSimpleDrawee;
//        final TextView mNameTextView;
//        final TextView mCountTextView;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mSimpleDrawee = (SimpleDraweeView) view.findViewById(R.id.media_item_image);
//            mNameTextView = (TextView) view.findViewById(R.id.album_item_name);
//            mCountTextView = (TextView) view.findViewById(R.id.album_item_count);
        }

        void populate(Media item) {
            mSimpleDrawee.setController(Fresco.newDraweeControllerBuilder()
                    .setOldController(mSimpleDrawee.getController())
                    .setLowResImageRequest(ImageRequestBuilder.newBuilderWithSource(item.getContentUri())
                            .setResizeOptions(new ResizeOptions(mImageSize, mImageSize))
                            .setRotationOptions(mListener.getConfig().autoRotateImages ? RotationOptions.autoRotate() : RotationOptions.forceRotation(RotationOptions.NO_ROTATION))
                            .setProgressiveRenderingEnabled(true)
                            .build())
                    .build());
        }
    }
}
