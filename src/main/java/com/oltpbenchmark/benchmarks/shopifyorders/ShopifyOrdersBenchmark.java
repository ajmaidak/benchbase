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

package com.oltpbenchmark.benchmarks.shopifyorders;

import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.shopifyorders.data.NoteHistogram;
import com.oltpbenchmark.benchmarks.shopifyorders.data.ShopSettingsHistogram;
import com.oltpbenchmark.benchmarks.shopifyorders.data.ShopNameHistogram;
import com.oltpbenchmark.benchmarks.shopifyorders.procedures.SelectShop;
import com.oltpbenchmark.util.RandomDistribution.FlatHistogram;

import java.util.ArrayList;
import java.util.List;

//
// This code was largely cargo culted from "smallbank" benchmark because
// I found it the most simple to understand
//

public class ShopifyOrdersBenchmark extends BenchmarkModule {

    protected final int numShops;

    protected final FlatHistogram<Integer> noteLengthGenerator;
    protected final FlatHistogram<Integer> shopNameLengthGenerator;
    protected final FlatHistogram<Integer> shopSettingsLengthGenerator;

    public ShopifyOrdersBenchmark(WorkloadConfiguration workConf) {
        super(workConf);
        this.numShops = (int) Math.max(this.getWorkloadConfiguration().getScaleFactor(), 1); //require at least one shop
        this.noteLengthGenerator = new FlatHistogram<>(this.rng(), new NoteHistogram());
        this.shopNameLengthGenerator= new FlatHistogram<>(this.rng(), new ShopNameHistogram());
        this.shopSettingsLengthGenerator = new FlatHistogram<>(this.rng(), new ShopSettingsHistogram());
    }

    @Override
    protected List<Worker<? extends BenchmarkModule>> makeWorkersImpl() {
        // I think it makes sense to think of the load driver as a orders worker...
        List<Worker<? extends BenchmarkModule>> workers = new ArrayList<>();
        for (int i = 0; i < workConf.getTerminals(); ++i) {
            workers.add(new ShopifyOrdersWorker(this, i));
        }
        return workers;
    }

    @Override
    protected Loader<ShopifyOrdersBenchmark> makeLoaderImpl() {
        return new ShopifyOrdersLoader(this);
    }

    @Override
    protected Package getProcedurePackageImpl() {
        // I think this is to discover the location of the procedures used
        // to load records into the database?  Unsure why.  Shops
        // was choosen because its all that exists right now
        return SelectShop.class.getPackage();
    }

}
