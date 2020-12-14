package com.example.donation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class Account_NGO extends Fragment {
    LocationManager locationManager;
    LocationListener locationListener;
    boolean userLocationPermission = false;
    public static String address = "Could not find address";
    EditText userAddress;
    ImageView imageView;
    Switch locationSwitch;
    Button photoButton;
    Button yourPost;
    TextView name;
    TextView email;
    TextView phone;
    TextView about;
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PHOTO = 100;
    byte []image;
    Button update;
    DB_handling db;
    SQLiteDatabase db1;
    String user_email;
    Button logout;
    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View account_ngo = inflater.inflate(R.layout.account_ngo, container, false);
        userAddress=account_ngo.findViewById(R.id.userAddress);
        imageView = (ImageView) account_ngo.findViewById(R.id.userPhoto);
        photoButton = (Button) account_ngo.findViewById(R.id.cameraButton);
        name=(TextView)account_ngo.findViewById(R.id.name);
        email=(TextView)account_ngo.findViewById(R.id.email);
        phone=(TextView)account_ngo.findViewById(R.id.phonenumber);
        about=(TextView)account_ngo.findViewById(R.id.about_Us);
        locationSwitch=(Switch) account_ngo.findViewById(R.id.locationSwitchId);
        update=(Button) account_ngo.findViewById(R.id.updateButton);
        yourPost=(Button) account_ngo.findViewById(R.id.yourPostButton);
        logout=(Button) account_ngo.findViewById(R.id.logoutButton);



        sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        user_email = sharedPreferences.getString("EMAIL", "");
        final String user_email_to_compare='"'+user_email+'"';
        db=new DB_handling(getContext());
        db1=db.getReadableDatabase();
        Cursor res =  db1.rawQuery( "select * from ngo where email_id="+user_email_to_compare, null );
        res.moveToFirst();
        while(!res.isAfterLast()){
            name.setText(res.getString(2));
            email.setText(res.getString(1));
            phone.setText(res.getString(6));
            about.setText(res.getString(5));
            try {
                userAddress.setText(res.getString(3));
            }
            catch (Exception e){
                userAddress.setText("");
            }try {

                Bitmap bmp = BitmapFactory.decodeByteArray(res.getBlob(8), 0, res.getBlob(8).length);
                imageView.setImageBitmap(bmp);
            }
            catch (Exception e){
                imageView.setImageResource(R.drawable.profile_pic_dummy);
            }


            //
            res.moveToNext();
        }
        initializeAllButtons();
        return account_ngo;
    }



    public void initializeAllButtons(){
        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getContext(),"On",Toast.LENGTH_SHORT).show();
                    Log.i("LOCATIONbutton","preseddddddddddddd");
                    setLocation(true);
                    locationSwitch.setChecked(false);

                }
                else{
                    Toast.makeText(getContext(),"Off",Toast.LENGTH_SHORT).show();

                }
            }
        });
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("button","preseddddddddddddd");


                // AlertDialog Builder class
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(getContext());

                // Set the message show for the Alert time
                builder.setMessage("Do you want to use camera ?");

                // Set Alert Title
                builder.setTitle("Camera Permission needed !");

                // Set Cancelable false
                // for when the user clicks on the outside
                // the Dialog Box then it will remain show
                builder.setCancelable(false);

                // Set the positive button with yes name
                // OnClickListener method is use of
                // DialogInterface interface.

                builder
                        .setPositiveButton(
                                "Yes",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {

                                        // When the user click yes button
                                        // then app will close

                                        //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                        //startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                        //onBackPressed();
                                        selectphoto();


                                    }
                                });

                // Set the Negative button with No name
                // OnClickListener method is use
                // of DialogInterface interface.
                builder
                        .setNegativeButton(
                                "No",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {

                                        // If user click no
                                        // then dialog box is canceled.

                                        dialog.cancel();
                                    }
                                });

                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();

                // Show the Alert Dialog box
                alertDialog.show();



            }
        });

        update.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          String updatedAddress=userAddress.getText().toString();
                                          if(db.updateNGOAddress(user_email,updatedAddress)){
                                              Toast.makeText(getContext(),"updated",Toast.LENGTH_SHORT).show();
                                          };
                                      }
                                  }
        );
        yourPost.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {

                                          Intent intent = new Intent(getContext(), yourPost.class);
                                          startActivity(intent);
                                          //finish();

                                      };

                                  }
        );

        logout.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          sharedPreferences.edit().clear().commit();
                                          Intent intent = new Intent(getContext(), MainActivity.class);
                                          startActivity(intent);
                                          //finish();

                                      };

                                  }
        );

    }
    public void selectphoto(){
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(getContext());

        // Set the message show for the Alert time
        builder.setMessage("Select the option");

        // Set Alert Title
        builder.setTitle("");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Gallery",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close

                                //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                //startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                //onBackPressed();
                                try{
                                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                    photoPickerIntent.setType("image/*");
                                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }



                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "Camera",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                try{

                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }

    }



    public void startListening() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        }

    }

    public  void  updateLocationInfo(Location location) {

        int locationInfo = Log.i("LocationInfo", location.toString());


        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {



            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddresses != null && listAddresses.size() > 0 ) {



                address = "";

                if (listAddresses.get(0).getSubThoroughfare() != null) {

                    address += listAddresses.get(0).getSubThoroughfare() + " ,";

                }

                if (listAddresses.get(0).getThoroughfare() != null) {

                    address += listAddresses.get(0).getThoroughfare() + ",";

                }

                if (listAddresses.get(0).getLocality() != null) {

                    address += listAddresses.get(0).getLocality() + ",";

                }

                if (listAddresses.get(0).getPostalCode() != null) {

                    address += listAddresses.get(0).getPostalCode() + ",";

                }

                if (listAddresses.get(0).getCountryName() != null) {

                    address += listAddresses.get(0).getCountryName() + "";

                }

            }
            Log.i("PlaceInfo", address);
            userAddress.setText(address);


        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
       // super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Log.i("requestcode",String.valueOf(requestCode));
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Log.i("camera","gallery module entered");
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContext().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageBitmap(yourSelectedImage);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                    image=outputStream.toByteArray();
                    if(db.updateNgoPhoto(user_email,image)){
                        Toast.makeText(getContext(),"imageupdated",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CAMERA_REQUEST:
                if(resultCode == RESULT_OK){
                    Log.i("camera","camera module entered");
                    //Bitmap photo = (Bitmap) data.getExtras().get("data");
                    /**Uri selectedImage = imageReturnedIntent.getData();
                     InputStream imageStream = null;
                     try {
                     imageStream = getContext().getContentResolver().openInputStream(selectedImage);
                     } catch (FileNotFoundException e) {
                     e.printStackTrace();
                     }**/
                    Bitmap yourSelectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    imageView.setImageBitmap(yourSelectedImage);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                    image=outputStream.toByteArray();
                    if(db.updateNgoPhoto(user_email,image)){
                        Toast.makeText(getContext(),"imageupdated",Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }


    }


    public void setLocation(boolean userLocationPermission){

        if(userLocationPermission){
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    //updateLocationInfo(location);

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };


            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {

                    updateLocationInfo(location);

                }

            }
        }
        else{
            address="";
        }
        userAddress.setText(address);
    }
}
