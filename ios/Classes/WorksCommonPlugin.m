#import "WorksCommonPlugin.h"

@interface WorksCommonPlugin()

@property(nonatomic,strong)id<FlutterPluginRegistrar> registrar;

@end

@implementation WorksCommonPlugin

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"works_common"
            binaryMessenger:[registrar messenger]];
  WorksCommonPlugin* instance = [[WorksCommonPlugin alloc] init];
    instance.registrar = registrar;
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getAssetFilePath" isEqualToString:call.method]) {

      NSString* key = [_registrar lookupKeyForAsset:call.arguments];
      if(!key.length)
      {
         result([FlutterError errorWithCode:@"-1" message:@"无此文件！" details:nil]);
          return;
      }
      NSURL* path = [[NSBundle mainBundle] URLForResource:key withExtension:nil];
      result(path.absoluteString);
  } else {
    result(FlutterMethodNotImplemented);
  }
}

@end
