/*************************************************************************
 * ADOBE CONFIDENTIAL
 * ___________________
 *
 *  Copyright 2018 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by all applicable intellectual property
 * laws, including trade secret and copyright laws.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/
package adobe.bus.booking.demo.app;
import com.adobe.marketing.mobile.AdobeCallback;
import com.adobe.marketing.mobile.Analytics;
import com.adobe.marketing.mobile.Assurance;
import com.adobe.marketing.mobile.Identity;
import com.adobe.marketing.mobile.InvalidInitException;
import com.adobe.marketing.mobile.Lifecycle;
import com.adobe.marketing.mobile.LoggingMode;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.MobileServices;
import com.adobe.marketing.mobile.Signal;
import com.adobe.marketing.mobile.Target;
import com.adobe.marketing.mobile.UserProfile;
import android.app.Application;
import android.content.Context;


public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileCore.setApplication(this);
        MobileCore.setLogLevel(LoggingMode.VERBOSE);
         try{
             Assurance.registerExtension();
             Target.registerExtension();
             MobileServices.registerExtension();
             Analytics.registerExtension();
             UserProfile.registerExtension();
             Identity.registerExtension();
             Lifecycle.registerExtension();
             Signal.registerExtension();
             MobileCore.start(new AdobeCallback () {
                 @Override
                 public void call(Object o) {
                     MobileCore.configureWithAppID("f9893730911b/970d3214f904/launch-76d94107de00-development");
                 }
             });
         } catch(InvalidInitException e){}
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
