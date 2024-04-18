package com.example.applayout.core.exam.listening;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applayout.R;

import java.util.ArrayList;
import java.util.List;

public class SentenceAdapter extends RecyclerView.Adapter<SentenceAdapter.SentenceViewHolder> {
    private List<Sentence> mListSentence;
    private static final int VIEW_TYPE_EVEN = 0;
    private static final int VIEW_TYPE_ODD = 1;

    public SentenceAdapter(List<Sentence> mListSentence) {
        this.mListSentence = mListSentence;
    }

    @NonNull
    @Override
    public SentenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = viewType == VIEW_TYPE_EVEN ? R.layout.exam_listening_item_sentence1 : R.layout.exam_listening_item_sentence2;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        SentenceViewHolder viewHolder = new SentenceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SentenceViewHolder holder, int position) {
        Sentence sentence = mListSentence.get(position);
        if (sentence == null) {
            return;
        }
        if(sentence.getStatus()==0){
            holder.edt.setText(sentence.getContext());
            holder.edt.setFocusable(false);
        }
        else{
            holder.edt.setText("");
            holder.edt.setFocusable(true);
        }
    }

    @Override
    public int getItemCount() {
        return mListSentence != null ? mListSentence.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? VIEW_TYPE_EVEN : VIEW_TYPE_ODD;
    }

    public class SentenceViewHolder extends RecyclerView.ViewHolder {
        private EditText edt;

        public SentenceViewHolder(@NonNull View itemView) {
            super(itemView);
            edt = itemView.findViewById(R.id.edt_a);
        }
    }

    public void setSentenceList(List<Sentence> listSentence) {
        mListSentence = listSentence;
        notifyDataSetChanged();
    }

}
