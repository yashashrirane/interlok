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

package com.adaptris.core;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Not used.
 *
 * <p>
 * In the adapter configuration file this class is aliased as <b>empty-file-name-creator</b> which is the preferred alternative to the
 * fully qualified classname when building your configuration.
 * </p>
 */
@XStreamAlias("empty-file-name-creator")
public class EmptyFileNameCreator implements FileNameCreator {

  /**
   * <p>
   * Returns an empty <code>String</code>.
   * </p>
   *
   * @see com.adaptris.core.FileNameCreator
   *      #createName(com.adaptris.core.AdaptrisMessage)
   */
  public String createName(AdaptrisMessage msg) {
    return "";
  }
}