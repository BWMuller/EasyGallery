package za.co.bwmuller.easygallery.ui.album;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import za.co.bwmuller.easygallerycore.Config;
import za.co.bwmuller.easygallery.R;
import za.co.bwmuller.easygallery.ui.ItemViewClickListener;
import za.co.bwmuller.easygallerycore.ui.RecyclerViewCursorAdapter;
import za.co.bwmuller.easygallerycore.model.Album;

public class AlbumAdapter extends RecyclerViewCursorAdapter<AlbumAdapter.ViewHolder> {

    private final AlbumFragment.OnAlbumFragmentListener mListener;
    private final RecyclerView mRecyclerView;
    private final Drawable mPlaceholder;
    private final int mImageSize;

    public AlbumAdapter(RecyclerView recyclerView, AlbumFragment.OnAlbumFragmentListener listener) {
        super(null);
        this.mListener = listener;
        this.mRecyclerView = recyclerView;
        mPlaceholder = null;
        mImageSize = recyclerView.getResources().getDisplayMetrics().widthPixels / listener.getConfig().albumGridColumnCount;
    }

    @Override protected void onBindViewHolder(ViewHolder holder, Cursor cursor, int position) {
        Album item = Album.from(cursor);
        holder.populate(item, mListener.getConfig());
        holder.mView.setOnClickListener(new ItemViewClickListener<Album>(item) {
            @Override public void onClick(View v, Album item) {
                mListener.onAlbumSelected(item);
            }
        });
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
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_list_item, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final SimpleDraweeView mSimpleDrawee;
        final TextView mNameTextView;
        final TextView mCountTextView;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mSimpleDrawee = (SimpleDraweeView) view.findViewById(R.id.album_item_image);
            mNameTextView = (TextView) view.findViewById(R.id.album_item_name);
            mCountTextView = (TextView) view.findViewById(R.id.album_item_count);
        }

        void populate(Album item, Config config) {
            mNameTextView.setText(item.getDisplayName(config));
            mCountTextView.setText(String.valueOf(item.getCount()));

            mSimpleDrawee.setController(Fresco.newDraweeControllerBuilder()
                    .setOldController(mSimpleDrawee.getController())
                    .setLowResImageRequest(ImageRequestBuilder.newBuilderWithSource(item.getCoverUri())
                            .setResizeOptions(new ResizeOptions(mImageSize, mImageSize))
                            .setRotationOptions(mListener.getConfig().autoRotateImages ? RotationOptions.autoRotate() : RotationOptions.forceRotation(RotationOptions.NO_ROTATION))
                            .setProgressiveRenderingEnabled(true)
                            .build())
                    .build());
        }
    }
}
