package com.oltpbenchmark.benchmarks.shopifyorders.procedures;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.shopifyorders.ShopifyOrdersConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrdersByShopId extends Procedure {

    public final SQLStmt selectOrdersByShopId = new SQLStmt(
        "SELECT * FROM " + ShopifyOrdersConstants.TABLE_ORDERS + 
       // Figure out how to include force index in this query for MySQL
       //   " FORCE INDEX(primary)" + 
          " WHERE shop_id=? AND is_deleted=false ORDER BY id LIMIT 100"
    );

    public void run(Connection conn, int shopId) throws SQLException {
        try (PreparedStatement ps = this.getPreparedStatement(conn, selectOrdersByShopId)) {
            ps.setInt(1, shopId); //
            ps.execute();
        }
    }
}
