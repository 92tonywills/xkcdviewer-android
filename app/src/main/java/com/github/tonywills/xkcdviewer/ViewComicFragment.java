package com.github.tonywills.xkcdviewer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.tonywills.xkcdviewer.api.XkcdService;
import com.github.tonywills.xkcdviewer.api.model.Comic;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewComicFragment extends Fragment {

    @BindView(R.id.image_view) ImageView imageView;
    private ComicViewerListener listener;

    public static ViewComicFragment newInstance() {
        ViewComicFragment fragment = new ViewComicFragment();
        Bundle args = new Bundle();
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

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        return inflater.inflate(R.layout.fragment_view_comic, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());
        XkcdService.instance.getLatestComic(new XkcdService.ComicCallback() {
            @Override public void complete(@Nullable Comic comic) {
                if (comic != null) {
                    listener.comicLoaded(comic);
                    imageView.setContentDescription(comic.getAlt());
                    Picasso.with(getActivity())
                            .load(comic.getImg())
                            .into(imageView);
                }
            }
        });
    }

    public interface ComicViewerListener {
        void comicLoaded(Comic comic);
        void setComicFavourite(boolean favourite, Comic comic);
    }

}
