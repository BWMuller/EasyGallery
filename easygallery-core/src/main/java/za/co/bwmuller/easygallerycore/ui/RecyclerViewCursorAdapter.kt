package za.co.bwmuller.easygallerycore.ui

import android.database.Cursor
import android.provider.MediaStore.Files.FileColumns
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class RecyclerViewCursorAdapter<VH : ViewHolder?>(c: Cursor?) : Adapter<VH>() {

    var cursor: Cursor? = null
        private set

    private var mRowIDColumn = 0
    protected abstract fun onBindViewHolder(holder: VH, cursor: Cursor?, position: Int)
    override fun onBindViewHolder(holder: VH, position: Int) {
        check(isDataValid(cursor)) { "Cannot bind view holder when cursor is in invalid state." }
        check(cursor?.moveToPosition(position) == true) {
            ("Could not move cursor to position " + position
                    + " when trying to bind view holder")
        }
        onBindViewHolder(holder, cursor, position)
    }

    override fun getItemViewType(position: Int): Int {
        check(cursor?.moveToPosition(position) == true) {
            ("Could not move cursor to position " + position
                    + " when trying to get item view type.")
        }
        return getItemViewType(position, cursor)
    }

    protected abstract fun getItemViewType(position: Int, cursor: Cursor?): Int
    override fun getItemCount(): Int {
        return if (isDataValid(cursor)) {
            cursor?.count ?: 0
        } else {
            0
        }
    }

    override fun getItemId(position: Int): Long {
        check(isDataValid(cursor)) { "Cannot lookup item id when cursor is in invalid state." }
        check(cursor?.moveToPosition(position) == true) {
            ("Could not move cursor to position " + position
                    + " when trying to get an item id")
        }
        return cursor?.getLong(mRowIDColumn) ?: 0
    }

    fun swapCursor(newCursor: Cursor?) {
        if (newCursor === cursor) {
            return
        }
        if (newCursor != null) {
            cursor = newCursor
            mRowIDColumn = cursor?.getColumnIndexOrThrow(FileColumns._ID) ?: -1
            // notify the observers about the new cursor
            notifyDataSetChanged()
        } else {
            notifyItemRangeRemoved(0, itemCount)
            cursor = null
            mRowIDColumn = -1
        }
    }

    private fun isDataValid(cursor: Cursor?): Boolean {
        return cursor != null && !cursor.isClosed
    }

    init {
        setHasStableIds(true)
        swapCursor(c)
    }
}