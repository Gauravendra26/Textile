package com.example.nsso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static String BEARER_TOKEN = ""; // Replace with your Bearer token

    private static final int REQUEST_CODE_PERMISSION = 2;
    private ImageView imageView;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
          SharedPreferences sharedPreferences =
                getSharedPreferences("MyData", MODE_PRIVATE);

      BEARER_TOKEN =  sharedPreferences.getString("Login_Token", ""); // Replace with your Bearer token

        Toast.makeText(this, ""+BEARER_TOKEN, Toast.LENGTH_SHORT).show();
        Button selectImageButton = findViewById(R.id.captureButton);
        selectImageButton.setOnClickListener(v -> openGallery());

        imageView=findViewById(R.id.imageView);

        Button uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(v -> uploadImage());



     }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the gallery.
                openGallery();
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user).
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);

        }
    }

    private void uploadImage() {
        if (selectedImageUri != null) {
            ApiService apiService = ApiClient.create(BEARER_TOKEN);

            File imageFile = new File(getRealPathFromUri(selectedImageUri));

            RequestBody imageRequestBody = RequestBody.create(imageFile, MediaType.parse("image/*"));
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageRequestBody);

            Call<ResponseBody> call = apiService.uploadImage(BEARER_TOKEN,imagePart);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    Toast.makeText(CameraActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();

                    if (response.isSuccessful()) {
                        try {
                            Toast.makeText(CameraActivity.this, "Success" , Toast.LENGTH_SHORT).show();

                            // Parse the response JSON
                            String responseBody = response.body().string();
                            JSONObject json = new JSONObject(responseBody);

                            // Check if the response has "status" and it's true
                            if (json.has("status") && json.getBoolean("status")) {
                                JSONObject data = json.getJSONObject("data");

                                // Get the image filename and full URL
                                String imageFilename = data.getString("image");
                                String imageUrl = data.getString("full_url");

                                Log.e("response",""+imageFilename);
                                Log.e("response",""+imageUrl);


                            } else {
                                // Handle API error, if needed
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                        }
                    } else {
                        // Handle HTTP error
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(CameraActivity.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("response",""+t);
                }
            });
        }
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }

        return uri.getPath();
    }



}