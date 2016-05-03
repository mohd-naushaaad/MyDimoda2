// Generated code from Butter Knife. Do not modify!
package com.mydimoda;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class DMSettingActivity$$ViewBinder<T extends com.mydimoda.DMSettingActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558697, "field 'mNotificationRL'");
    target.mNotificationRL = finder.castView(view, 2131558697, "field 'mNotificationRL'");
    view = finder.findRequiredView(source, 2131558699, "field 'mNotificationToggleBtn'");
    target.mNotificationToggleBtn = finder.castView(view, 2131558699, "field 'mNotificationToggleBtn'");
    view = finder.findRequiredView(source, 2131558701, "field 'mStyleMeLAbel'");
    target.mStyleMeLAbel = finder.castView(view, 2131558701, "field 'mStyleMeLAbel'");
    view = finder.findRequiredView(source, 2131558702, "field 'mStylemePointTv'");
    target.mStylemePointTv = finder.castView(view, 2131558702, "field 'mStylemePointTv'");
  }

  @Override public void unbind(T target) {
    target.mNotificationRL = null;
    target.mNotificationToggleBtn = null;
    target.mStyleMeLAbel = null;
    target.mStylemePointTv = null;
  }
}
