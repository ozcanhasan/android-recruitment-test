package com.algoritma.recruitmenttest;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.algoritma.recruitmenttest.db.DbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getSimpleName();
    private Socket socket;
    private DbHelper dbHelper;
    private RecyclerView rvCurrency;
    private StockAdapter stockAdapter;
    private LottieAnimationView lavLoading;
    private static final String MESSAGE_RECEIVED = "message";

    public MainFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        rvCurrency = rootView.findViewById(R.id.rvCurrency);
        lavLoading = rootView.findViewById(R.id.lavLoading);

        setRecyclerView();

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DbHelper(getActivity().getApplicationContext());

        try {
            socket = IO.socket("https://q.investaz.net:3000");
        } catch (URISyntaxException e) {
            Log.e(TAG, "onCreate: ", e);
        }

        socket.on(Socket.EVENT_CONNECT, onConnected);
        socket.on(Socket.EVENT_CONNECT_ERROR, onError);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnected);
        socket.on(MESSAGE_RECEIVED, onMessageReceived);

        socket.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        socket.disconnect();

    }

    private Emitter.Listener onConnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "Connected");
        }
    };

    private Emitter.Listener onDisconnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "Disconnected");
        }
    };

    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "Error");
        }
    };

    private Emitter.Listener onMessageReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lavLoading.setVisibility(View.VISIBLE);

                    JSONObject data = (JSONObject) args[0];
                    try {
                        JSONArray jsonArray = data.getJSONArray("result");

                        dbHelper.insertData(jsonArray);

                        refreshUI();
                    } catch (JSONException e) {
                        Log.e(TAG, "run: ", e);
                    }
                }
            });
        }
    };

    private void setRecyclerView() {
        rvCurrency.setNestedScrollingEnabled(false);
        rvCurrency.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        rvCurrency.smoothScrollToPosition(0);

        stockAdapter = new StockAdapter(getActivity().getApplicationContext(), dbHelper.getStockList());
        rvCurrency.setAdapter(stockAdapter);
    }

    private void refreshUI() {
        stockAdapter.setData(dbHelper.getStockList());
        lavLoading.setVisibility(View.GONE);
    }
}
