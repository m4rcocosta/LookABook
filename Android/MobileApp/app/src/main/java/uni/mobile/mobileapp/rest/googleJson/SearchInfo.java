
package uni.mobile.mobileapp.rest.googleJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SearchInfo {

    @SerializedName("textSnippet")
    @Expose
    private String textSnippet;

    public String getTextSnippet() {
        return textSnippet;
    }

    public void setTextSnippet(String textSnippet) {
        this.textSnippet = textSnippet;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("textSnippet", textSnippet).toString();
    }

}
