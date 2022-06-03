package com.reactnativebigimageviewer;

import android.content.Context;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.fresco.FrescoImageLoader;
import com.github.piasy.biv.loader.glide.GlideCustomImageLoader;
import com.github.piasy.biv.loader.glide.GlideImageLoader;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BigImageViewerPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
      Context appContext = reactContext.getApplicationContext();
      BigImageViewer.initialize(FrescoImageLoader.with(appContext));
        // MUST use app context to avoid memory leak!
        // load with fresco
//
//        // or load with glide
//        BigImageViewer.initialize(GlideImageLoader.with(appContext));
//
//        // or load with glide custom component
//        BigImageViewer.initialize(GlideCustomImageLoader.with(appContext, CustomComponentModel.class));
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {


        return Arrays.<ViewManager>asList(new BigImageViewerViewManager());
    }
}
