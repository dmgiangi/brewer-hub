package com.github.dmgiangi.brewerhub.models.entity;

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
public class Hop {
  @SerializedName("name")
  private String name = null;

  @SerializedName("amount")
  private Weight weight = null;

  @SerializedName("add")
  private String add = null;

  @SerializedName("attribute")
  private String attribute = null;

  @Override
  public String toString() {
    return "Class Hop: {\n" +
            "    name: " + Indent.toIndentedString(name) + "\n" +
            "    amount: " + Indent.toIndentedString(weight) + "\n" +
            "    add: " + Indent.toIndentedString(add) + "\n" +
            "    attribute: " + Indent.toIndentedString(attribute) + "\n" +
            "}";
  }
}
