package com.github.dmgiangi.brewerhub.models;

import com.github.dmgiangi.brewerhub.utilities.Indent;
import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
//TOTO Write JAVADOC for this class
public class Fermentation {
    @SerializedName("temp")
    Temperature temperature = null;

    @Override
    public String toString() {
        return "Class Fermentation: {\n" +
                "    temp: " + Indent.toIndentedString(temperature) + "\n" +
                "}";
    }
}
