package com.project.linkup;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "https://ligptkvwcwnwoyogyodf.supabase.co/storage/v1/object/";
    public static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxpZ3B0a3Z3Y3dud295b2d5b2RmIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0MTQ2NDg1NywiZXhwIjoyMDU3MDQwODU3fQ.YvXAzQMY3jgh3LEStGrPqgpYkK1dSBuJwqLeyR_zY-g";
    public static final String BUCKET_NAME = "linkup";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

