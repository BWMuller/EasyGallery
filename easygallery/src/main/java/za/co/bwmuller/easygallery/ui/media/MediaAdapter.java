package za.co.bwmuller.easygallery.ui.media;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import za.co.bwmuller.easygallery.R;
import za.co.bwmuller.easygallerycore.data.loader.AlbumCallback;
import za.co.bwmuller.easygallerycore.model.Media;
import za.co.bwmuller.easygallerycore.ui.RecyclerViewCursorAdapter;

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

    @Override
    protected void onBindViewHolder(ViewHolder holder, Cursor cursor, int position) {
        holder.populate(Media.from(cursor));
    }

    @Override
    protected int getItemViewType(int position, Cursor cursor) {
        return position;
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

        Media mItem;

        void populate(Media item) {
            this.mItem = item;
            mSimpleDrawee.setController(Fresco.newDraweeControllerBuilder()
                    .setOldController(mSimpleDrawee.getController())
                    .setLowResImageRequest(ImageRequestBuilder.newBuilderWithSource(item.getContentUri())
                            .setResizeOptions(new ResizeOptions(mImageSize, mImageSize))
                            .setRotationOptions(mListener.getConfig().autoRotateImages ? RotationOptions.autoRotate() : RotationOptions.forceRotation(RotationOptions.NO_ROTATION))
                            .setProgressiveRenderingEnabled(true)
                            .build())
                    .build());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), MimeTypeMap.getSingleton().getExtensionFromMimeType(v.getContext().getContentResolver().getType(mItem.getContentUri())), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
