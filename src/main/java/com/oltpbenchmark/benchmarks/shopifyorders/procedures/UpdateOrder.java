package com.oltpbenchmark.benchmarks.shopifyorders.procedures;

import com.oltpbenchmark.util.TimeUtil;
import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.shopifyorders.ShopifyOrdersConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UpdateOrder extends Procedure {

    public final SQLStmt createOrder = new SQLStmt(
        "UPDATE " + ShopifyOrdersConstants.TABLE_ORDERS + 
          " SET note=?, is_deleted=?, financial_status=?, updated_at=?" +
          " WHERE id=?"
    );

    public void run(Connection conn, int orderId, int isDeleted, int financialStatus, String note) throws SQLException {
        final Timestamp currentDateTime = new Timestamp(System.currentTimeMillis());
        try (PreparedStatement ps = this.getPreparedStatement(conn, createOrder)) {
            ps.setString(1, note);
            ps.setInt(2, isDeleted);
            ps.setString(3, ShopifyOrdersConstants.FINANICAL_STATUSES[financialStatus]);
            ps.setTimestamp(4, currentDateTime);
            ps.setInt(5, orderId);
            ps.execute();
        }
    }
}
