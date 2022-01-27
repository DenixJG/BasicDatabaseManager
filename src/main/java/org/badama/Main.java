package org.badama;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        BasicDatabaseManager.connect("aad");
        BasicDatabaseManager manager = new BasicDatabaseManager();
        manager.printResultSetExtended(manager.executeSuperQuery("SELECT * FROM empleados WHERE emp_no = ?", 7369));
    }
}
