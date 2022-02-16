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

package com.oltpbenchmark.benchmarks.shopifyorders.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.shopifyorders.ShopifyOrdersConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class InsertOrder extends Procedure {

    public final SQLStmt createOrder = new SQLStmt(
        "INSERT INTO " + ShopifyOrdersConstants.TABLE_ORDERS + 
          " (id, shop_id, customer_id, is_deleted, note, financial_status, updated_at, created_at) " +
          " VALUES (?,?,?,?,?,?,?,?)"
    );

    public void run(Connection conn, int shopId, int orderId, int customerId, int isDeleted, int financialStatus, String note) throws SQLException {
        final Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
        try (PreparedStatement ps = this.getPreparedStatement(conn, createOrder)) {
            ps.setInt(1, orderId); //
            ps.setInt(2, shopId); //
            ps.setInt(3, customerId); // customer_id
            ps.setInt(4, isDeleted); // is_deleted
            ps.setString(5, note); // note
            ps.setString(6, ShopifyOrdersConstants.FINANICAL_STATUSES[financialStatus]); // financial_status
            ps.setTimestamp(7, currentDateTime);  // updated_at
            ps.setTimestamp(8, currentDateTime); // created_at
            ps.execute();
        }
    }
}

