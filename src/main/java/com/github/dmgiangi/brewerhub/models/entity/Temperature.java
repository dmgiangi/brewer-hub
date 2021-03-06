package com.github.dmgiangi.brewerhub.models.entity;

import com.github.dmgiangi.brewerhub.utilities.Indent;
import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

// TODO write javadoc for this class
@EqualsAndHashCode
public class Temperature {
  @Getter
  @Accessors(chain = true)
  @SerializedName("value")
  private Float value = null;

  @Getter
  @Accessors(chain = true)
  @SerializedName("unit")
  private TemperatureUnits unit = TemperatureUnits.CELSIUS;

  public Temperature setValue(Float value) { this.value = value; return this;}

  public Temperature setUnit(TemperatureUnits unit) {
    if(this.value != null)
      this.setValue(TemperatureUnits.getConvertedValue(this.value, this.unit, unit));
    this.unit = unit;
    return this;
  }

  @Override
  public String toString() {
    return "Class Temperature: {\n" +
            "    value: " + Indent.toIndentedString(value) + "\n" +
            "    unit: " + Indent.toIndentedString(unit) + "\n" +
            "}";
  }
}
