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

import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.LoaderThread;
import com.oltpbenchmark.catalog.Table;
import com.oltpbenchmark.util.SQLUtil;


import com.oltpbenchmark.util.TextGenerator;
import com.oltpbenchmark.util.RandomGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

class ShopifyOrdersLoader extends Loader<ShopifyOrdersBenchmark> {
    private final int ordersPerShop;

    private final Table catalogShops;
    private final Table catalogOrders;

    private final String sqlShops;
    private final String sqlOrders;

    public ShopifyOrdersLoader(ShopifyOrdersBenchmark benchmark) {
        super(benchmark);

        this.catalogShops = benchmark.getCatalog().getTable(ShopifyOrdersConstants.TABLE_SHOPS);
        this.catalogOrders = benchmark.getCatalog().getTable(ShopifyOrdersConstants.TABLE_ORDERS);

        this.sqlShops = SQLUtil.getInsertSQL(this.catalogShops, this.getDatabaseType());
        this.sqlOrders = SQLUtil.getInsertSQL(this.catalogOrders, this.getDatabaseType());

        this.ordersPerShop = ShopifyOrdersConstants.ORDERS;
    }

    @Override
    public List<LoaderThread> createLoaderThreads() {
        List<LoaderThread> threads = new ArrayList<>();

        for (int shop = 1; shop <= benchmark.numShops; shop++) {
            final int shopId = shop;
            threads.add(new ShopGenerator(shopId, this.ordersPerShop));
        }
        return (threads);
    }

    private class ShopGenerator extends LoaderThread {
        private final int shopId;
        private final int ordersPerShop;

        PreparedStatement stmtShop;
        PreparedStatement stmtOrders;

        public ShopGenerator(int shopId, int ordersPerShop) {
            super(benchmark);
            this.shopId = shopId;
            this.ordersPerShop = ordersPerShop;
        }

        @Override
        public void load(Connection conn){
            try {

                this.stmtShop = conn.prepareStatement(ShopifyOrdersLoader.this.sqlShops);
                this.stmtOrders = conn.prepareStatement(ShopifyOrdersLoader.this.sqlOrders);

                final Timestamp dateTime = new Timestamp(System.currentTimeMillis());
                RandomGenerator randIntGenerator = new RandomGenerator((int) System.currentTimeMillis());

                int shopNameLength = benchmark.shopNameLengthGenerator.nextValue();
                String shopName = TextGenerator.randomStr(benchmark.rng(), shopNameLength);

                int shopSettingsLength = benchmark.shopSettingsLengthGenerator.nextValue();
                String settings = TextGenerator.randomStr(benchmark.rng(), shopSettingsLength);

                String domainName = TextGenerator.randomStr(benchmark.rng(), 25);

                // Insert the shop
                stmtShop.setInt(1, shopId);
                stmtShop.setString(2, shopName);
                stmtShop.setString(3, settings); // settings
                stmtShop.setString(4, domainName);
                stmtShop.setTimestamp(5, dateTime);
                stmtShop.setTimestamp(6, dateTime);


                //Batch insert the shop orders
                int orderId = 1 + this.ordersPerShop * (shopId - 1);
                int lastOrderId = this.ordersPerShop * shopId;
                for(; orderId <= lastOrderId; orderId++) {

                    int noteLength = benchmark.noteLengthGenerator.nextValue();
                    String note = TextGenerator.randomStr(benchmark.rng(), noteLength);

                    int customerId = randIntGenerator.number(ShopifyOrdersConstants.MIN_CUSTOMER_ID, ShopifyOrdersConstants.MAX_CUSTOMER_ID);
                    int isDeleted = randIntGenerator.number(0, 1);
                    int financialStatus = randIntGenerator.number(1, ShopifyOrdersConstants.FINANICAL_STATUSES.length) - 1;

                    stmtOrders.setInt(1, orderId);
                    stmtOrders.setInt(2, shopId);     
                    stmtOrders.setInt(3, customerId); // customer_id
                    stmtOrders.setInt(4, isDeleted);  // is_deleted
                    stmtOrders.setString(5, note); // note
                    stmtOrders.setString(6, ShopifyOrdersConstants.FINANICAL_STATUSES[financialStatus]); // financial_status
                    stmtOrders.setTimestamp(7, dateTime); // updated_at
                    stmtOrders.setTimestamp(8, dateTime); // created_at
                    stmtOrders.addBatch();
                }
                this.loadTables(conn);
            } catch (SQLException ex) {
                LOG.error("Failed to load data", ex);
                throw new RuntimeException(ex);
            }
        }

        private void loadTables(Connection conn) throws SQLException {
            this.stmtShop.execute();
            this.stmtOrders.executeBatch();
        }

    }
}
