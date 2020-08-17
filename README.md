# works_common

一些公共的需要与原生通信的功能组合插件.


## 目前包含的功能列表

- 获取Android aseets文件夹中的文件路径 ios获取bundle中的文件路径
- 播放系统声音
- 震动

##usage

# 引入

```yaml
  dependencies:
    flutter:
      sdk: flutter
    works_common:
    #本地路径
      path: /**/flutter_works_common
#或者git地址
#	  git:
#       url: git://github.com/cdworks/flutter_works_common.git
```

# 示例

```dart

//获取原生文件路径
static Future<String>  getAssetFilePath(String fileName);

//播放提示音 ，若不传则播放默认提示音. android端忽略文件后缀，所以，如果android是sound.mp3 ios 是sound.caf 那么直接传sound.caf，两端通用
static Future<bool> playSytemSounds({String soundId})

//震动 milliseconds 震动时间 android有效
static Future<bool> vibrate({int milliseconds = 1000});


```