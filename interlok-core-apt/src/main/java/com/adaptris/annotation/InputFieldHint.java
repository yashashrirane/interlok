/*
 * Copyright 2015 Adaptris Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adaptris.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An interface for making a "hint" for the UI about what type of input field this is.
 *
 * @author lchan
 * @since 3.0.2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InputFieldHint {
  /**
   * The style associated with this InputField.
   */
  String style() default "";

  /**
   * The base interface or class for the field value suggestions.
   */
  String ofType() default "";

  /**
   * The UI friendly name for the field; particularly enum's.
   */
  String friendly() default "";

  /**
   * Whether or not this field allows the new {@code %message{}} style expressions
   * 
   * @return true if enabled.
   * @since 3.6.2
   */
  boolean expression() default false;

  /**
   * Whether or not this field supports external resolution.
   * 
   * @return true if enabled.
   * @since 3.7.1
   */
  boolean external() default false;

}
