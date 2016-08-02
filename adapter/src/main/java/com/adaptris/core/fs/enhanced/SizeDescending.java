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

package com.adaptris.core.fs.enhanced;

import java.io.File;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Sort the list of files by their size in reverse order
 * 
 * @config fs-sort-size-descending
 * 
 * @author lchan
 * 
 */
@XStreamAlias("fs-sort-size-descending")
public class SizeDescending extends SafeFileSorter {

  @Override
  SortEntry wrap(File f) {
    return new SizeDesc(f);
  }

  private class SizeDesc extends SortEntry<SizeDesc> {
    private long val;

    private SizeDesc(File f) {
      super(f);
      val = f.length();
    }

    public int compareTo(SizeDesc o) {
      long theirs = o.val;
      return val < theirs ? 1 : val == theirs ? 0 : -1;
    }
  }
}
