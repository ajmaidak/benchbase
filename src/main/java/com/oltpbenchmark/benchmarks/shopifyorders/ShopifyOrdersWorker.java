/*
 * Copyright 2021 by OLTPBenchmark Project
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

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.Procedure.UserAbortException;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.shopifyorders.procedures.*;
import com.oltpbenchmark.distributions.CounterGenerator;
import com.oltpbenchmark.types.TransactionStatus;
import com.oltpbenchmark.util.RandomGenerator;
import com.oltpbenchmark.util.TextGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * ShopifyOrdersWorker Implementation
 * Much Cargo Cult Here, beware
 *
 * @author @ajmaidak
 */
class ShopifyOrdersWorker extends Worker<ShopifyOrdersBenchmark> {
    private static final Logger LOG = LoggerFactory.getLogger(ShopifyOrdersWorker.class);

    // Procs
    private final InsertOrder procInsertOrder;
    private final UpdateOrder procUpdateOrder;
    private final PointSelectOrder procPointSelectOrder;
    private final SelectShop procSelectShop;
    private final OrdersByCustomerId procOrdersByCustomerId;
    private final OrdersByShopId procOrdersByShopId;

    // Scale
    private final int numOrders;
    private final int numShops;

    // Generators...
    private static CounterGenerator nextOrderGenerator;
    private static final RandomGenerator randomGenerator = new RandomGenerator(1);


    public ShopifyOrdersWorker(ShopifyOrdersBenchmark benchmarkModule, int id) {
        super(benchmarkModule, id);

        this.procInsertOrder = this.getProcedure(InsertOrder.class);
        this.procUpdateOrder = this.getProcedure(UpdateOrder.class);
        this.procPointSelectOrder = this.getProcedure(PointSelectOrder.class);
        this.procSelectShop = this.getProcedure(SelectShop.class);
        this.procOrdersByCustomerId = this.getProcedure(OrdersByCustomerId.class);
        this.procOrdersByShopId = this.getProcedure(OrdersByShopId.class);

        // must have at least one shop
        this.numShops = (int) Math.max(this.getWorkloadConfiguration().getScaleFactor(), 1);

        // orders per shop is currently constant...
        this.numOrders = ShopifyOrdersConstants.ORDERS * numShops;

        // Guard the counter in a lock...  Cargo culted from somewhere...
        synchronized (ShopifyOrdersWorker.class){
            if (nextOrderGenerator == null) {
                nextOrderGenerator = new CounterGenerator(this.numOrders+1);
            }
        }
    }

    @Override
    protected TransactionStatus executeWork(Connection conn, TransactionType nextTransaction) throws UserAbortException {
        Class<? extends Procedure> procClass = nextTransaction.getProcedureClass();

        ShopifyOrdersBenchmark sob = this.getBenchmarkModule();

        int lastOrderId = nextOrderGenerator.lastInt();
        final int orderId = randomGenerator.number(1, lastOrderId);
        final int shopId = randomGenerator.number(1, numShops);

        try {
            if(procClass.equals(InsertOrder.class)) {
                int nextOrderId = nextOrderGenerator.nextInt();
                int customerId = randomGenerator.number(ShopifyOrdersConstants.MIN_CUSTOMER_ID, ShopifyOrdersConstants.MAX_CUSTOMER_ID);
                int isDeleted = 0;
                int financialStatus = randomGenerator.number(1, ShopifyOrdersConstants.FINANICAL_STATUSES.length) - 1;
                String note = TextGenerator.randomStr(sob.rng(), sob.noteLengthGenerator.nextValue());
                this.procInsertOrder.run(conn, shopId, nextOrderId, customerId, isDeleted, financialStatus, note);
            }
            else if (procClass.equals(SelectShop.class)){
                this.procSelectShop.run(conn, shopId);
            }
            else if (procClass.equals(UpdateOrder.class)) {
                int isDeleted = randomGenerator.number(0, 1);
                int financialStatus = randomGenerator.number(1, ShopifyOrdersConstants.FINANICAL_STATUSES.length) - 1;
                String note = TextGenerator.randomStr(sob.rng(), sob.noteLengthGenerator.nextValue());
                this.procUpdateOrder.run(conn, orderId, isDeleted, financialStatus, note);
            }
            else if (procClass.equals(PointSelectOrder.class)) {
                this.procPointSelectOrder.run(conn, orderId);
            }
            else if (procClass.equals(OrdersByCustomerId.class)) {
                int customerId = randomGenerator.number(ShopifyOrdersConstants.MIN_CUSTOMER_ID, ShopifyOrdersConstants.MAX_CUSTOMER_ID);
                this.procOrdersByCustomerId.run(conn, customerId);
            }
            else if (procClass.equals(OrdersByShopId.class)) {
                this.procOrdersByShopId.run(conn, shopId);
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }

        return (TransactionStatus.SUCCESS);
    }
}
