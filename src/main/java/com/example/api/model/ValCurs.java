package com.example.api.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Setter;

import java.util.List;

@Setter
@XmlRootElement(name = "ValCurs")
public class ValCurs {

    private List<Valute> valutes;

    @XmlElement(name = "Valute")
    public List<Valute> getValutes() {
        return valutes;
    }

}
