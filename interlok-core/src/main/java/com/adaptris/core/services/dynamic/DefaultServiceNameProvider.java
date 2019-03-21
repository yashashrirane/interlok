/*
 * Copyright 2015 Adaptris Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package com.adaptris.core.services.dynamic;

import javax.validation.constraints.NotNull;
import com.adaptris.annotation.AutoPopulated;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.annotation.InputFieldHint;
import com.adaptris.annotation.Removal;
import com.adaptris.core.CoreException;
import com.adaptris.core.TradingRelationship;
import com.adaptris.core.util.Args;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <p>
 * Implementation of {@link ServiceNameProvider} which returns the passed
 * {@link TradingRelationship} source, destination and type separated by an (optional) configurable
 * character.
 * </p>
 * 
 * @config default-service-name-provider
 * @deprecated since 3.8.4 use {@link DynamicServiceExecutor} with a URL based
 *             {@link ServiceExtractor} instead.
 * 
 */
@Deprecated
@XStreamAlias("default-service-name-provider")
@DisplayOrder(order = {"separator"})
@Removal(version = "3.11.0")
public class DefaultServiceNameProvider extends ServiceNameProviderImp {

  @AutoPopulated
  @InputFieldHint(style = "BLANKABLE")
  @NotNull
  @InputFieldDefault(value = "-")
  private String separator;

  /**
   * <p>
   * Creates a new instance.  Default separator is "-".
   * </p>
   */
  public DefaultServiceNameProvider() {
    this.setSeparator("-");
  }

  @Override
  protected String retrieveName(TradingRelationship t) throws CoreException {
    Args.notNull(t, "relationship");
    StringBuffer result = new StringBuffer();
    result.append(t.getSource());
    result.append(this.getSeparator());
    result.append(t.getDestination());
    result.append(this.getSeparator());
    result.append(t.getType());

    return result.toString();
  }

  /**
   * <p>
   * Gets the separator used to delineate source, destination and type. May not
   * be null.
   * </p>
   *
   * @return the separator
   */
  public String getSeparator() {
    return separator;
  }

  /**
   * <p>
   * Sets the separator used to delineate source, destination and type. May not
   * be null.
   * </p>
   *
   * @param s separator, default is '-'
   */
  public void setSeparator(String s) {
    this.separator = Args.notNull(s, "separator");

  }
}
