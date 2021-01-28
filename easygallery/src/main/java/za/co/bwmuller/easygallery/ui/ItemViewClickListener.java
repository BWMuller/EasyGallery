package za.co.bwmuller.easygallery.ui;

import android.view.View;

/**
 * Created by Bernhard Müller on 8/23/2017.
 */

public abstract class ItemViewClickListener<T> implements View.OnClickListener {

    T item;

    public ItemViewClickListener(T item) {
        this.item = item;
    }

    @Override
    public final void onClick(View v) {
        onClick(v, item);
    }

    public abstract void onClick(View v, T item);
}
