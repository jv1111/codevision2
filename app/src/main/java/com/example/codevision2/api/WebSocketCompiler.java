package com.example.codevision2.api;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.codevision2.Constant;
import com.example.codevision2.ENV;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketCompiler {

    public static WebSocket webSocket;
    public static OkHttpClient client = new OkHttpClient();

    public interface ICompiler{
        void onCodeRunOutput(String output);
    }

    public static void connectWebSocket(Activity activity, ICompiler cb) {
        Request request = new Request.Builder().url("ws://"+ ENV.COMPILER_API_IP +"/").build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                activity.runOnUiThread(()->{
                    Toast.makeText(activity, "WebSocket Connected", Toast.LENGTH_SHORT).show();
                    Log.i("myTag WebSocket", "web socket connected");
                });
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                activity.runOnUiThread(()->{
                    cb.onCodeRunOutput(text);
                });
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                activity.runOnUiThread(() ->
                        Toast.makeText(activity, "WebSocket Closed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                activity.runOnUiThread(() ->
                        Toast.makeText(activity, "WebSocket Error: " + t.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}
