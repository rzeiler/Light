package rze.app.light;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    boolean b = false, hasFlash = false;
    Camera camera;
    Camera.Parameters parameters;
    ImageButton tb;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            camera = Camera.open();
            parameters = camera.getParameters();
            hasFlash = true;
        }

        tb = (ImageButton) findViewById(R.id.imageButton);
        tv = (TextView) findViewById(R.id.textView);

        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasFlash) {
                    new SetCamera(tb, tv).execute();
                } else {
                    Toast.makeText(MainActivity.this, "No Flashlight Support", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class SetCamera extends AsyncTask<Object, Void, Boolean> {

        private ImageButton tb;
        private TextView tv;

        public SetCamera(ImageButton _tb, TextView _tv) {
            tb = _tb;
            tv = _tv;
        }

        protected Boolean doInBackground(Object... params) {
            b = (b) ? false : true;
            if (b) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
                camera.startPreview();
            } else {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.stopPreview();
            }
            return b;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv.setText(getString(R.string.dots));
            if (!b) {
                tb.setBackgroundResource(R.drawable.on);
            } else {
                tb.setBackgroundResource(R.drawable.off);
            }
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            if (b) {
                tv.setText(getString(R.string.on));
            } else {
                tv.setText(getString(R.string.off));
            }
        }
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
