//
//  alipay.m
//  alipay
//
//  Created by 郑江荣 on 2018/5/31.
//  Copyright © 2018年 郑江荣. All rights reserved.
//

#import "alipay.h"
#import <WeexSDK/WXSDKInstance.h>
#import <WeexSDK/WXSDKEngine.h>
#import "WXAlipayModule.h"
#import "AlipayManager.h"
#import "farwolf_weex.h"
WX_PLUGIN_Entry(alipay)
@implementation alipay
-(void)initEntry:(NSMutableDictionary*)lanchOption
{
//    [WXSDKEngine registerModule:@"alipay" withClass:[WXAlipayModule class]];
    [[AlipayManager sharedManager] initHanler];
}
@end
