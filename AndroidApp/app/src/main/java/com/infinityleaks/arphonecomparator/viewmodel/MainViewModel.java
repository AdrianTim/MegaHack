package com.infinityleaks.arphonecomparator.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.infinityleaks.arphonecomparator.App;
import com.infinityleaks.arphonecomparator.http.ApiResponse;
import com.infinityleaks.arphonecomparator.http.HttpService;
import com.infinityleaks.arphonecomparator.view.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    File downloadedModel;

    private MutableLiveData<ResponseBody> modelLiveData;

    public MainViewModel() {
        App.getInjector().inject(this);
        modelLiveData = new MutableLiveData<>();
    }

    public void getPhoneModelByName(String name) {
        HttpService httpService = retrofit.create(HttpService.class);

        Call<ResponseBody> call = httpService.getPhoneModelByName(name);
    }

    public void getPhoneModelById(String id) {
        HttpService httpService = retrofit.create(HttpService.class);

        Call<ResponseBody> call = httpService.getPhoneModelById(id);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                modelLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File downloadFolder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "MEGAHACK");
            if(!downloadFolder.exists()) {
                if(!downloadFolder.mkdir())
                    throw(new Exception("Nu s-a putut face folderul de downloads."));
            }

            downloadedModel = new File(downloadFolder, "temp.sfb");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(downloadedModel);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("FILE DOWNLOAD", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public MutableLiveData<ResponseBody> getModelLiveData() {
        return modelLiveData;
    }

    public File getDownloadedModel() {
        return downloadedModel;
    }
}
