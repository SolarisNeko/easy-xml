package com.neko233.easyxml.data;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author SolarisNeko on 2023-01-01
 **/
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllXml {


    @XmlElement(name = "demo")
    private Demo demo;

    @XmlElement(name = "cityRoot")
    private CityRoot city;


}
