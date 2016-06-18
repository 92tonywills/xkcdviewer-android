package com.github.tonywills.xkcdviewer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.tonywills.xkcdviewer.api.XkcdService;
import com.github.tonywills.xkcdviewer.api.model.Comic;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewComicFragment extends Fragment {

    private static final String TAG = "ViewComicFragment";

    public static final int MODE_RANDOM = 0x0457;
    public static final int MODE_LATEST = 0x1745;

    private static final String ARG_MODE = "mode";

    @BindView(R.id.image_view) ImageView imageView;
    private ComicViewerListener listener;
    private Comic comic;
    private int mode;

    public static ViewComicFragment newInstance(int mode) {
        ViewComicFragment fragment = new ViewComicFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    public ViewComicFragment() {
        // Required empty public constructor
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        attachListener(context);
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        attachListener(activity);
    }

    private void attachListener(Context context) {
        listener = (ComicViewerListener) context;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = getArguments().getInt(ARG_MODE, 0);
        setHasOptionsMenu(true);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        return inflater.inflate(R.layout.fragment_view_comic, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());
        loadComic();
    }

    private void loadComic() {
        switch (mode) {
            case MODE_LATEST:
                listener.setTitleFromComicViewer("Latest");
                XkcdService.getInstance(getContext()).getLatestComic(comicCallback);
                break;
            case MODE_RANDOM:
                listener.setTitleFromComicViewer("Random Comic");
                XkcdService.getInstance(getContext()).getRandomComic(comicCallback);
                break;

            default:
                Log.w(TAG, "onViewCreated: No mode selected", new Exception("No mode set"));
        }
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.comic, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share: return true;
            case R.id.action_star:
                XkcdService.getInstance(getContext()).setComicFavourite(comic, !comic.isFavourite());
                item.setIcon(comic.isFavourite() ? R.drawable.ic_star_filled : R.drawable.ic_star_outline);
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    private XkcdService.ComicCallback comicCallback = new XkcdService.ComicCallback() {
        @Override public void complete(@Nullable Comic comic) {
            if (comic != null) {
                ViewComicFragment.this.comic = comic;
                listener.setTitleFromComicViewer(comic.getTitle());
                imageView.setContentDescription(comic.getAlt());
                Context context = getContext();
                if (context != null) {
                    Picasso.with(context)
                            .load(comic.getImg())
                            .into(imageView);
                }
            }
        }
    };

    public interface ComicViewerListener {
        void setTitleFromComicViewer(String title);
    }

}
