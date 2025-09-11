package com.tenpay.injector;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.util.NoSuchElementException;

/**
 * Base binding class for TInjector framework.
 * this class will bind the View variable to the real id by reflect.
 * delanding
 * */
public abstract class BaseBinding {
  private Activity mActivity;

  public BaseBinding(Activity activity) {
    this.mActivity = activity;
  }

  /**binding the filed name to the resource id.
   * @param fildName filed name in activity etc.
   * @param rely the relative root view which contains the id in include mode in layout file.
   * @param visible visible setting for the view.
   * */
  protected void injector(String fildName, int id, int visible, int[] rely) {
    Class cls = mActivity.getClass();
    try {
      Field filed = cls.getDeclaredField(fildName);
      filed.setAccessible(true);
      if (rely.length > 0) {
        View parent = mActivity.findViewById(rely[0]);
        for (int i = 1; i < rely.length; i++) {
          if (parent == null) {
            throw new NoSuchElementException("no id found: " + rely[i - 1]);
          }
          parent = parent.findViewById(rely[i]);
        }
        if (parent == null) {
          throw new NoSuchElementException("no id found: " + rely[rely.length - 1]);
        }
        View view = parent.findViewById(id);
        if (visible >= 0 && view.getVisibility() != visible) {
          view.setVisibility(visible);
        }
        filed.set(mActivity, view);
      } else {
        View view = mActivity.findViewById(id);
        if (visible >= 0 && view.getVisibility() != visible) {
          view.setVisibility(visible);
        }
        filed.set(mActivity, view);
      }
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  protected void injector(String fildName, int id, int visiable) {
    injector(fildName, id, visiable, new int[]{});
  }

  protected void injector(String fildName, int id) {
    injector(fildName, id, -1, new int[]{});
  }
}
