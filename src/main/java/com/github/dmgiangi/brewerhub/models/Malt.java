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
//TODO Write JAVADOC for this class
public class Malt {
  @SerializedName("name")
  private String name = null;

  @SerializedName("amount")
  private Weight weight = null;

  @Override
  public String toString() {

    return "Class Malt: {\n" +
            "    name: " + Indent.toIndentedString(name) + "\n" +
            "    amount: " + Indent.toIndentedString(weight) + "\n" +
            "}";
  }
}
