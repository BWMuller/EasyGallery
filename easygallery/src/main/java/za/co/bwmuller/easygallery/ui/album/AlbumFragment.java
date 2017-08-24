package za.co.bwmuller.easygallery.ui.album;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import za.co.bwmuller.easygallery.Config;
import za.co.bwmuller.easygallery.R;
import za.co.bwmuller.easygallery.data.loader.AlbumCallback;
import za.co.bwmuller.easygallery.data.loader.AlbumLoader;
import za.co.bwmuller.easygallery.model.Album;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnAlbumFragmentListener}
 * interface.
 */
public class AlbumFragment extends Fragment implements AlbumCallback {

    RecyclerView recyclerView;
    AlbumLoader albumLoader;
    private OnAlbumFragmentListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AlbumFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAlbumFragmentListener) {
            mListener = (OnAlbumFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, mListener.getConfig().albumGridColumnCount));
            AlbumAdapter adapter = new AlbumAdapter(recyclerView, mListener);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        albumLoader = new AlbumLoader();
        albumLoader.onCreate(getActivity(), this);
        albumLoader.loadAlbums();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        if (albumLoader != null)
            albumLoader.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override public void onCursorLoad(Cursor cursor) {
        getAdapter().swapCursor(cursor);
    }

    @Override public void onCursorReset() {
        getAdapter().swapCursor(null);
    }

    @Override public Config getConfig() {
        return mListener.getConfig();
    }

    public static AlbumFragment newInstance() {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private AlbumAdapter getAdapter() {
        return (AlbumAdapter) recyclerView.getAdapter();
    }

    public interface OnAlbumFragmentListener {

        Config getConfig();

        void onAlbumSelected(Album item);
    }
}
