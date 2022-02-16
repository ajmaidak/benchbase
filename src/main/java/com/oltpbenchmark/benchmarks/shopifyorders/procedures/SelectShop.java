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

public class SelectShop extends Procedure {
    public final SQLStmt selectShop = new SQLStmt(
            "SELECT * FROM " + ShopifyOrdersConstants.TABLE_SHOPS + " WHERE id=?"
    );

    public void run(Connection conn, int shopId) throws SQLException {
        try (PreparedStatement ps = this.getPreparedStatement(conn, selectShop)) {
            ps.setLong(1, shopId);
            ps.execute();
        }
    }
}
