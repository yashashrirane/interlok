/*
 * Copyright 2017 Adaptris Ltd.
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
package com.adaptris.core.services.exception;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.util.DocumentBuilderFactoryBuilder;
import com.adaptris.core.util.ExceptionHelper;
import com.adaptris.core.util.XmlHelper;
import com.adaptris.util.text.xml.DocumentMerge;
import com.adaptris.util.text.xml.ReplaceOriginal;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Use with {@link ExceptionReportService} to write the exception as part of an xml document.
 * 
 * 
 * @config exception-as-xml
 */
@XStreamAlias("exception-as-xml")
@DisplayOrder(order =
{
    "xmlEncoding", "exceptionGenerator", "documentMerge"
})
public class ExceptionAsXml implements ExceptionSerializer {
  protected transient Logger log = LoggerFactory.getLogger(this.getClass());

  @Valid
  private DocumentMerge documentMerge;
  @Valid
  private ExceptionReportGenerator exceptionGenerator;
  @InputFieldDefault(value = "UTF-8")
  private String xmlEncoding;
  @AdvancedConfig
  @Valid
  private DocumentBuilderFactoryBuilder xmlDocumentFactoryConfig;

  public ExceptionAsXml() {
  }

  public ExceptionAsXml withDocumentMerge(DocumentMerge docMerge) {
    setDocumentMerge(docMerge);
    return this;
  }

  public ExceptionAsXml withExceptionGenerator(ExceptionReportGenerator e) {
    setExceptionGenerator(e);
    return this;
  }

  public ExceptionAsXml withXmlEncoding(String e) {
    setXmlEncoding(e);
    return this;
  }

  public ExceptionAsXml withDocumentFactoryConfig(DocumentBuilderFactoryBuilder e) {
    setXmlDocumentFactoryConfig(e);
    return this;
  }

  @Override
  public void serialize(Exception exception, AdaptrisMessage msg) throws CoreException {
    try {
      Document newDoc = exceptionGenerator().create(exception);
      Document result = documentMerge().merge(XmlHelper.createDocument(msg, documentFactoryBuilder()), newDoc);
      String encoding = XmlHelper.getXmlEncoding(msg, getXmlEncoding());
      XmlHelper.writeXmlDocument(result, msg, encoding);
    }
    catch (Exception e) {
      throw ExceptionHelper.wrapServiceException(e);
    }
  }

  public DocumentMerge getDocumentMerge() {
    return documentMerge;
  }

  /**
   * Specify how to merge the exception into the AdaptrisMessage.
   *
   * @param m the merge implementation, if not specified defaults to {@link ReplaceOriginal}
   */
  public void setDocumentMerge(DocumentMerge m) {
    documentMerge = m;
  }

  DocumentMerge documentMerge() {
    return getDocumentMerge() != null ? getDocumentMerge() : new ReplaceOriginal();
  }

  public ExceptionReportGenerator getExceptionGenerator() {
    return exceptionGenerator;
  }

  /**
   * Specify how to create the XML document from the exception.
   *
   * @param generator the generator, if not specified defaults to {@link SimpleExceptionReport}.
   */
  public void setExceptionGenerator(ExceptionReportGenerator generator) {
    exceptionGenerator = generator;
  }

  ExceptionReportGenerator exceptionGenerator() {
    return getExceptionGenerator() != null ? getExceptionGenerator() : new SimpleExceptionReport();
  }

  public String getXmlEncoding() {
    return xmlEncoding;
  }

  /**
   * Set the encoding for the resulting XML document.
   *
   * @param encoding the encoding, default is UTF-8
   */
  public void setXmlEncoding(String encoding) {
    xmlEncoding = encoding;
  }

  public DocumentBuilderFactoryBuilder getXmlDocumentFactoryConfig() {
    return xmlDocumentFactoryConfig;
  }

  public void setXmlDocumentFactoryConfig(DocumentBuilderFactoryBuilder xml) {
    this.xmlDocumentFactoryConfig = xml;
  }

  DocumentBuilderFactoryBuilder documentFactoryBuilder() {
    return DocumentBuilderFactoryBuilder.newInstance(getXmlDocumentFactoryConfig());
  }

}