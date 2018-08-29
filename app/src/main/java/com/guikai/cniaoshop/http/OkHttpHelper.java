package com.guikai.cniaoshop.http;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
 * Time:         2018/8/28 23:29
 * Package_Name: com.guikai.cniaoshop.http
 * File_Name:    OkHttpHelper
 * Creator:      Anding
 * Note:         封装OkHttp请求
 */
public class OkHttpHelper {


    private static OkHttpClient okHttpClient;

    private Gson gson;

    //单例模式
    private OkHttpHelper(){

        okHttpClient = new OkHttpClient.Builder()
                           .readTimeout(10, TimeUnit.SECONDS)
                           .writeTimeout(10, TimeUnit.SECONDS)
                           .connectTimeout(10,TimeUnit.SECONDS)
                           .build();
        gson = new Gson();
    }

    //单例模式
    private static OkHttpHelper getInstance() {
        return new OkHttpHelper();
    }

    public void get(String url, BaseCallback callback) {

        Request request = buildRequset(url, null, HttpMethodType.GET);

        doRequest(request,callback);
    }

    public void post(String url, Map<String, String> params, BaseCallback callback) {

        Request request = buildRequset(url, params, HttpMethodType.POST);

        doRequest(request,callback);
    }


    public void doRequest(final Request request, final BaseCallback callback) {

        callback.onRequestBefore(request);

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

//                response.isSuccessful()
//                response.body().string()
//                gson.fromJson(response.body().string(), callback.mType);
                if (response.isSuccessful()) {

                    String resultStr = response.body().string();

                    if (callback.mType == String.class) {
                        callback.onSuccess(call, response, resultStr);
                    }
                    else {


                        try {
                            Object object = gson.fromJson(resultStr, callback.mType);
                            callback.onSuccess(call,response,object);
                        } catch (JsonParseException e) {
                            callback.onError(call, response, response.code(),e);
                        }
                    }


                }
                else {
                    callback.onError(call,response,response.code(),null);
                }

            }
        });
    }

    private Request buildRequset(String url, Map<String, String> params, HttpMethodType methodType) {

        Request.Builder builder = new Request.Builder();

        builder.url(url);

        if (methodType == HttpMethodType.GET) {
            builder.get();
        }
        else if (methodType == HttpMethodType.POST) {

            RequestBody body = buildFormData(params);

            builder.post(body);
        }
        return builder.build();
    }

    private RequestBody buildFormData(Map<String, String> params) {

        FormBody.Builder builder = new FormBody.Builder();

        if (params != null) {

            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }

        }

        return builder.build();
    }

    enum HttpMethodType{
        GET,
        POST
    }

}
