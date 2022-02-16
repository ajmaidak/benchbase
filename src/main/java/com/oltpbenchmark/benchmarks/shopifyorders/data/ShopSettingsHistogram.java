/*
 * Copyright 2020 by OLTPBenchmark Project
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
 *
 */

package com.oltpbenchmark.benchmarks.shopifyorders.data;

import com.oltpbenchmark.util.Histogram;

/**
 * A histogram of text....  Created without reason.  We should
 * do an analysis of one of our medium text fields and determine
 *  its data layout.
 */
public class ShopSettingsHistogram extends Histogram<Integer> {

    {
        this.put(1, 10);
        this.put(5, 10);
        this.put(10, 100);
        this.put(15, 10000);
        this.put(20, 10000);
        this.put(25, 100);
        this.put(30, 100000);
        this.put(35, 1000);
        this.put(45, 1000);
        this.put(50, 1000);
        this.put(55, 1000);
        this.put(60, 1000);
        this.put(70, 100);
        this.put(80, 100);
        this.put(90, 100);
        this.put(100, 100);
        this.put(120, 100);
        this.put(250, 1000);
    }

}