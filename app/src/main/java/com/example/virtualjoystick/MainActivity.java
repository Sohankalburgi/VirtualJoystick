package com.example.virtualjoystick;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String HOST = "192.168.31.141";
    private static final int PORT = 5051; // CHANGE THIS
    private Button leftButton;
    private Button rightButton;
    private Button bottomButton;
    private Button topButton;
    private TextView statusTextView;

    private Button squareButton, triangleButton, circleButton, crossButton;
    private Button startButton, selectButton;



    @SuppressLint({"ClickableViewAccessibility", "WrongViewCast"})
    // Suppress lint warning for setOnTouchListener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); // Make sure R.id.statusText exists
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rightButton = findViewById(R.id.RightBtn);
        leftButton = findViewById(R.id.LeftBtn);
        bottomButton = findViewById(R.id.DownBtn);
        topButton = findViewById(R.id.UpButton);
        squareButton = findViewById(R.id.SquareButton);
        triangleButton = findViewById(R.id.TriangleButton);
        circleButton = findViewById(R.id.CircleButton);
        crossButton = findViewById(R.id.CrossButton);

        // Start & Select
        startButton = findViewById(R.id.StartButton);
        selectButton = findViewById(R.id.SelectButton);

        SocketConnection socketConnection = new SocketConnection(HOST,PORT);

        socketConnection.StartSocket();

        handleButtonPress(rightButton,"RIGHT", socketConnection);
        handleButtonPress(leftButton,"LEFT", socketConnection);
        handleButtonPress(bottomButton,"DOWN", socketConnection);
        handleButtonPress(topButton,"UP", socketConnection);
        handleButtonPress(squareButton,"SQUARE", socketConnection);
        handleButtonPress(triangleButton,"TRIANGLE", socketConnection);
        handleButtonPress(circleButton,"CIRCLE", socketConnection);
        handleButtonPress(crossButton,"CROSS", socketConnection);
        handleButtonPress(startButton,"START", socketConnection);
        handleButtonPress(selectButton,"SELECT", socketConnection);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void handleButtonPress(Button button, String message, SocketConnection socketConnection){
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        socketConnection.SendMessage(message+"_"+"1");
                        break;
                    case MotionEvent.ACTION_UP:
                        socketConnection.SendMessage(message+"_"+"0");
                        break;
                }
                return true;
            }
        });
    }

}
