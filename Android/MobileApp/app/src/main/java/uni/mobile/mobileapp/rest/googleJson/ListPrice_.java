
package uni.mobile.mobileapp.rest.googleJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ListPrice_ {

    @SerializedName("amountInMicros")
    @Expose
    private Integer amountInMicros;
    @SerializedName("currencyCode")
    @Expose
    private String currencyCode;

    public Integer getAmountInMicros() {
        return amountInMicros;
    }

    public void setAmountInMicros(Integer amountInMicros) {
        this.amountInMicros = amountInMicros;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("amountInMicros", amountInMicros).append("currencyCode", currencyCode).toString();
    }

}
