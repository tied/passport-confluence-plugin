package com.inversoft.passport.confluence.impl;

import javax.inject.Inject;
import javax.inject.Named;

import com.atlassian.sal.api.ApplicationProperties;
import com.inversoft.passport.confluence.api.MyPluginComponent;

@Named("myPluginComponent")
public class MyPluginComponentImpl implements MyPluginComponent {
  private final ApplicationProperties applicationProperties;

  @Inject
  public MyPluginComponentImpl(final ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  public String getName() {
    if (null != applicationProperties) {
      return "myComponent:" + applicationProperties.getDisplayName();
    }

    return "myComponent";
  }
}