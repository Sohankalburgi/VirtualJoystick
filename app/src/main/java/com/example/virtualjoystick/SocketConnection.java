package com.example.virtualjoystick;
import android.util.Log;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class SocketConnection {

    private String HOST;

    private int PORT;

    private Socket socket;

    private PrintWriter writer;
    private boolean isConnected = false;

    public SocketConnection(String HOST, int PORT){
        this.HOST = HOST;
        this.PORT = PORT;
    }

    public void StartSocket(){
        new Thread(() ->{
            try{
                Socket socket = new Socket(this.HOST, this.PORT);
                OutputStream outputStream = socket.getOutputStream();
                writer = new PrintWriter(outputStream,true);
                isConnected = true;
                Log.d("SocketConnection", "StartSocket: Connected");
            }catch (Exception e){
                Log.d("SocketConnection", "StartSocket: "+ e.getMessage());
            }
        }).start();
    }

    public void SendMessage(String message){
        try{
            new Thread(() ->{

                if(writer != null && isConnected) {
                    writer.println(message);
                    Log.d("SocketConnection", "SendMessage: " + message);
                }else{
                    Log.d("SocketConnection", "SendMessage: Not Connected");
                }
            }).start();
        }catch(Exception e){
            Log.d("SocketConnection", "SendMessage: "+ e.getMessage());
        }
    }

    public void CloseSocket(){
            new Thread(() ->{
                try {
                    if (writer != null) {
                        writer.close();
                    }

                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                        isConnected = false;
                        Log.d("SocketConnection", "CloseSocket: Closed");
                    } else {
                        Log.d("SocketConnection", "CloseSocket: Not Connected");
                    }
                }
                catch (Exception e){
                    Log.d("SocketConnection", "CloseSocket: "+ e.getMessage());
                }
            }).start();
    }

    public boolean isConnected() {
        return isConnected;
    }

}
