package com.neko233.easyxml.dto;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * @desc 城市列表
 */
@XmlRootElement(name = "cityRoot")  // 声明为xml的根元素c
@XmlAccessorType(XmlAccessType.FIELD)  //通过字段来访问
@Data
public class CityRoot implements Serializable {

    @XmlAttribute(name = "rootId")
    private Integer rootId;

    @XmlElement(name = "city")
    private List<City> cityList;

    @XmlElement(name = "any", defaultValue = "<any></any>")
    private List<City> any;


}
