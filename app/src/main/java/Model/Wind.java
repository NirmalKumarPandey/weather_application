
package Model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Wind {

    @SerializedName("deg")
    private Long mDeg;
    @SerializedName("gust")
    private Double mGust;
    @SerializedName("speed")
    private Double mSpeed;

    public Long getDeg() {
        return mDeg;
    }

    public void setDeg(Long deg) {
        mDeg = deg;
    }

    public Double getGust() {
        return mGust;
    }

    public void setGust(Double gust) {
        mGust = gust;
    }

    public Double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(Double speed) {
        mSpeed = speed;
    }

}
