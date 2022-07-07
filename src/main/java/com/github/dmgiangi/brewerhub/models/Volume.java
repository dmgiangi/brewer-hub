package com.github.dmgiangi.brewerhub.models;

import com.github.dmgiangi.brewerhub.utilities.Indent;
import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

// TODO write javadoc for this class
@EqualsAndHashCode
public class Volume {
    @Getter
    @Accessors(chain = true)
    @SerializedName("value")
    private Float value = null;

    @Getter
    @Accessors(chain = true)
    @SerializedName("unit")
    private VolumeUnits unit = VolumeUnits.LITRES;

    public Volume setValue(Float value) {
        this.value = value;
        return this;
    }

    //TODO implements this method
    /*public Volume setValueAndUnit(Float newValue, VolumeUnits toUnit) {}*/

    public Volume setUnit(VolumeUnits unit) {
        this.setValue(VolumeUnits.getConvertedValue(this.value, this.unit, unit));
        this.unit = unit;
        return this;
    }

    @Override
    public String toString() {
        return "Class Volume: {\n" +
                "    value: " + Indent.toIndentedString(value) + "\n" +
                "    unit: " + Indent.toIndentedString(unit) + "\n" +
                "}";
    }
}
