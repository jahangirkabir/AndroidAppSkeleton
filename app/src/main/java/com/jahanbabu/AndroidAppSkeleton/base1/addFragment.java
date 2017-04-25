package com.jahanbabu.AndroidAppSkeleton.base1;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.jahanbabu.AndroidAppSkeleton.R;
import com.jahanbabu.AndroidAppSkeleton.Utils.MultipartRequest;
import com.jahanbabu.AndroidAppSkeleton.Utils.URLs;
import com.mlsdev.rximagepicker.RxImageConverters;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class addFragment extends Fragment {

    private static Context mContext;
    Spinner spinner, spinner2;
    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;
    ImageView displayImageView, cameraImageView, galleryImageView;
    ProgressDialog pd;
    private Spinner from_actv;
    Spinner conditionSpinner;
    Button cancelButton, reportButton;
    Spinner fromAutoComText;
    AutoCompleteTextView toAutoComText;
    EditText remarksEditText;
    View rootview;
    String lat = "23.816012", lon = "90.366080";
    String remarks = "", reportImagePath = "", reportImageName = "";
    Uri reportImageUri;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 23;
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
    private byte[] multipartBody;
    private FirebaseAnalytics mFirebaseAnalytics;
    public static addFragment newInstance(Context context) {
        addFragment f = new addFragment();
        Bundle args = new Bundle();
        args.putString("index", context.toString());
        f.setArguments(args);
        mContext = context;
        return f;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (menu.findItem(R.id.action_settings) != null) {
            menu.findItem(R.id.action_settings).setVisible(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_report, menu);
    }

    public void showDisplayImage(Bitmap bitmap){
        displayImageView.setImageBitmap(bitmap);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation anim;
        if (enter) {
            anim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
        } else {
            anim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
        }

        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) { }

            public void onAnimationRepeat(Animation animation) { }

            public void onAnimationStart(Animation animation) { }
        });

        return anim;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_add, container, false);
        onCreate(savedInstanceState);

        cameraImageView = (ImageView) rootview.findViewById(R.id.cameraImageView);
        galleryImageView = (ImageView) rootview.findViewById(R.id.galleryImageView);
        displayImageView = (ImageView) rootview.findViewById(R.id.displayImageView);

        adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

        };

        cameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    } else {
                        openCameraWithRX();
                    }
                } else {
                    openCameraWithRX();
                }
//                openCameraWithRX();
            }
        });

        galleryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalleryWithRX();
            }
        });
        return rootview;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCameraWithRX();
                } else {
                    Toast.makeText(mContext, "Please allow camera permission to add photo on report.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void openCameraWithRX(){

        RxImagePicker.with(mContext).requestImage(Sources.CAMERA).subscribe(new Action1<Uri>() {
            @Override
            public void call(Uri uri) {
                //Get image by uri using one of image loading libraries. I use Glide in sample app.
                reportImagePath = getRealPathFromURI(mContext, uri);
                reportImageName = reportImagePath.substring(reportImagePath.lastIndexOf("/")+1);
                reportImageUri = Uri.parse(reportImagePath);
                displayImageView.setImageURI(reportImageUri);
                Log.d("Image uri-Name", uri + "  --  " + reportImagePath + " -- " + reportImageName);
//                Picasso.with(mContext).load(uri).into(displayImageView);
            }
        });

    }

    public void openGalleryWithRX(){

        RxImagePicker.with(mContext)
                .requestImage(Sources.GALLERY)
                .flatMap(new Func1<Uri, Observable<? extends String>>() {
                    @Override
                    public Observable<? extends String> call(Uri uri) {
                        return RxImageConverters.uriToFullPath(mContext, uri);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String path) {
                        // Do something with your image path
                        // Ex. /storage/emulated/0/DCIM/Camera/20160701_113408.jpg
                        reportImagePath = path;
                        reportImageName = path.substring(path.lastIndexOf("/")+1);
                        Uri uri = Uri.fromFile(new File(path));
                        displayImageView.setImageURI(uri);
                        reportImageUri = uri;
                        Log.d("Image uri-Name", uri+"  --  "+reportImageName);
                    }
                });
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // get the button view
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);

//        // set a onclick listener for when the button gets clicked
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            // Start new list activity
//            public void onClick(View v) {
//                backToDrawerActivity();
//            }
//        });
//

    }

    private void backToDrawerActivity(){

        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
    }

    private void uploadReportData(String condition, String remarks, Uri reportImageUri, String reportImageName, String lat, String lon) {
        Log.i("addFragment", reportImageName + " -- " + reportImageUri);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            if (reportImageName.length() > 4){
                byte[] fileData = getFileDataFromDrawable(mContext, reportImageUri);
                buildPart(dos, fileData, reportImageName);
            }


            buildTextPart(dos, "condition", condition);
            buildTextPart(dos, "comment", remarks);
            buildTextPart(dos, "latitude", lat);
            buildTextPart(dos, "longitude", lon);
            // send multipart form data necesssary after file data
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // pass to multipart body
            multipartBody = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = URLs.HOMEURL + "/api/comments.php";
        MultipartRequest multipartRequest = new MultipartRequest(url, null, mimeType, multipartBody, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.e("NetworkResponse ", response.toString());
                pd.dismiss();
                InsertSuccessMethod();
//                backToDrawerActivity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Network ERROR ", error.toString());
                pd.dismiss();
//                Toast.makeText(mContext, "Upload failed!\r\n" + error.toString(), Toast.LENGTH_SHORT).show();
                InsertFailMethod();
            }
        });
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(multipartRequest);
    }

    public String trimMessage(String json, String key){
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    private void buildPart(DataOutputStream dataOutputStream, byte[] fileData, String fileName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\""
                + fileName + "\"" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        // read file and write it into form...
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
        dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(parameterValue + lineEnd);
    }

    private byte[] getFileDataFromDrawable(Context context, Uri reportImageUri) {
        Bitmap bitmap = decodeSampledBitmapFromResource(reportImageUri, 500, 500);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap decodeSampledBitmapFromResource(Uri uri, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.getPath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(uri.getPath(), options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    // Decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 20000;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if (exception.getCause() != null) {
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }


    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }

    private void InsertSuccessMethod() {
//        ContextThemeWrapper ctw = new ContextThemeWrapper(mContext, R.style.MyAlertDialogStyle);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mContext).setMessage("Your Update has been successfully sent");

        final AlertDialog alert = dialog.create();
        alert.show();

// Hide after some seconds
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();
                    backToDrawerActivity();
                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
                backToDrawerActivity();
            }
        });

        handler.postDelayed(runnable, 1500);


    }

    private void InsertFailMethod() {
        Toast.makeText(mContext, "Insertion Failed", Toast.LENGTH_LONG).show();
    }
}
