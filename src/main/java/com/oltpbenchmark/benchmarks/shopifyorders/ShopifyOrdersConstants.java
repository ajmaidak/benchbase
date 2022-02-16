/*
 * Copyright 2022 by OLTPBenchmark Project
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

public abstract class ShopifyOrdersConstants {

    //
    // Table Names
    //
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_SHOPS = "shops";
     
    //
    // Default number of orders per shop, this should
    // be moved to some sort of histogram distribution? or Zifian?
    // for now every shop starts with 2000k orders to trigger the range
    // select in OrdersByShopId proc
    //
    public static final int ORDERS = 2000;
    
    //
    // customer_id's are generated randomly in this range...
    // that should change as customers clearly repeat orders
    // on certain shops
    //
    public static final int MIN_CUSTOMER_ID = 1;
    public static final int MAX_CUSTOMER_ID = 2147483647;

    public static final String[] FINANICAL_STATUSES = { "authorized", 
                                                        "pending",
                                                        "paid",
                                                        "refunded",
                                                        "voided",
                                                        "partially_paid",
                                                        "partially_refunded",
                                                        "expired",
                                                        "unpaid" };

}
