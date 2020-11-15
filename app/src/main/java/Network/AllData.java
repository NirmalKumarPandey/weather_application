package Network;

import Model.Nirmal;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface AllData
{
       @GET("weather?&APPID=2afab23e24bed941e9664f65604e9d8f")
       @Streaming
       Call<Nirmal> getWeatherReport(@Query("q") String name);
}
