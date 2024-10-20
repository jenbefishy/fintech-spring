package com.example.api.model;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Valute {

    @XmlAttribute(name = "ID")
    private String id;

    @XmlElement(name = "NumCode")
    private String numCode;

    @XmlElement(name = "CharCode")
    private String charCode;

    @XmlElement(name = "Nominal")
    private int nominal;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "Value")
    private String value;

    @XmlElement(name = "VunitRate")
    private String vunitRate;

    public double getDoubleValue() {
        if (value == null || value.isEmpty()) {
            return 0;
        }
        return Double.parseDouble(value.replace(",", "."));
    }
}