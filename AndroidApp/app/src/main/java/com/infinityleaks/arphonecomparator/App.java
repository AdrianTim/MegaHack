package com.infinityleaks.arphonecomparator;

import android.app.Application;

import com.infinityleaks.arphonecomparator.dependency.DaggerInjector;
import com.infinityleaks.arphonecomparator.dependency.Injector;
import com.infinityleaks.arphonecomparator.dependency.MainModule;

public class App extends Application {
    private static Injector injector;

    public static Injector getInjector() {
        return injector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        injector = DaggerInjector.builder()
                .mainModule(new MainModule(getApplicationContext()))
                .build();
    }
}
