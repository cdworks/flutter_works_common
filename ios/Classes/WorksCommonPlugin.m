#import "WorksCommonPlugin.h"
#import <AudioToolbox/AudioToolbox.h>

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

void WorksCommonSystemSoundFinishedPlayingCallback(SystemSoundID sound_id, void* user_data)
{
    AudioServicesDisposeSystemSoundID(sound_id);
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
  } else if ([@"playSytemSounds" isEqualToString:call.method]) {

      NSString* soundId = call.arguments;
      if(soundId.length)
      {
         NSURL *audioPath = [[NSBundle mainBundle] URLForResource:soundId withExtension:nil];
          if(audioPath != nil)
          {
             SystemSoundID soundID;
               OSStatus status = AudioServicesCreateSystemSoundID((__bridge CFURLRef)(audioPath), &soundID);

             if(status == errSecSuccess)
             {
                 status =  AudioServicesAddSystemSoundCompletion(soundID,
                                                          NULL, // uses the main run loop
                                                          NULL, // uses kCFRunLoopDefaultMode
                                                          WorksCommonSystemSoundFinishedPlayingCallback, // the name of our custom callback function
                                                          NULL // for user data, but we don't need to do that in this case, so we just pass NULL
                                                          );
                 if(status == errSecSuccess)
                 {
                     AudioServicesPlaySystemSound(soundID);
                     result(@(YES));
                     return;
                 }

             }
          }
      }
      else
      {
          AudioServicesPlaySystemSound(1007);
          result(@(YES));
          return;
      }
      
      result(@(NO));
  }
    else if ([@"vibrate" isEqualToString:call.method]) {

//      OSStatus status =  AudioServicesAddSystemSoundCompletion(kSystemSoundID_Vibrate,
//                                              NULL, // uses the main run loop
//                                              NULL, // uses kCFRunLoopDefaultMode
//                                    WorksCommonSystemSoundFinishedPlayingCallback, // the name of our custom callback function
//                                              NULL // for user data, but we don't need to do that in this case, so we just pass NULL
//                                              );
//        if(status == errSecSuccess)
//        {
            AudioServicesPlaySystemSound(kSystemSoundID_Vibrate);
            result(@(YES));
            return;
//        }
        
//        result(@(NO));
        
    }
  else {
    result(FlutterMethodNotImplemented);
  }
}

@end
