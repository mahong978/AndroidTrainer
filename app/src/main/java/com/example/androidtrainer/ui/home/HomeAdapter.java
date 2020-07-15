package com.example.androidtrainer.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtrainer.R;

import java.util.ArrayList;
import java.util.List;

import io.doist.recyclerviewext.sticky_headers.StickyHeaders;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaders, StickyHeaders.ViewSetup {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_CONTENT = 1;

    private final float stickyHeaderElevation;
    private final List<HomeViewModel.Question> questionList = new ArrayList<>();
    private final List<Data> items = new ArrayList<>();

    private HeaderViewHolder.Callback headerCallback = new HeaderViewHolder.Callback() {
        @Override
        public void onEvent(int position) {
            HeaderData data = (HeaderData) items.get(position);
            if (!data.expand) {
                items.add(position + 1, new Data(questionList.get(data.index).a));
                notifyItemInserted(position + 1);
            } else {
                items.remove(position + 1);
                notifyItemRemoved(position + 1);
            }
            data.expand = !data.expand;
        }
    };

    public HomeAdapter(Context context) {
        stickyHeaderElevation = context.getResources().getDimension(R.dimen.sticky_header_elevation);
    }

    void setItems(List<HomeViewModel.Question> items) {
        this.questionList.clear();
        this.questionList.addAll(items);
        this.items.clear();
        for (int i = 0; i < items.size(); i++) {
            HomeViewModel.Question item = items.get(i);
            this.items.add(new HomeAdapter.HeaderData(i, item.q));
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return HeaderViewHolder.newInstance(parent, headerCallback);
            case VIEW_TYPE_CONTENT:
                return ContentViewHolder.newInstance(parent);
        }
        return ContentViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Data data = items.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(data);
                break;
            case VIEW_TYPE_CONTENT:
                ((ContentViewHolder) holder).bind(data);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof HeaderData ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public boolean isStickyHeader(int position) {
        return getItemViewType(position) == VIEW_TYPE_HEADER;
    }

    @Override
    public void setupStickyHeaderView(View view) {
        view.setTranslationZ(stickyHeaderElevation);
    }

    @Override
    public void teardownStickyHeaderView(View view) {
        view.setTranslationZ(0);
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Callback callback;

        private HeaderViewHolder(@NonNull View itemView, final Callback callback) {
            super(itemView);

            this.callback = callback;
            textView = itemView.findViewById(android.R.id.text1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onEvent(getAdapterPosition());
                }
            });
        }

        static HeaderViewHolder newInstance(ViewGroup viewParent, Callback callback) {
            View view = LayoutInflater.from(viewParent.getContext())
                    .inflate(R.layout.question_item, viewParent, false);
            return new HeaderViewHolder(view, callback);
        }

        void bind(Data data) {
            textView.setText(data.content);
        }

        interface Callback {
            void onEvent(int position);
        }
    }

    private static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        private ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        static ContentViewHolder newInstance(ViewGroup viewParent) {
            View view = LayoutInflater.from(viewParent.getContext())
                    .inflate(R.layout.question_answer_item, viewParent, false);
            return new ContentViewHolder(view);
        }

        void bind(Data data) {
            textView.setText(data.content);
        }
    }

    private static class Data {
        final String content;

        public Data(String content) {
            this.content = content;
        }
    }

    private static class HeaderData extends Data {
        int index;
        boolean expand;
//        boolean mark;

        public HeaderData(int index, String content) {
            super(content);
            this.index = index;
        }
    }
}
