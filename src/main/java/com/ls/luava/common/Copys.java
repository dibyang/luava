package com.ls.luava.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Copys {

  public static void copy(Object source, Object target) {
    copy(source, target, null);
  }

  public static void copy(Object source, Object target, NameMapping mapping) {
    if (source == null || target == null) {
      return;
    }

    if (mapping == null) {
      mapping = NameMapping.c();
    }

    if (target != null) {
      Field[] fields = target.getClass().getDeclaredFields();
      for (Field field : fields) {
        int mod = field.getModifiers();
        if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
          continue;
        }
        field.setAccessible(true);
        String name = mapping.getSource(field.getName());
        try {
          Field sField = source.getClass().getDeclaredField(name);
          sField.setAccessible(true);
          field.set(target, Types.cast(sField.get(source), field.getType()));

        }catch(NoSuchFieldException e){
          //igorne NoSuchFieldException
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
    }

  }
}
