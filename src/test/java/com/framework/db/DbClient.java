package com.framework.db;

import com.framework.config.ConfigManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Thin JDBC helper. Each call opens and closes its own connection. */
public final class DbClient {

    private DbClient() {
    }

    public static List<Map<String, Object>> query(String sql, Object... params) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                return toRows(rs);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("SQL query failed: " + sql, e);
        }
    }

    public static int update(String sql, Object... params) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bind(ps, params);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("SQL update failed: " + sql, e);
        }
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(
                ConfigManager.require("db.url"),
                ConfigManager.require("db.user"),
                ConfigManager.require("db.password"));
    }

    private static void bind(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    private static List<Map<String, Object>> toRows(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> rows = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columns; i++) {
                row.put(md.getColumnLabel(i), rs.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }
}
