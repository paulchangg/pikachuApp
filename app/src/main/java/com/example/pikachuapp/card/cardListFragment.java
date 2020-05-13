package com.example.pikachuapp.card;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pikachuapp.Common;
import com.example.pikachuapp.R;
import com.example.pikachuapp.task.CardTask;
import com.example.pikachuapp.task.ImgTask;
import com.example.pikachuapp.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class cardListFragment extends Fragment {
    private static final String TAG = "TAG_SpotListFragment";
    private Activity activity;
    private RecyclerView rvCard;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CardTask cardGetAllTask;
    private ImageTask cardImageTask;
    private List<Card> cards;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchView searchView = view.findViewById(R.id.searchView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        rvCard = view.findViewById(R.id.rvCard);

        rvCard.setLayoutManager(new LinearLayoutManager(activity));
        cards = getCards();
        showCards(cards);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showCards(cards);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // 如果搜尋條件為空字串，就顯示原始資料；否則就顯示搜尋後結果
                if (newText.isEmpty()) {
                    showCards(cards);
                } else {
                    List<Card> searchCards = new ArrayList<>();
                    // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
                    for (Card card : cards) {
                        if (card.getC_name().toUpperCase().contains(newText.toUpperCase())) {
                            searchCards.add(card);
                        }
                    }
                    showCards(searchCards);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });



    }

    private List<Card> getCards(){
        List<Card> cardList = new ArrayList<>();
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "cards/getCardAndroid";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "getAll");
//            String jsonOut = jsonObject.toString();
//            cardGetAllTask = new CommonTask(url, jsonOut);
            cardGetAllTask = new CardTask(url);
            try {
                String jsonIn = cardGetAllTask.execute().get();
                Type listType = new TypeToken<List<Card>>() {
                }.getType();
                cardList = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }

        return cardList;
    }

    private void showCards(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            Common.showToast(activity, R.string.textNoCardsFound);
        }
        CardAdapter cardAdapter = (CardAdapter) rvCard.getAdapter();
        // 如果spotAdapter不存在就建立新的，否則續用舊有的
        if (cardAdapter == null) {
            rvCard.setAdapter(new CardAdapter(activity, cards));
        } else {
            cardAdapter.setCards(cards);
            cardAdapter.notifyDataSetChanged();
        }
    }

    private class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Card> cards;
        private int imageSize;

        CardAdapter(Context context, List<Card> cards) {
            layoutInflater = LayoutInflater.from(context);
            this.cards = cards;
            /* 螢幕寬度除以4當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        void setCards(List<Card> cards) {
            this.cards = cards;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivCard;
            TextView tvCardName;

            MyViewHolder(View itemView) {
                super(itemView);
                ivCard = itemView.findViewById(R.id.ivCard);
                tvCardName = itemView.findViewById(R.id.tvCardName);
            }
        }

        @Override
        public int getItemCount() {
            return cards == null ? 0 : cards.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_card, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final Card card = cards.get(position);
            int id = card.getC_id();
            String url = Common.URL_SERVER + "/cards/getImageAndroid";
            cardImageTask = new ImageTask(url, id, imageSize, myViewHolder.ivCard);
            cardImageTask.execute();
            myViewHolder.tvCardName.setText(card.getC_name());
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("card", card);
                    Navigation.findNavController(view)
                            .navigate(R.id.action_cardListFragment_to_cardDetailFragment, bundle);
                }
            });
        }

    }
    @Override
    public void onStop() {
        super.onStop();
        if (cardGetAllTask != null) {
            cardGetAllTask.cancel(true);
            cardGetAllTask = null;
        }

        if (cardImageTask != null) {
            cardImageTask.cancel(true);
            cardImageTask = null;
        }

    }


}
