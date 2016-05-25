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

package com.adaptris.core.services.jmx;

import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.AutoPopulated;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.core.AdaptrisConnection;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.adaptris.core.jmx.JmxConnection;
import com.adaptris.core.util.Args;
import com.adaptris.core.util.ExceptionHelper;
import com.adaptris.core.util.LifecycleHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <p>
 * Allows you to make a remote call on a JMX operation.
 * </p>
 * <p>
 * You can set parameters for the call using {@link ValueTranslator}'s and also a single
 * {@link ValueTranslator} to help translate the result back into the Message.
 * </p>
 * <p>
 * If you do not wish to translate the result of the operation, simply omit the
 * "result-value-translator".
 * </p>
 * 
 * @since 3.0.3
 * 
 * @config jmx-operation-call-service
 * @since 3.0.3
 */
@XStreamAlias("jmx-operation-call-service")
@AdapterComponent
@ComponentProfile(summary = "Execute a JMX operation", tag = "service,jmx", recommended = {JmxConnection.class})
@DisplayOrder(order = {"objectName", "operationName", "operationParameters", "resultValueTranslator"})
public class JmxOperationCallService extends ServiceImp {
  
  @Valid
  @NotNull
  @AutoPopulated
  private AdaptrisConnection connection;

  /**
   * Should you want to translate the result of the operation back into the message, configure a single {@link ValueTranslator}.
   */
  @Valid
  private ValueTranslator resultValueTranslator;


  /**
   * The fully qualified object name string, pointing to the object containing your chosen operation.
   */
  @NotBlank
  private String objectName;
  
  /**
   * The name of the operation that belongs to the ObjectName specified by the jmx-service-url.
   */
  @NotBlank
  private String operationName;
  
  @Valid
  @NotNull
  @AutoPopulated
  private List<ValueTranslator> operationParameters;
  

  private transient JmxOperationInvoker invoker;

  public JmxOperationCallService() {
    this.setResultValueTranslator(null);
    this.setOperationParameters(new ArrayList<ValueTranslator>());
    setConnection(new JmxConnection());
    setInvoker(new JmxOperationInvoker<Object>());
  }

  @Override
  public void doService(AdaptrisMessage message) throws ServiceException {
    try {
      MBeanServerConnection mbeanConn = getConnection().retrieveConnection(JmxConnection.class).mbeanServerConnection();
      Object result = getInvoker().invoke(mbeanConn, getObjectName(), getOperationName(), parametersToArray(message),
          parametersToTypeArray(message));
      if(this.getResultValueTranslator() != null)
        this.getResultValueTranslator().setValue(message, result);
      
    } catch (Exception e) {
      throw ExceptionHelper.wrapServiceException(e);
    }
  }
  
  private Object[] parametersToArray(AdaptrisMessage message) throws CoreException {
    Object[] returnArray = new Object[this.getOperationParameters().size()];
    for(int count = 0; count < this.getOperationParameters().size(); count ++)
      returnArray[count] = this.getOperationParameters().get(count).getValue(message);
    
    return returnArray;
  }
  
  private String[] parametersToTypeArray(AdaptrisMessage message) {
    String[] returnArray = new String[this.getOperationParameters().size()];
    for(int count = 0; count < this.getOperationParameters().size(); count ++)
      returnArray[count] = this.getOperationParameters().get(count).getType();
    
    return returnArray;
  }

  @Override
  public void prepare() throws CoreException {
    getConnection().prepare();
  }

  @Override
  protected void initService() throws CoreException {
    LifecycleHelper.init(getConnection());
  }

  @Override
  protected void closeService() {
    LifecycleHelper.close(getConnection());
  }

  @Override
  public void stop() {
    super.stop();
    LifecycleHelper.stop(getConnection());
  }

  @Override
  public void start() throws CoreException {
    super.start();
    LifecycleHelper.start(getConnection());
  }


  public String getObjectName() {
    return objectName;
  }

  public void setObjectName(String objectName) {
    this.objectName = Args.notNull(objectName, "objectName");
  }

  public ValueTranslator getResultValueTranslator() {
    return resultValueTranslator;
  }

  public void setResultValueTranslator(ValueTranslator resultValueTranslator) {
    this.resultValueTranslator = resultValueTranslator;
  }

  public String getOperationName() {
    return operationName;
  }

  public void setOperationName(String operationName) {
    this.operationName = operationName;
  }

  public List<ValueTranslator> getOperationParameters() {
    return operationParameters;
  }

  public void setOperationParameters(List<ValueTranslator> parameters) {
    this.operationParameters = parameters;
  }

  /**
   * @return the connection
   */
  public AdaptrisConnection getConnection() {
    return connection;
  }

  /**
   * @param c the connection to set
   */
  public void setConnection(AdaptrisConnection c) {
    this.connection = Args.notNull(c, "connection");
  }

  /**
   * @return the invoker
   */
  private JmxOperationInvoker<Object> getInvoker() {
    return invoker;
  }

  /**
   * @param invoker the invoker to set
   */
  void setInvoker(JmxOperationInvoker<Object> invoker) {
    this.invoker = invoker;
  }

}
