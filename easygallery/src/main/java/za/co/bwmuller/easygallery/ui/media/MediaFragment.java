package za.co.bwmuller.easygallery.ui.media;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import za.co.bwmuller.easygallerycore.Config;
import za.co.bwmuller.easygallery.R;
import za.co.bwmuller.easygallerycore.data.loader.AlbumCallback;
import za.co.bwmuller.easygallerycore.data.loader.AlbumMediaLoader;
import za.co.bwmuller.easygallerycore.model.Album;
import za.co.bwmuller.easygallerycore.model.Media;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnMediaFragmentListener}
 * interface.
 */
public class MediaFragment extends Fragment implements AlbumCallback {

    private static final String ARG_ALBUM = "arg_album";
    RecyclerView recyclerView;
    private OnMediaFragmentListener mListener;
    private AlbumMediaLoader albumMediaLoader;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MediaFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMediaFragmentListener) {
            mListener = (OnMediaFragmentListener) context;
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
        View view = inflater.inflate(R.layout.fragment_media_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, mListener.getConfig().mediaGridColumnCount));
            recyclerView.setAdapter(new MediaAdapter(recyclerView, this));
        }
        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        albumMediaLoader = new AlbumMediaLoader(getActivity(), this);
        albumMediaLoader.load((Album) getArguments().getParcelable(ARG_ALBUM));
    }

    @Override public void onDestroy() {
        super.onDestroy();
        if (albumMediaLoader != null)
            albumMediaLoader.onDestroy();
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

    public static MediaFragment newInstance(Album album) {
        MediaFragment fragment = new MediaFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ALBUM, album);
        fragment.setArguments(args);
        return fragment;
    }

    private MediaAdapter getAdapter() {
        return (MediaAdapter) recyclerView.getAdapter();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMediaFragmentListener {
        // TODO: Update argument type and name
        void onMediaSelected(Media item);

        Config getConfig();
    }
}
