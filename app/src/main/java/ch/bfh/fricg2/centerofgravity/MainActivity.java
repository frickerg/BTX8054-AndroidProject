package ch.bfh.fricg2.centerofgravity;

import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    public static final float GRAVITY = 9.81f;      // g constant (static, no acceleration)
    public static final float BALL_SPEED = 1.25f;   // human walking speed (4.5 km/h)
    private int currentAngle;
    public boolean isGameStarted = false;

    private ImageView paddleTop, paddleBottom, paddleLeft, paddleRight, ball;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    public static int x, y;
    private static final float HORIZONTAL_LIMITER = 650f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        paddleTop = findViewById(R.id.paddleTop);
        paddleBottom = findViewById(R.id.paddleBottom);
        paddleLeft = findViewById(R.id.paddleLeft);
        paddleRight = findViewById(R.id.paddleRight);
        ball = findViewById(R.id.imageSpriteBall);

        final Button clickButton = (Button) findViewById(R.id.buttonPlay);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGameStarted = !isGameStarted;
            }
        });


        Random r = new Random();
        currentAngle = r.nextInt(360 - 1) + 1;
        int moveX = calculateXCoordinateBasedOnAngle(currentAngle, GRAVITY);
        int moveY = calculateYCoordinateBasedOnAngle(currentAngle, GRAVITY);
        ball.setX(ball.getX() + moveX);
        ball.setY(ball.getY() + moveY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isGameStarted && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            y = Math.round(event.values[0] * GRAVITY);
            x = Math.round(event.values[1] * GRAVITY);

            if (paddleTop.getX() - x >= paddleLeft.getX() && paddleTop.getX() - x <= paddleRight.getX()) {
                paddleTop.setX(paddleTop.getX() - x);
                paddleBottom.setX(paddleBottom.getX() + x);
            }

            if (paddleLeft.getY() - y >= paddleTop.getY() && paddleLeft.getY() - y <= paddleBottom.getY()) {
                paddleLeft.setY(paddleLeft.getY() - y);
                paddleRight.setY(paddleRight.getY() + y);
            }

            int moveX = calculateXCoordinateBasedOnAngle(currentAngle, BALL_SPEED);
            int moveY = calculateYCoordinateBasedOnAngle(currentAngle, BALL_SPEED);
            ball.setX(ball.getX() + moveX);
            ball.setY(ball.getY() + moveY);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor == this.accelerometer) {
            switch (accuracy) {
                case 0:
                    System.out.println("Unreliable");
                    break;
                case 1:
                    System.out.println("Low Accuracy");
                    break;
                case 2:
                    System.out.println("Medium Accuracy");
                    break;
                case 3:
                    System.out.println("High Accuracy");
                    break;
            }
        }
    }

    public static int calculateXCoordinateBasedOnAngle(int angle, float distance){
        return Math.round(distance * (float) Math.cos(angle));
    }

    public static int calculateYCoordinateBasedOnAngle(int angle, float distance) {
        int y = Math.round(distance * (float) Math.sin(angle));
        return y;
    }
}
