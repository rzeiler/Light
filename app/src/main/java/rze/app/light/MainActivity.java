package rze.app.light;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {

    boolean b = false, hasFlash = false;
    Camera camera;
    Camera.Parameters parameters;
    ImageButton tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ( getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            camera = Camera.open();
            parameters = camera.getParameters();
            hasFlash = true;
            Log.d("camera"," has flash");
        }

        tb = (ImageButton) findViewById(R.id.imageButton);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasFlash) {
                    b = (b) ? false : true;
                    Log.d("hasFlash"," b is " + String.valueOf(b));
                    if (b) {
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(parameters);
                        camera.startPreview();
                        tb.setBackgroundResource(R.drawable.light_on);
                    } else {
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(parameters);
                        camera.stopPreview();
                        tb.setBackgroundResource(R.drawable.light_off);
                    }
                }else{
                    Toast.makeText(MainActivity.this, "No Flashlight Support", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

}
