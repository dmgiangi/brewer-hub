package com.github.dmgiangi.brewerhub.models;

import com.github.dmgiangi.brewerhub.utilities.Indent;
import com.google.gson.annotations.SerializedName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

// TODO write javadoc for this class
@EqualsAndHashCode
public class Weight {
  @Getter
  @Accessors(chain = true)
  @SerializedName("value")
  private Float value = null;

  @Getter
  @Accessors(chain = true)
  @SerializedName("unit")
  private WeightUnits unit = WeightUnits.KILOGRAMS;

  public Weight setValue(Float value) {
    this.value = value;
    return this;
  }

  //TODO implements this method
  /*public Amount setValueAndUnit(Float newValue, WeightUnits newUnit){}*/

  public Weight setUnit(WeightUnits newUnit) {
    this.setValue(WeightUnits.getConvertedValue(this.value, this.unit, newUnit));
    this.unit = newUnit;
    return this;
  }

  @Override
  public String toString() {

    return "Class Weight: {\n" +
            "    value: " + Indent.toIndentedString(value) + "\n" +
            "    unit: " + Indent.toIndentedString(unit) + "\n" +
            "}";
  }
}
