package com.infinityleaks.arphonecomparator.view;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.infinityleaks.arphonecomparator.App;
import com.infinityleaks.arphonecomparator.R;
import com.infinityleaks.arphonecomparator.util.DownloadCallback;
import com.infinityleaks.arphonecomparator.viewmodel.MainViewModel;

import java.io.File;

import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements DownloadCallback{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private static final int CODE_SCAN = 1;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 2;

    private ArFragment arFragment;
    private ModelRenderable assetRenderable;

    private ImageButton btnSearch;
    private ImageButton btnUndo;
    private ImageButton btnScan;
    private ImageButton btnMyPhone;

    private EditText etModelInput;

    String phoneId;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initControls();

        App.getInjector().inject(this);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getModelLiveData().observe(this, new Observer<ResponseBody>() {
            @Override
            public void onChanged(@Nullable ResponseBody responseBody) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                } else {
                    downloadFile(responseBody, MainActivity.this);
                }
            }
        });
    }

    public void downloadFile(ResponseBody bytes, DownloadCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return mainViewModel.writeResponseBodyToDisk(bytes);
            }

            @Override
            protected void onPostExecute(Boolean writtenToDisk) {
                super.onPostExecute(writtenToDisk);
                if(writtenToDisk)
                    callback.onDownloadSuccess();
                else
                    callback.onDownloadFailure();
            }
        }.execute();
    }

    private void initControls() {
        btnSearch = findViewById(R.id.btn_search);
        btnScan = findViewById(R.id.btn_scan);
        btnMyPhone = findViewById(R.id.btn_myPhone);
        btnUndo = findViewById(R.id.btn_revert);
        etModelInput = findViewById(R.id.et_model_input);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        btnScan.setOnClickListener(v -> startActivityForResult(
                new Intent(MainActivity.this, ScanActivity.class), CODE_SCAN));

        btnMyPhone.setOnClickListener(v -> {
            loadAsset(R.raw.iphone);
        });

    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }

        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    private void loadAsset(int resourceId) {
        if(resourceId == -1) {
            ModelRenderable.builder()
                    .setSource(this, Uri.fromFile(mainViewModel.getDownloadedModel()))
                    .build()
                    .thenAccept(renderable -> assetRenderable = renderable)
                    .exceptionally(
                            throwable -> {
                                Toast toast =
                                        Toast.makeText(this, "Unable to load asset", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return null;
                            });

            arFragment.setOnTapArPlaneListener(
                    (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                        if (assetRenderable == null) {
                            return;
                        }

                        // Create the Anchor.
                        Anchor anchor = hitResult.createAnchor();
                        AnchorNode anchorNode = new AnchorNode(anchor);
                        anchorNode.setParent(arFragment.getArSceneView().getScene());

                        // Create the transformable andy and add it to the anchor.
                        TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                        andy.setParent(anchorNode);
                        andy.setRenderable(assetRenderable);
                        andy.select();
                    });
        } else {
            ModelRenderable.builder()
                    .setSource(this, resourceId)
                    .build()
                    .thenAccept(renderable -> assetRenderable = renderable)
                    .exceptionally(
                            throwable -> {
                                Toast toast =
                                        Toast.makeText(this, "Unable to load asset", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return null;
                            });

            arFragment.setOnTapArPlaneListener(
                    (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                        if (assetRenderable == null) {
                            return;
                        }

                        // Create the Anchor.
                        Anchor anchor = hitResult.createAnchor();
                        AnchorNode anchorNode = new AnchorNode(anchor);
                        anchorNode.setParent(arFragment.getArSceneView().getScene());

                        // Create the transformable andy and add it to the anchor.
                        TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                        andy.setParent(anchorNode);
                        andy.setRenderable(assetRenderable);
                        andy.select();
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_SCAN) {
            if(resultCode == Activity.RESULT_OK){
                phoneId = data.getStringExtra("result");
                mainViewModel.getPhoneModelById(phoneId);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public void onDownloadSuccess() {
        Toast.makeText(this, "Modelul gasit a fost incarcat", Toast.LENGTH_SHORT).show();
        loadAsset(-1);
    }

    @Override
    public void onDownloadFailure() {
        Toast.makeText(this, "Modelul nu a putut fi descarcat", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadFile(mainViewModel.getModelLiveData().getValue(), MainActivity.this);
                } else {
                    //HANDLE IT!
                }
                return;
            }
        }
    }
}
