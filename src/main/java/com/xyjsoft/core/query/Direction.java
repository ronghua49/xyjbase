package com.xyjsoft.core.query;

import io.swagger.annotations.ApiModel;

@ApiModel(description="排序对象")
public enum Direction
{
  ASC, 

  DESC;

  public static Direction fromString(String value)
  {
    try {
      return valueOf(value.toUpperCase()); } catch (Exception e) {
    }
    return ASC;
  }
}