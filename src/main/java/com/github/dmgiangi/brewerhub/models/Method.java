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
public class Method {
  @SerializedName("mash_temp")
  private MashTempList mashTempsList = null;

  @SerializedName("fermentation")
  private Fermentation fermentation = null;

  @SerializedName("twist")
  private String twist = null;

  @Override
  public String toString() {
    return "Class Method: {\n" +
            "    mashTemps: " + Indent.toIndentedString(mashTempsList) + "\n" +
            "    fermentation: " + Indent.toIndentedString(fermentation) + "\n" +
            "    twist: " + Indent.toIndentedString(twist) + "\n" +
            "}";
  }
}
