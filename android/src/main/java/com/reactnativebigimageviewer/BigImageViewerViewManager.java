package com.reactnativebigimageviewer;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator;
import com.github.piasy.biv.loader.ImageLoader;
import com.github.piasy.biv.view.BigImageView;

import java.io.File;
import java.util.Map;

public class BigImageViewerViewManager extends SimpleViewManager<View> {
  public static final String REACT_CLASS = "BigImageViewerView";


  @Override
  @NonNull
  public String getName() {
    return REACT_CLASS;
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
  @Override
  @NonNull
  public View createViewInstance(ThemedReactContext reactContext) {
    System.out.println("CREATE VIEW INSTANCE");
    LinearLayout layout = new LinearLayout(reactContext);
    BigImageView view = new BigImageView(reactContext);
    view.setLayoutParams(new LinearLayout.LayoutParams(
      LinearLayout.LayoutParams.MATCH_PARENT,
      LinearLayout.LayoutParams.MATCH_PARENT));
    layout.addView(view);

//    view.setInitScaleType(BigImageView.INIT_SCALE_TYPE_CENTER_INSIDE);

    return layout;
  }

  @Override
  public @Nullable
  Map getExportedCustomDirectEventTypeConstants() {
    return MapBuilder.of(
      "onSuccess",
      MapBuilder.of("registrationName", "onSuccess"),
      "onProgress",
      MapBuilder.of("registrationName", "onProgress")
      );
  }
  @ReactProp(name = "url")
  public void setUrl(LinearLayout layout, String url) {
    BigImageView view = (BigImageView) layout.getChildAt(0);
    ReactContext reactContext = (ReactContext) layout.getContext();
    ImageLoader.Callback myImageLoaderCallback = new ImageLoader.Callback() {
      @Override
      public void onCacheHit(int imageType, File image) {
        System.out.println("cache hit");
        // Image was found in the cache
      }

      @Override
      public void onCacheMiss(int imageType, File image) {
        // Image was downloaded from the network
      }

      @Override
      public void onStart() {
        System.out.println("START");

        // Image download has started
      }

      @Override
      public void onProgress(int progress) {
        WritableMap map = Arguments.createMap();
        map.putInt("progress",progress);
        map.putString("url", url);
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
          layout.getId(),
          "onProgress",
          map
        );
        // Image download progress has changed
      }

      @Override
      public void onFinish() {
        System.out.println("Finish");
        // Image download has finished
      }

      @Override
      public void onSuccess(File image) {
        WritableMap map = Arguments.createMap();
        map.putString("url",url);
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
          layout.getId(),
          "onSuccess",
          map
        );
        layout.invalidate();
        layout.requestLayout();
        // Image was retrieved successfully (either from cache or network)
      }

      @Override
      public void onFail(Exception error) {
        // Image download failed
        System.out.println("error");

        System.out.println(error.toString());
      }
    };
    if(view.getSSIV() != null){
      view.getSSIV().recycle();
    }
    view.cancel();
    view.setImageLoaderCallback(myImageLoaderCallback);
    view.showImage(Uri.parse(url));
  }
}
