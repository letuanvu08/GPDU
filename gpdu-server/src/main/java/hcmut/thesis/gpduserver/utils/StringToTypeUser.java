package hcmut.thesis.gpduserver.utils;

import hcmut.thesis.gpduserver.constants.enumations.TypeUser;
import org.springframework.core.convert.converter.Converter;

public class StringToTypeUser implements Converter<String, TypeUser> {

  @Override
  public TypeUser convert(String s) {
    return TypeUser.valueOf(s);
  }
}
