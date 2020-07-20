import 'dart:async';

import 'package:flutter/services.dart';

class WorksCommon {
  static const MethodChannel _channel =
      const MethodChannel('works_common');

  static Future<String>  getAssetFilePath(String fileName) async {
    if(fileName == null || fileName.isEmpty)
      return '';
    final String version = await _channel.invokeMethod('getAssetFilePath',fileName);
    return version;
  }


  //播放提示音 ，若不传则播放默认提示音
  static Future<bool> playSytemSounds({String soundId}) async
  {
     return await _channel.invokeMethod('playSytemSounds',soundId ?? '');
  }

  //milliseconds 震动时间 android有效
  static Future<bool> vibrate({int milliseconds = 1000}) async
  {
    return await _channel.invokeMethod('vibrate',milliseconds);
  }

}
