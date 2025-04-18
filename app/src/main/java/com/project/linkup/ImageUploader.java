package com.project.linkup;

//import static com.example.app.LoginActivity.USERID;

import static com.project.linkup.ApiClient.API_KEY;
import static com.project.linkup.ApiClient.BUCKET_NAME;
import static com.project.linkup.FirebaseDatabaseRef.postRef;
import static com.project.linkup.FirebaseDatabaseRef.userRef;
import static com.project.linkup.MainActivity.USERID;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageUploader {


//profileUrl="https://ligptkvwcwnwoyogyodf.supabase.co/storage/v1/object/linkup/"+FilePath;

   public static File getFileFromUri(Context context, Uri imageuri) {

        try {
            ContentResolver resolver = context.getContentResolver();
            InputStream inputStream = resolver.openInputStream(imageuri);
            if (inputStream == null) {
                Log.e("getFileFromUri", "Failed to open InputStream");
                return null;
            }

            File file = new File(context.getCacheDir(), "tempFile.jpg"); // Create temp file
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();

            Log.d("getFileFromUri", "File loaded successfully: " + file.getAbsolutePath());
            return file;
        }
        catch (Exception e) {
            Log.e("getFileFromUri error!", "Exception: " + e.getMessage());
            return null;
        }
    }

//-----upload file
    public static void uploadFile(File file,String FilePath)  {
        StorageApi storageApi = ApiClient.getClient().create(StorageApi.class);//--------------------client

        //MediaType.parse("multipart/form-data")           //Images, PDFs, etc.
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        //Make API call
        Call<ResponseBody> call = storageApi.uploadFile(API_KEY,BUCKET_NAME, FilePath,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("uploadFile", "Upload success: ");
//                        renameFile(FilePath,"prtham.jpg");
                } else {
                    Log.e("uploadFile", "Upload failed: " + response.message());
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("Supabase", "Upload Error: " + t.getMessage());
            }
        });
    }


//-----check if image exists
    public static void checkExistImage(String profileUrl,File file,String FilePath) {
        StorageApi storageApi = ApiClient.getClient().create(StorageApi.class);//--------------------client

        Call<Void> checkFile = storageApi.checkImageExists(API_KEY,profileUrl);
        checkFile.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("checkFile", "file exist ");
                    deleteFile(file,FilePath);
                } else {
                    Log.e("checkFile", "file not exist " + response.message());
                    uploadFile(file,FilePath);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("checkFile", "deleteFile Error: " + t.getMessage());
            }
        });
    }

//-----Delete File
    public static void deleteFile(File file,String FilePath)  {
        StorageApi storageApi = ApiClient.getClient().create(StorageApi.class);//--------------------client

        Call<ResponseBody> deleteFile = storageApi.deleteFile(API_KEY,BUCKET_NAME, FilePath);
        deleteFile.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("deleteFile", "file deleted: ");

                } else {
                    Log.e("deleteFile", "file not deleted " + response.message());

                }
                uploadFile(file,FilePath);
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("deleteFile", "deleteFile Error: " + t.getMessage());
            }
        });
    }

//------rename File
   public static void renameFile(String tempName, String originalName){

       StorageApi storageApi = ApiClient.getClient().create(StorageApi.class);
       Call<ResponseBody> downloadcall = storageApi.getFile(API_KEY,BUCKET_NAME, tempName);

       downloadcall.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
               if(response.isSuccessful()){
                   try {
                       byte[] fileData = response.body().bytes();

//                       RequestBody requestBody = RequestBody.create(fileData, MediaType.parse("application/octet-stream"));
//                       MultipartBody.Part body = MultipartBody.Part.createFormData("file", originalName, requestBody);

//                       //MediaType.parse("multipart/form-data")           //Images, PDFs, etc.
                       RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),fileData);
                       MultipartBody.Part body = MultipartBody.Part.createFormData("file", originalName, requestFile);


                       Call<ResponseBody> uploadCall = storageApi.uploadFile(API_KEY,BUCKET_NAME,originalName, body);
                       uploadCall.enqueue(new Callback<ResponseBody>() {
                           @Override
                           public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                               if(response.isSuccessful()){
                                   Log.d("upload success", "File name: " + originalName);
                                   // Step 3: Delete old
                                   Call<ResponseBody> deleteCall = storageApi.deleteFile(API_KEY, BUCKET_NAME, tempName);
                                   deleteCall.enqueue(new Callback<ResponseBody>() {
                                       @Override
                                       public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                           if(response.isSuccessful()){
                                               Log.e("rename","renamed success!");
                                           }else {
                                               Log.e("rename","rename failed!");
                                           }
                                       }

                                       @Override
                                       public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                                           Log.e("upload onfailuter", "fail: " + throwable.getMessage());
                                       }
                                   });


                               }else {
                                   Log.e("upload faileddddd", "upload failed: " + response.message());
                               }
                           }

                           @Override
                           public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                               Log.e("upload_error", "Upload failed: " + throwable.getMessage(), throwable);
                           }
                       });


                   } catch (Exception e) {
                       Log.e("renacme error", "Upload failed: " + e.getMessage());
                   }
               }else {
                   Log.e("download","download failed!"+response.message());
               }
           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable throwable) {

           }
       });

   }

}
