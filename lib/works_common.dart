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
}
