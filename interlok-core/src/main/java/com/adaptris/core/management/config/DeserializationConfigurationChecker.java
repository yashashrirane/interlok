package com.adaptris.core.management.config;

import com.adaptris.core.Adapter;
import com.adaptris.core.DefaultMarshaller;
import com.adaptris.core.management.BootstrapProperties;
import com.adaptris.core.management.UnifiedBootstrap;
import com.adaptris.core.util.LifecycleHelper;

public class DeserializationConfigurationChecker implements ConfigurationChecker {
  
  private static final String FRIENDLY_NAME = "Configuration loading test";
  
  @Override
  public ConfigurationCheckReport performConfigCheck(BootstrapProperties bootProperties, UnifiedBootstrap bootstrap) {
    ConfigurationCheckReport report = new ConfigurationCheckReport();
    report.setCheckName(this.getFriendlyName());
    
    // This seems a bit cheaty, but we're going to exit anyway, so
    // calling prepare probably makes no difference.
    try {
      Adapter clonedAdapter = (Adapter) DefaultMarshaller.getDefaultMarshaller().unmarshal(bootstrap.createAdapter().getConfiguration());
      LifecycleHelper.prepare(clonedAdapter);
      
    } catch (Exception ex) {
      report.getFailureExceptions().add(ex);
    }
    return report;
  }

  @Override
  public String getFriendlyName() {
    return FRIENDLY_NAME;
  }

}
