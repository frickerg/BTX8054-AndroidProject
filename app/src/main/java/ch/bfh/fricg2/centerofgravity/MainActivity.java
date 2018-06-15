package ch.bfh.fricg2.centerofgravity;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.content.Context;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private static float GRAVITY = 9.81f;
    private ImageView paddleTop, paddleBottom, paddleLeft, paddleRight;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    public static float x;
    public static float y;

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
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = event.values[0] * GRAVITY;
            y = event.values[1] * GRAVITY;

            paddleTop.setX(paddleTop.getX() + y);
            paddleBottom.setX(paddleBottom.getX() - y);
            paddleLeft.setY(paddleLeft.getY() - x);
            paddleRight.setY(paddleRight.getY() + x);
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
}
