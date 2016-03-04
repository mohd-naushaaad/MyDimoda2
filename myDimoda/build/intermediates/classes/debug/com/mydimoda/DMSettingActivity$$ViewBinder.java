// Generated code from Butter Knife. Do not modify!
package com.mydimoda;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class DMSettingActivity$$ViewBinder<T extends com.mydimoda.DMSettingActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427521, "field 'mNotificationRL'");
    target.mNotificationRL = finder.castView(view, 2131427521, "field 'mNotificationRL'");
    view = finder.findRequiredView(source, 2131427523, "field 'mNotificationToggleBtn'");
    target.mNotificationToggleBtn = finder.castView(view, 2131427523, "field 'mNotificationToggleBtn'");
  }

  @Override public void unbind(T target) {
    target.mNotificationRL = null;
    target.mNotificationToggleBtn = null;
  }
}
