/*
 * Copyright (c) 2016, Inversoft Inc., All Rights Reserved
 */
package com.inversoft.passport.confluence;

/**
 * @author Derek Klatt
 */

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserManager;


@Path("/")
public class ConfigResource {
  private final PluginSettingsFactory pluginSettingsFactory;

  private final TransactionTemplate transactionTemplate;

  private final UserManager userManager;

  @Inject
  public ConfigResource(UserManager userManager, PluginSettingsFactory pluginSettingsFactory,
                        TransactionTemplate transactionTemplate) {
    this.userManager = userManager;
    this.pluginSettingsFactory = pluginSettingsFactory;
    this.transactionTemplate = transactionTemplate;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response get(@Context HttpServletRequest request) {
    String username = userManager.getRemoteUsername(request);
    if (username == null || !userManager.isSystemAdmin(username)) {
      return Response.status(Status.UNAUTHORIZED).build();
    }

    return Response.ok(transactionTemplate.execute(new TransactionCallback() {
      public Object doInTransaction() {
        PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        Config config = new Config();
        config.setPassportFrontend((String) settings.get(Config.class.getName() + ".passportFrontend"));
        config.setPassportBackend((String) settings.get(Config.class.getName() + ".passportBackend"));
        config.setClientId((String) settings.get(Config.class.getName() + ".clientId"));
        config.setClientSecret((String) settings.get(Config.class.getName() + ".clientSecret"));
        config.setApiKey((String) settings.get(Config.class.getName() + ".apiKey"));

        return config;
      }
    })).build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public Response put(final Config config, @Context HttpServletRequest request) {
    String username = userManager.getRemoteUsername(request);
    if (username == null || !userManager.isSystemAdmin(username)) {
      return Response.status(Status.UNAUTHORIZED).build();
    }

    transactionTemplate.execute(new TransactionCallback() {
      public Object doInTransaction() {
        PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
        pluginSettings.put(Config.class.getName() + ".apiKey", config.getApiKey());
        pluginSettings.put(Config.class.getName() + ".clientId", config.getClientId());
        pluginSettings.put(Config.class.getName() + ".clientSecret", config.getClientSecret());
        pluginSettings.put(Config.class.getName() + ".passportBackend", config.getPassportBackend());
        pluginSettings.put(Config.class.getName() + ".passportFrontend", config.getPassportFrontend());
        return null;
      }
    });
    return Response.noContent().build();
  }


  @XmlRootElement
  @XmlAccessorType(XmlAccessType.FIELD)
  public static final class Config {
    @XmlElement
    private String apiKey;

    @XmlElement
    private String clientId;

    @XmlElement
    private String clientSecret;

    @XmlElement
    private String passportBackend;

    @XmlElement
    private String passportFrontend;

    public String getApiKey() {
      return apiKey;
    }

    public void setApiKey(String apiKey) {
      this.apiKey = apiKey;
    }

    public String getClientId() {
      return clientId;
    }

    public void setClientId(String clientId) {
      this.clientId = clientId;
    }

    public String getClientSecret() {
      return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
      this.clientSecret = clientSecret;
    }

    public String getPassportBackend() {
      return passportBackend;
    }

    public void setPassportBackend(String passportBackend) {
      this.passportBackend = passportBackend;
    }

    public String getPassportFrontend() {
      return passportFrontend;
    }

    public void setPassportFrontend(String passportFrontend) {
      this.passportFrontend = passportFrontend;
    }
  }
}
