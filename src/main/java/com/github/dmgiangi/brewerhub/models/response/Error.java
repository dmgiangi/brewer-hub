package com.github.dmgiangi.brewerhub.models.response;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@EqualsAndHashCode
public class Error {
    @SerializedName("message")
    private String message;

    @SerializedName("status-code")
    private String statusCode;
}
