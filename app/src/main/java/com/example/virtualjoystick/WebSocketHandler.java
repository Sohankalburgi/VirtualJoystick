package com.example.virtualjoystick; // Use your app's package name

import android.util.Log;

import androidx.annotation.NonNull; // For @NonNull annotation
import androidx.annotation.Nullable; // For @Nullable annotation

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketHandler extends WebSocketListener {

    private static final String TAG = "MyWebSocketListener";
    private final WebSocketEventCallback eventCallback;

    // Interface to send events back to the calling class (e.g., MainActivity)
    public interface WebSocketEventCallback {
        void onWebSocketEvent(String eventText);
    }

    public WebSocketHandler(WebSocketEventCallback callback) {
        this.eventCallback = callback;
    }

    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
        super.onOpen(webSocket, response);
        String openMsg = "WebSocket Connection Opened: " + response.message();
        Log.d(TAG, openMsg);
        if (eventCallback != null) {
            eventCallback.onWebSocketEvent("STATUS: Connected to server");
        }
        // You can send an initial message upon connection if needed
        // webSocket.send("Client connected (Java)");
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
        super.onMessage(webSocket, text);
        Log.d(TAG, "Receiving: " + text);
        if (eventCallback != null) {
            eventCallback.onWebSocketEvent("MESSAGE: " + text);
        }
        // Handle incoming text messages from the server
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
        Log.d(TAG, "Receiving bytes: " + bytes.hex());
        if (eventCallback != null) {
            eventCallback.onWebSocketEvent("MESSAGE_BYTES: " + bytes.hex());
        }
        // Handle incoming binary messages from the server
    }

    @Override
    public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        super.onClosing(webSocket, code, reason);
        Log.d(TAG, "Closing: " + code + " / " + reason);
        if (eventCallback != null) {
            eventCallback.onWebSocketEvent("STATUS: Closing connection - " + code + " / " + reason);
        }
        // Optionally, try to close gracefully, though the server usually initiates this
        // webSocket.close(1000, null);
    }

    @Override
    public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        super.onClosed(webSocket, code, reason);
        Log.d(TAG, "Closed: " + code + " / " + reason);
        if (eventCallback != null) {
            eventCallback.onWebSocketEvent("STATUS: Connection Closed - " + code + " / " + reason);
        }
    }

    @Override
    public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        Log.e(TAG, "Failure: " + t.getMessage(), t);
        String errorMsgDetail = (response != null) ? ("HTTP " + response.code() + " " + response.message()) : t.getMessage();
        if (eventCallback != null) {
            eventCallback.onWebSocketEvent("ERROR: Connection Failed - " + errorMsgDetail);
        }
    }
}
