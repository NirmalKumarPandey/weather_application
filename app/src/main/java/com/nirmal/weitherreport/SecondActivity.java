package com.nirmal.weitherreport;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import Model.Main;
import Model.Nirmal;
import Network.AllData;
import Network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText mDate;
    private Spinner mCity;
    private Button bShow;
    private TextView mShowDate;
    private String date,city;
    private String town[]=null;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mDate=findViewById(R.id.enter_date);
        mCity=findViewById(R.id.spinner);
        bShow=findViewById(R.id.show);
        bShow.setOnClickListener(this);
        mDate.setOnClickListener(this);
        mShowDate=findViewById(R.id.show_date_city);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.spinner,android.R.layout.simple_dropdown_item_1line);
        mCity.setAdapter(adapter);
        mCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v)
    {
        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        if(v==mDate)
        {
            DatePickerDialog datePickerDialog=new DatePickerDialog(SecondActivity.this, new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day)
                {
                      month=month+1;
                      date=day+"/"+month+"/"+year;
                      mDate.setText(date);
                }
            },year,month,day);
            datePickerDialog.show();
        }

        if(v==bShow)
        {
            firstRecyclerView();
        }

    }
    private void firstRecyclerView()
    {

        AllData allData = RetrofitInstance.getService();
        Call<Nirmal> call = allData.getWeatherReport(city);
        call.enqueue(new Callback<Nirmal>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Nirmal> call, Response<Nirmal> response)
            {
                Nirmal nirmal=response.body();
                Main main= nirmal.getMain();
                if(main!=null)
                {
                    double value= (main.getTemp()) - 273.15;
                    double max= (main.getTempMax()) - 273.15;
                    double min= (main.getTempMin()) - 273.15;
                    mShowDate.setText("Hello "+mDate.getText().toString().trim()+"\n"+"Temperature : "+ value +" °C"+"\nHumidity :"+main.getHumidity().toString()+"\nPressure :"+main.getPressure()+
                            "\nMax Tem :"+max+" °C"+"\nMin Tem :"+min+" °C");
                }
                else
                {
                    mShowDate.setText("Error");
                }
            }

            @Override
            public void onFailure(Call<Nirmal> call, Throwable t)
            {
                Toast.makeText(SecondActivity.this, "Fail.......Fail......", Toast.LENGTH_LONG).show();
            }

        });
    }




}