package com.infinityleaks.arphonecomparator.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.infinityleaks.arphonecomparator.App;
import com.infinityleaks.arphonecomparator.http.HttpService;
import com.infinityleaks.arphonecomparator.model.SpecsModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainViewModel extends ViewModel {

    @Inject
    Retrofit retrofit;

    @Inject
    Context context;

    private MutableLiveData<ResponseBody> modelLiveData;
    private MutableLiveData<SpecsModel> specsLiveData;

    private List<Node> bubbleNodes;
    private List<AnchorNode> anchorNodes;

    private int maxRAMId;
    private int maxBatteryId;

    private int maxRamValue;
    private int maxBatteryValue;

    public MainViewModel() {
        App.getInjector().inject(this);
        modelLiveData = new MutableLiveData<>();
        specsLiveData = new MutableLiveData<>();
        bubbleNodes = new ArrayList<>();
        anchorNodes = new ArrayList<>();
        maxBatteryId = -1;
        maxRAMId = -1;

        maxRamValue = -1;
        maxBatteryValue = -1;
    }


    public MutableLiveData<SpecsModel> getSpecsLiveData() {
        return specsLiveData;
    }

    public void getPhoneSpecsById(String id){

        HttpService httpService = retrofit.create(HttpService.class);
        Call<SpecsModel> call = httpService.getPhoneSpecsById(id);

        call.enqueue(new Callback<SpecsModel>() {
            @Override
            public void onResponse(Call<SpecsModel> call, Response<SpecsModel> response) {
                specsLiveData.setValue(response.body());
                int batteryValue = Integer.valueOf(response.body().getBatteryHours());
                if(batteryValue > maxBatteryValue) {
                    maxBatteryId = Integer.valueOf(response.body().getId());
                    maxBatteryValue = Integer.valueOf(response.body().getBatteryHours());
                }
                int ramValue = Integer.valueOf(response.body().getRam());
                if(ramValue > maxRamValue) {
                    maxRAMId = Integer.valueOf(response.body().getId());
                    maxRamValue = Integer.valueOf(response.body().getRam());
                }
            }

            @Override
            public void onFailure(Call<SpecsModel> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public MutableLiveData<ResponseBody> getModelLiveData() {
        return modelLiveData;
    }

    public List<Node> getBubbleNodes() {
        return bubbleNodes;
    }

    public List<AnchorNode> getAnchorNodes() {
        return anchorNodes;
    }

    public int getMaxRAMId() {
        return maxRAMId;
    }

    public int getMaxBatteryId() {
        return maxBatteryId;
    }

    public int getMaxRamValue() {
        return maxRamValue;
    }

    public int getMaxBatteryValue() {
        return maxBatteryValue;
    }

    public void resetMax() {
        maxBatteryValue = -1;
        maxRamValue = -1;
        maxRAMId = -1;
        maxBatteryId = -1;
    }
}
