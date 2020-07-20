package com.works.works_common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.annotation.NonNull;

import java.io.IOException;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import static android.content.Context.VIBRATOR_SERVICE;

/** WorksCommonPlugin */
public class WorksCommonPlugin implements FlutterPlugin, MethodCallHandler {

  private Context applicationContext;

  static  WorksCommonPlugin worksCommonPlugin;


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {

    if(worksCommonPlugin == null)
    {
      worksCommonPlugin = this;
      worksCommonPlugin.applicationContext = flutterPluginBinding.getApplicationContext();

      final MethodChannel channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "works_common");
      channel.setMethodCallHandler(this);
    }
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  public static void registerWith(Registrar registrar) {

    if(worksCommonPlugin == null)
    {
      final MethodChannel channel = new MethodChannel(registrar.messenger(), "works_common");
      worksCommonPlugin = new WorksCommonPlugin();
      worksCommonPlugin.applicationContext = registrar.context();
      channel.setMethodCallHandler(worksCommonPlugin);
    }

  }

  @TargetApi(Build.VERSION_CODES.M)
  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getAssetFilePath")) {
      result.success(call.arguments);
    }
    else if (call.method.equals("playSytemSounds")) {
      String soundName = (String) call.arguments;
      if(soundName.length() > 0)
      {
        SoundPool soundPool;
        //实例化SoundPool

        //sdk版本21是SoundPool 的一个分水岭
        if (Build.VERSION.SDK_INT >= 21) {
          SoundPool.Builder builder = new SoundPool.Builder();
          //传入最多播放音频数量,
          builder.setMaxStreams(1);
          //AudioAttributes是一个封装音频各种属性的方法
          AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
          //设置音频流的合适的属性
          attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
          //加载一个AudioAttributes
          builder.setAudioAttributes(attrBuilder.build());
          soundPool = builder.build();
        } else {
          /**
           * 第一个参数：int maxStreams：SoundPool对象的最大并发流数
           * 第二个参数：int streamType：AudioManager中描述的音频流类型
           *第三个参数：int srcQuality：采样率转换器的质量。 目前没有效果。 使用0作为默认值。
           */
          soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        try {
          String soundId = soundName;
          if(soundId.contains("."))
          {
            soundId = soundName.split("\\.")[0];
          }
          int soundResourceId = applicationContext.getResources().getIdentifier(soundId, "raw", applicationContext.getPackageName());
//          AssetFileDescriptor descriptor = applicationContext.getAssets().openFd(soundId);
          final int voiceId = soundPool.load(applicationContext, soundResourceId, 1);
          soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
              if (status == 0) {
                //第一个参数soundID
                //第二个参数leftVolume为左侧音量值（范围= 0.0到1.0）
                //第三个参数rightVolume为右的音量值（范围= 0.0到1.0）
                //第四个参数priority 为流的优先级，值越大优先级高，影响当同时播放数量超出了最大支持数时SoundPool对该流的处理
                //第五个参数loop 为音频重复播放次数，0为值播放一次，-1为无限循环，其他值为播放loop+1次
                //第六个参数 rate为播放的速率，范围0.5-2.0(0.5为一半速率，1.0为正常速率，2.0为两倍速率)
                soundPool.play(voiceId, 1, 1, 1, 0, 1);
              }
            }
          });
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }

      }
      else
      {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(applicationContext, notification);
        r.play();
      }


    }
    else if (call.method.equals("vibrate")) {
      Vibrator vibrator = (Vibrator) applicationContext.getSystemService(VIBRATOR_SERVICE);
      int duration = (int) call.arguments;
      vibrator.vibrate(duration);
    }
    else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }
}
