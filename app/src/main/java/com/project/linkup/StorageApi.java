package com.project.linkup;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface StorageApi {
//    @Multipart
//    @POST("object/img") // Replace "bucket-name" with your actual bucket name
//    Call<ResponseBody> uploadFile(
//            @Header("Authorization") String apiKey,
//            @Part MultipartBody.Part file
//    );

    @Multipart
    @POST("{bucket}/{filePath}")
    Call<ResponseBody> uploadFile(
            @Header("Authorization") String apiKey,
            @Path("bucket") String bucket,
            @Path("filePath") String filePath,
            @Part MultipartBody.Part file
    );

    @HEAD
    Call<Void> checkImageExists(
            @Header("apikey") String apiKey,
            @Url String url
    );
    @GET("{bucket}/{filePath}")
    Call<ResponseBody> getFile(
            @Header("Authorization") String apiKey,
            @Path("bucket") String bucket,
            @Path("filePath") String filePath
    );

    @DELETE("{bucket}/{filePath}")
    Call<ResponseBody> deleteFile(
            @Header("Authorization") String apiKey,
            @Path("bucket") String bucket,
            @Path("filePath") String filePath
    );

}

