
package uni.mobile.mobileapp.rest.googleJson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PanelizationSummary {

    @SerializedName("containsEpubBubbles")
    @Expose
    private Boolean containsEpubBubbles;
    @SerializedName("containsImageBubbles")
    @Expose
    private Boolean containsImageBubbles;

    public Boolean getContainsEpubBubbles() {
        return containsEpubBubbles;
    }

    public void setContainsEpubBubbles(Boolean containsEpubBubbles) {
        this.containsEpubBubbles = containsEpubBubbles;
    }

    public Boolean getContainsImageBubbles() {
        return containsImageBubbles;
    }

    public void setContainsImageBubbles(Boolean containsImageBubbles) {
        this.containsImageBubbles = containsImageBubbles;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("containsEpubBubbles", containsEpubBubbles).append("containsImageBubbles", containsImageBubbles).toString();
    }

}
