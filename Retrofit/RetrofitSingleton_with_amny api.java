

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import Utilities.SessionManager;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class RetrofitSingleton {
    private static Retrofit sRetrofit;

    static String appId2;
    static String appId;
    static String deviceId2;
    static String deviceId;
    static String firebaseId2;
    static String firebaseId;
    static String firebaseIdToken;
    static String customerId2;
    static String customerId;

    static String appId1;
    static String deviceId1 = "deviceId";
    static String firebaseId1 = "firebaseId";
    static String customerId1 = "cusId";
    public static String applicationName = "CustomerApp";

    private RetrofitSingleton() {
    }

    public synchronized static Retrofit getInstance(final Context context, String mBaseUrl) {

        if (sRetrofit == null) {
            createRetrofit(context, mBaseUrl);
        }else{
            createRetrofit(context, mBaseUrl);
        }
        return sRetrofit;
    }

    private static void createRetrofit(final Context context, String baseUrl) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        deviceId2 = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

                        try {
                            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                            appId2 = pInfo.versionName;

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        appId1 = "android";

                        try {

                            firebaseIdToken = FirebaseInstanceId.getInstance().getToken();
                            Log.e("firebasetoken", firebaseIdToken);
                            if (firebaseIdToken != null) {
                                firebaseId2 = firebaseIdToken.replaceAll(":", "=");
                                firebaseId = firebaseId1.concat(firebaseId2);
                            } else {
                                firebaseId2 = "";
                                firebaseId = firebaseId1.concat(firebaseId2);
                            }

                        } catch (Exception e) {
                            firebaseIdToken = "";
                            firebaseId = "";
                        }
                        SessionManager mSessionManager = new SessionManager(context);
                        HashMap<String, Integer> userProfileId = mSessionManager.getUserID();
                        customerId2 = String.valueOf(userProfileId.get(SessionManager.UserIdKey));

                        appId = appId1.concat(appId2);
                        deviceId = deviceId1.concat(deviceId2);
                        customerId = customerId1.concat(customerId2);

                        
                        Request orginalReq = chain.request();

                        Request newRequest = orginalReq.newBuilder()
                                .header("AppId", appId)
                                .header("DeviceId", deviceId)
                                .header("FirebaseId", firebaseId)
                                .header("CustomerId", customerId)
                                .header("ApplicationName", applicationName)
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .readTimeout(30, TimeUnit.SECONDS)
                .cache(new Cache(context.getCacheDir(), 10 * 1024 * 1024))
                .build();



        /*OkHttpClient okHttpClient2 = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        deviceId2 = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

                        try {
                            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                            appId2 = pInfo.versionName;

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        appId1 = "android";

                        try {

                            firebaseIdToken = FirebaseInstanceId.getInstance().getToken();
                            Log.e("firebasetoken", firebaseIdToken);
                            if (firebaseIdToken != null) {
                                firebaseId2 = firebaseIdToken.replaceAll(":", "=");
                                firebaseId = firebaseId1.concat(firebaseId2);
                            } else {
                                firebaseId2 = "";
                                firebaseId = firebaseId1.concat(firebaseId2);
                            }

                        } catch (Exception e) {
                            firebaseIdToken = "";
                            firebaseId = "";
                        }
                        SessionManager mSessionManager = new SessionManager(context);
                        HashMap<String, Integer> userProfileId = mSessionManager.getUserID();
                        customerId2 = String.valueOf(userProfileId.get(SessionManager.UserIdKey));

                        appId = appId1.concat(appId2);
                        deviceId = deviceId1.concat(deviceId2);
                        customerId = customerId1.concat(customerId2);

                        

                        Request orginalReq = chain.request();

                        Request newRequest = orginalReq.newBuilder()
                                .header("AppId", appId)
                                .header("DeviceId", deviceId)
                                .header("FirebaseId", firebaseId)
                                .header("CustomerId", customerId)
                                .header("ApplicationName", applicationName)
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .readTimeout(10, TimeUnit.SECONDS)
                .cache(new Cache(context.getCacheDir(), 10 * 1024 * 1024))
                .build();*/

        


        if (baseUrl.equals("apiBase")) {

            sRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://api....net")  
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        } else if (baseUrl.equals("elasticBase")) {
            sRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://elastic....net")    
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        } else if (baseUrl.equals("merchantApiBase")) {

            sRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://merchantapi....net")  
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        } else if (baseUrl.equals("awsBase")) {
            sRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://es2.....net")  
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }else if (baseUrl.equals("imageUploadBase")) {
            sRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://adm....net")   
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }else if (baseUrl.equals("bridgeApiBase")) {

            sRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://....net")   
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }else if (baseUrl.equals("voucherApiBase")) {

            sRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://api....net")  
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }
}
