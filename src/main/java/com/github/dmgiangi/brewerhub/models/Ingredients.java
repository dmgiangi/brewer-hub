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
//TODO write JAVADOC for this class
public class Ingredients {
  @SerializedName("malt")
  private MaltList malts = null;

  @SerializedName("hops")
  private HopList hops = null;

  @SerializedName("yeast")
  private String yeast = null;

  @Override
  public String toString() {
    return "Class Ingredients: {\n" +
            "    malts: " + Indent.toIndentedString(malts) + "\n" +
            "    hops: " + Indent.toIndentedString(hops) + "\n" +
            "    yeast: " + Indent.toIndentedString(yeast) + "\n" +
            "}";
  }
}
