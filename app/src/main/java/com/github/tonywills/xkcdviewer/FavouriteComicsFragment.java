package com.github.tonywills.xkcdviewer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.tonywills.xkcdviewer.api.XkcdService;
import com.github.tonywills.xkcdviewer.api.model.Comic;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteComicsFragment extends Fragment {

    private static final String TAG = "FavouriteComicsFragment";

    @BindView(R.id.favourites_list) RecyclerView favouritesList;
    @BindView(R.id.no_favourites_error_text) TextView errorView;
    private FavouriteComicsFragmentListener listener;
    private FavouriteComicAdapter adapter;
    private ArrayList<Comic> comics;

    public static FavouriteComicsFragment newInstance() {
        FavouriteComicsFragment fragment = new FavouriteComicsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FavouriteComicsFragment() {
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
        listener = (FavouriteComicsFragmentListener) context;
        listener.setTitleFromFavouritesFragment("Favourites");
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comics = new ArrayList<>();
        adapter = new FavouriteComicAdapter(getContext(), comics);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        return inflater.inflate(R.layout.fragment_favourite_comics, container, false);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());
        favouritesList.setAdapter(adapter);
        favouritesList.setLayoutManager(new LinearLayoutManager(getContext()));
        comics.clear();
        comics.addAll(XkcdService.getInstance(getContext()).getFavouriteComics());
        adapter.notifyDataSetChanged();
        errorView.setVisibility(comics.isEmpty() ? View.VISIBLE : View.GONE);
    }

    public interface FavouriteComicsFragmentListener {
        void setTitleFromFavouritesFragment(String title);
        void didSelectComic(Comic comic);
    }

    private class FavouriteComicAdapter extends RecyclerView.Adapter<FavouriteComicPreviewHolder> {

        private final Context context;
        private final List<Comic> comics;

        private FavouriteComicAdapter(Context context, List<Comic> comics) {
            this.context = context;
            this.comics = comics;
        }

        @Override public FavouriteComicPreviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FavouriteComicPreviewHolder(
                    LayoutInflater.from(context)
                            .inflate(R.layout.view_favourite_comic_preview, parent, false));
        }

        @Override public void onBindViewHolder(final FavouriteComicPreviewHolder holder, int position) {
            holder.bindData(comics.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.didSelectComic(comics.get(holder.getAdapterPosition()));
                }
            });
        }

        @Override public int getItemCount() {
            return comics.size();
        }
    }

    private static class FavouriteComicPreviewHolder extends RecyclerView.ViewHolder {

        public FavouriteComicPreviewHolder(View itemView) {
            super(itemView);
        }

        private void bindData(Comic comic) {
            ((TextView) itemView).setText(comic.getTitle());
        }

    }
}
