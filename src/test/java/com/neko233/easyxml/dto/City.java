package com.neko233.easyxml.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "city")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class City implements Serializable {

    @XmlAttribute(name = "cityId")
    private Integer cityId;

    @XmlAttribute(name = "cityId2")
    private Integer cityId2;

    @XmlAttribute(name = "isMainLand")
    private Boolean isMainLand;

    @XmlAttribute(name = "d2")
    private String cityName;

    @XmlAttribute(name = "d3")
    private String cityCode;

    @XmlAttribute(name = "d4")
    private String province;


}
