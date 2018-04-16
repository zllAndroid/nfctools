//
//  config.h
//  ihealthsdk
//
//  Created by xuhua on 12/07/2017.
//  Copyright © 2017 xuhua. All rights reserved.
//

#ifndef config_h
#define config_h

#define CX_PLATFORM_UNKNOWN             0
#define CX_PLATFORM_IOS                 1
#define CX_PLATFORM_ANDROID             2
#define CX_PLATFORM_WINDOWS             3
#define CX_PLATFORM_OSX                 4
#define CX_PLATFORM_LINUX               5

#ifdef _WIN32
    #define CX_TARGET_PLATFORM          CX_PLATFORM_WINDOWS
    #define WIN32                       1
    #define WIN64                       0
#elif _WIN64
    #define CX_TARGET_PLATFORM          CX_PLATFORM_WINDOWS
    #define WIN32                       0
    #define WIN64                       1
#elif __APPLE__
    #include <TargetConditionals.h>
    #if TARGET_IPHONE_SIMULATOR
        #define CX_TARGET_PLATFORM      CX_PLATFORM_IOS
        #define IOS_SIMULATOR           1
    #elif TARGET_OS_IPHONE
        #define CX_TARGET_PLATFORM      CX_PLATFORM_IOS
        #define IOS_SIMULATOR           0
    #elif TARGET_OS_MAC
        #define CX_TARGET_PLATFORM      CX_PLATFORM_OSX
    #else
        #error "Unknown Apple platform"
    #endif
#elif __ANDROID__
    #define CX_TARGET_PLATFORM          CX_PLATFORM_ANDROID
#else
    #error "Unknown platform"
#endif

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <stdbool.h>
#include <string.h>

#endif /* config_h */
