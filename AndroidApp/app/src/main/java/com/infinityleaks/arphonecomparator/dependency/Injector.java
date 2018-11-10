package com.infinityleaks.arphonecomparator.dependency;

import com.infinityleaks.arphonecomparator.view.MainActivity;
import com.infinityleaks.arphonecomparator.viewmodel.MainViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MainModule.class})
public interface Injector {
    void inject(MainActivity mainActivity);
    void inject(MainViewModel mainViewModel);
}

