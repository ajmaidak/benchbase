package com.oltpbenchmark.benchmarks.shopifyorders.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.shopifyorders.ShopifyOrdersConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrdersByCustomerId extends Procedure {

    public final SQLStmt selectOrdersByCustomerId = new SQLStmt(
        "SELECT * FROM " + ShopifyOrdersConstants.TABLE_ORDERS + 
          " WHERE customer_id=? AND is_deleted=false"
    );

    public void run(Connection conn, int customerId) throws SQLException {
        try (PreparedStatement ps = this.getPreparedStatement(conn, selectOrdersByCustomerId)) {
            ps.setLong(1, customerId); //
            ps.execute();
        }
    }
}
