package com.nirmal.weitherreport;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.io.IOException;
import java.util.List;

import Model.Main;
import Model.Nirmal;
import Network.AllData;
import Network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nirmal.weitherreport.R.*;

public class MainActivity extends AppCompatActivity
{
    private TextView mLocation,mTemperature;
    private EditText mName;
    private FusedLocationProviderClient fusedLocationProviderClient;
     private Geocoder geocoder;
     private BottomAppBar bottomAppBar;
     private String address,address1;
    private static final String tag="nirmal";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        mLocation=findViewById(id.your_location);
        mTemperature=findViewById(id.temperature);
        mName=findViewById(id.name);
        geocoder=new Geocoder(this);
        bottomAppBar=findViewById(id.bottomAppBar);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if(getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==
                    PackageManager.PERMISSION_GRANTED)
            {
               fusedLocationProviderClient.getLastLocation()
                       .addOnSuccessListener(new OnSuccessListener<Location>() {
                           @SuppressLint("SetTextI18n")
                           @Override
                           public void onSuccess(Location location)
                           {
                               if(location!=null) {
                                   double lat = location.getLatitude();
                                   double lon = location.getLongitude();
                                   try
                                   {
                                       List<Address> addresses=geocoder.getFromLocation(lat,lon,1);
                                        address=addresses.get(0).getSubAdminArea();
                                        address1=addresses.get(0).getAddressLine(0);
                                        mLocation.setText(address1);

                                   } catch (IOException e) {
                                       e.printStackTrace();
                                   }

                               }
                               else
                               {
                                   Toast.makeText(MainActivity.this, "Please Enable Your Internet Connection", Toast.LENGTH_SHORT).show();
                               }

                           }
                       });

            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                Toast.makeText(getApplicationContext(),"Requset for the permission",Toast.LENGTH_LONG).show();
            }

        }
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId())
                {
                    case  R.id.current :
                        firstRecyclerView();
                        break;
                    case  R.id.date :
                        Intent intent1=new Intent(MainActivity.this,SecondActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.setting :
                        Intent intent2=new Intent(MainActivity.this,SecondActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.report :
                        AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                        alert.setMessage("Hello :"+mName.getText().toString().trim()+"\n\nTemperature : "+mTemperature.getText().toString().trim()+"\n\nLocation:"+address);
                        alert.setCancelable(true);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                finish();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                mLocation.setText(mName.getText().toString().trim());
                            }
                        });
                        AlertDialog dialog=alert.create();
                        dialog.show();
                        break;
                }
                return true;
            }
        });


    }

    private void firstRecyclerView()
    {

        AllData allData = RetrofitInstance.getService();
        Call<Nirmal> call = allData.getWeatherReport(address);
        call.enqueue(new Callback<Nirmal>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Nirmal> call, Response<Nirmal> response)
            {
                Nirmal nirmal=response.body();
                Main main= nirmal.getMain();
                if(main!=null)
                {
                  //  Double min=Double.valueOf((main.getTempMin())-273.15);
                    double value= (main.getTemp()) - 273.15;
                    double max= (main.getTempMax()) - 273.15;
                    double min= (main.getTempMin()) - 273.15;
                    mTemperature.setText("Hello "+mName.getText().toString().trim()+"\n"+"Temperature : "+ value +" °C"+"\nHumidity :"+main.getHumidity().toString()+"\nPressure :"+main.getPressure()+
                            "\nMax Tem :"+max+" °C"+"\nMin Tem :"+min+" °C");
                }
                else
                {
                    mTemperature.setText("Error");
                }
            }

            @Override
            public void onFailure(Call<Nirmal> call, Throwable t)
            {
                Toast.makeText(MainActivity.this, "Fail.......Fail......", Toast.LENGTH_LONG).show();
            }

        });
    }



}