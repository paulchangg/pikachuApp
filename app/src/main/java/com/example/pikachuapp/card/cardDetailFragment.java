package com.example.pikachuapp.card;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.pikachuapp.Common;
import com.example.pikachuapp.R;
import com.example.pikachuapp.task.ImageTask;


public class cardDetailFragment extends Fragment {
    private final static String TAG = "TAG_CardDetailFragment";
    private FragmentActivity activity;
    private ImageView ivCardD;
    private TextView tvNameD,tvAnnlFeeD,tvFcbD,tvDcbD;
    private Card card;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivCardD = view.findViewById(R.id.ivCardD);
        tvNameD = view.findViewById(R.id.tvNameD);
        tvAnnlFeeD = view.findViewById(R.id.tvAnnlFeeD);
        tvFcbD = view.findViewById(R.id.tvFcbD);
        tvDcbD = view.findViewById(R.id.tvDcbD);
        final NavController navController = Navigation.findNavController(view);
        Bundle bundle = getArguments();

        if (bundle == null || bundle.getSerializable("card") == null) {
            Common.showToast(activity, R.string.textNoCardsFound);
            navController.popBackStack();
            return;
        }
        card = (Card) bundle.getSerializable("card");
        showCard();
    }

    private void showCard() {
        String url = Common.URL_SERVER + "/cards/getImageAndroid";
        int id = card.getC_id();
        int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
        Bitmap bitmap = null;
        try {
            bitmap = new ImageTask(url, id, imageSize).execute().get();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (bitmap != null) {
            ivCardD.setImageBitmap(bitmap);
        } else {
            ivCardD.setImageResource(R.drawable.no_image);
        }

        tvNameD.setText(card.getC_name());
        tvAnnlFeeD.setText(card.getAnnlfee());
        tvFcbD.setText(String.valueOf(card.getFcb()));
        tvDcbD.setText(String.valueOf(card.getDcb()));
    }
}
