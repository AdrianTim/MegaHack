package com.infinityleaks.arphonecomparator.view;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.infinityleaks.arphonecomparator.App;
import com.infinityleaks.arphonecomparator.R;
import com.infinityleaks.arphonecomparator.dependency.MainModule;
import com.infinityleaks.arphonecomparator.model.SpecsModel;
import com.infinityleaks.arphonecomparator.util.DownloadCallback;
import com.infinityleaks.arphonecomparator.viewmodel.MainViewModel;

import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;

//implements DownloadCallback
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private static final int CODE_SCAN = 1;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 2;

    private ArFragment arFragment;
    private ModelRenderable assetRenderable;
    private ViewRenderable testViewRenderable;

    private ImageButton btnSearch;
    private ImageButton btnUndo;
    private ImageButton btnScan;
    private ImageButton btnMyPhone;
    private ImageButton btnSpecs;
    private ProgressBar pbMain;
    private TextView tvLoadedModel;


    private EditText etModelInput;

    String phoneId;
    private MainViewModel mainViewModel;

    private boolean showSpecs = false;


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

        ViewRenderable.builder()
                .setView(this, R.layout.bubble)
                .build()
                .thenAccept(renderable -> testViewRenderable = renderable);

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (assetRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    mainViewModel.getAnchorNodes().add(anchorNode);

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(assetRenderable);
                    andy.getScaleController().setEnabled(false);
                    andy.select();

                    ViewRenderable specsViewRenderable = null;

                    ViewRenderable.builder()
                            .setView(this, R.layout.bubble)
                            .build()
                            .thenAccept(renderable -> {createSpecsBubble(anchorNode, specsViewRenderable, renderable);});

                });

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        mainViewModel.getModelLiveData().observe(this, new Observer<ResponseBody>() {
//            @Override
//            public void onChanged(@Nullable ResponseBody responseBody) {
//                if (responseBody != null) {
//                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
//                    } else {
//                        downloadFile(responseBody, MainActivity.this);
//                    }
//                }
//            }
//        });
        mainViewModel.getSpecsLiveData().observe(this, new Observer<SpecsModel>() {
            @Override
            public void onChanged(@Nullable SpecsModel specsModel) {
                if(specsModel != null) {
                    tvLoadedModel.setText(specsModel.getName());
                    tvLoadedModel.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void removeChildren() {

        List<AnchorNode> nodes = mainViewModel.getAnchorNodes();

        for(AnchorNode anchorNode: nodes) {
            anchorNode.setParent(null);
            anchorNode.setRenderable(null);
        }

        try {
            arFragment.getArSceneView().getSession().update();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }

    }
    public void createSpecsBubble(AnchorNode anchorNode, ViewRenderable specsViewRenderable, ViewRenderable renderable ){

        specsViewRenderable = renderable;

        if(mainViewModel.getSpecsLiveData().getValue() != null){

            SpecsModel specsModel = mainViewModel.getSpecsLiveData().getValue();

            View bubbleView = (View) specsViewRenderable.getView();
            TextView tvCpu = bubbleView.findViewById(R.id.value_CPU);
            tvCpu.setText(specsModel.getCpu());
            TextView tvRam = bubbleView.findViewById(R.id.value_RAM);
            tvRam.setText(specsModel.getRam());
            TextView tvHeight = bubbleView.findViewById(R.id.value_Height);
            tvHeight.setText(specsModel.getHeight());
            TextView tvWidth = bubbleView.findViewById(R.id.value_Width);
            tvWidth.setText(specsModel.getWidth());
            TextView tvThickness = bubbleView.findViewById(R.id.value_Thickness);
            tvThickness.setText(specsModel.getThickness());
            TextView tvBattery = bubbleView.findViewById(R.id.value_Battery);
            tvBattery.setText(specsModel.getBatteryHours());

            Node specsBubble = new Node();
            specsBubble.setParent(anchorNode);
            specsBubble.setRenderable(specsViewRenderable);
            specsBubble.setLocalPosition(new Vector3(0.0f, 0.1f, 0.0f));
            specsBubble.setWorldScale(new Vector3(0.1f, 0.1f, 0.1f));
            specsBubble.setEnabled(showSpecs);
            mainViewModel.getBubbleNodes().add(specsBubble);

        }

    }

//    public void downloadFile(ResponseBody bytes, DownloadCallback callback) {
//        pbMain.setVisibility(View.VISIBLE);
//        new AsyncTask<Void, Void, Boolean>() {
//            @Override
//            protected Boolean doInBackground(Void... voids) {
//                return mainViewModel.writeResponseBodyToDisk(bytes);
//            }
//
//            @Override
//            protected void onPostExecute(Boolean writtenToDisk) {
//                super.onPostExecute(writtenToDisk);
//                if(writtenToDisk)
//                    callback.onDownloadSuccess();
//                else
//                    callback.onDownloadFailure();
//            }
//        }.execute();
//    }

    private void initControls() {
        btnSearch = findViewById(R.id.btn_search);
        btnScan = findViewById(R.id.btn_scan);
        btnMyPhone = findViewById(R.id.btn_myPhone);
        btnUndo = findViewById(R.id.btn_revert);
        btnSpecs = findViewById(R.id.btn_specs);
        btnSpecs.setImageDrawable(showSpecs ? getDrawable(
                R.drawable.ic_info_outline_black_24dp) : getDrawable(R.drawable.ic_info_outline_white_24dp));
        etModelInput = findViewById(R.id.et_model_input);
        pbMain = findViewById(R.id.pb_main);
        tvLoadedModel = findViewById(R.id.tv_loadedModel);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        btnScan.setOnClickListener(v -> startActivityForResult(
                new Intent(MainActivity.this, ScanActivity.class), CODE_SCAN));

        btnMyPhone.setOnClickListener(v -> {
            loadAsset("1");
        });

        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeChildren();
            }
        });

        btnSpecs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSpecs();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mainViewModel.getPhoneModelByName(etModelInput.getText().toString());
                loadAsset(etModelInput.getText().toString());
            }
        });

    }

    private void toggleSpecs() {
        showSpecs = !showSpecs;
        btnSpecs.setImageDrawable(showSpecs ? getDrawable(
                R.drawable.ic_info_outline_black_24dp) : getDrawable(R.drawable.ic_info_outline_white_24dp));
        for(Node node: mainViewModel.getBubbleNodes()) {
            node.setEnabled(showSpecs);
        }
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

    private void loadAsset(String phoneId) {
        //if(phoneId == -1) {
        pbMain.setVisibility(View.VISIBLE);
            ModelRenderable.builder()
                    //.setSource(this, Uri.fromFile(mainViewModel.getDownloadedModel()))
                    .setSource(this, Uri.parse(MainModule.BASE_URL+"phones/"+phoneId+"/sfb"))
                    .build()
                    .thenAccept(renderable -> {
                        assetRenderable = renderable;
                    pbMain.setVisibility(View.GONE);})
                    .exceptionally(
                            throwable -> {
                                Toast toast =
                                        Toast.makeText(this, "Unable to load asset", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                return null;
                            });

        mainViewModel.getPhoneSpecsById(phoneId);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_SCAN) {
            if(resultCode == Activity.RESULT_OK){
                phoneId = data.getStringExtra("result");
                loadAsset(phoneId);
                //mainViewModel.getPhoneModelById(phoneId);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

//    @Override
//    public void onDownloadSuccess() {
//        Toast.makeText(this, "Modelul gasit a fost incarcat", Toast.LENGTH_SHORT).show();
//        //loadAsset(-1);
//        pbMain.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onDownloadFailure() {
//        pbMain.setVisibility(View.GONE);
//        Toast.makeText(this, "Modelul nu a putut fi descarcat", Toast.LENGTH_SHORT).show();
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_READ_EXTERNAL_STORAGE: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    downloadFile(mainViewModel.getModelLiveData().getValue(), MainActivity.this);
//                } else {
//                    //HANDLE IT!
//                }
//                return;
//            }
//        }
//    }

}
