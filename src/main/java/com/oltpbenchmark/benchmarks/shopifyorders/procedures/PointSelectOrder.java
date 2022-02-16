package com.oltpbenchmark.benchmarks.shopifyorders.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.shopifyorders.ShopifyOrdersConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PointSelectOrder extends Procedure {

    public final SQLStmt selectOrder = new SQLStmt(
        "SELECT * FROM " + ShopifyOrdersConstants.TABLE_ORDERS + 
          " WHERE id=?"
    );

    public void run(Connection conn, int orderId) throws SQLException {
        try (PreparedStatement ps = this.getPreparedStatement(conn, selectOrder)) {
            ps.setLong(1, orderId); //
            ps.execute();
        }
    }
}
