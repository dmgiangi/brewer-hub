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
public class BeerReference {
    @SerializedName("id")
    private Integer id = null;

    @SerializedName("name")
    private String name = null;

    @Override
    public String toString() {

        return "Class Beer: {\n" +
                "    id: " + Indent.toIndentedString(id) + "\n" +
                "    name: " + Indent.toIndentedString(name) + "\n" +
                "}";
    }
}
