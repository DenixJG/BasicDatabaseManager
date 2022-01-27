package org.badama;



import org.badama.models.ResultSetExtended;
import org.badama.utils.DBTablePrinter;

import java.sql.*;

// TODO: Mejorar la documentación

/**
 * Clase que controla la conexión con la base de datos, conecta y desconecta a la base de datos.
 * Si es necesario, se puede elegir a que base de datos se conecta y otras opciones de conexión, como
 * el usuario y contraseña o las opciones de conexión.
 * <p>
 * También se puede ejecutar una consulta SQL y obtener un ResultSet con los resultados de la consulta.
 *
 * @author Rafael Popescu
 * @version 1.0
 */
public class BasicDatabaseManager {
    private static final String DEFAULT_DATABASE_USERNAME = "root";

    private static Connection connection;

    public BasicDatabaseManager() {

    }

    //region Métodos de conexión

    /**
     * Conecta a la base de datos en local, con el driver de MySQL.
     * <p>
     * Por defecto si solo se pone el nombre de la base de datos, se conecta a la base de datos en local
     * con las siguientes opciones:
     * <ul>
     *     <li><b>Driver:</b> com.mysql.jdbc.Driver</li>
     *     <li><b>URL:</b> jdbc:mysql://localhost:3306/'database_name'</li>
     *     <li><b>User:</b> 'root'</li>
     *     <li><b>Password:</b> ''</li>
     * </ul>
     *
     * @param databaseName Nombre de la base de datos a la que se quiere conectar.
     */
    public static void connect(String databaseName) throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, "root", "");
        } else {
            throw new SQLException("Ya hay una conexión abierta.");
        }
    }

    /**
     * Conectarse a un servidor de base de datos MySQL con el driver de MySQL. Se puede elegir el usuario y la contraseña
     * para la conexión así como la url del servidor y el nombre de la base de datos a la que se quiere conectar.
     *
     * @param url          URL del servidor de base de datos.
     * @param databaseName Nombre de la base de datos.
     * @param user         Usuario.
     * @param password     Contraseña.
     * @throws SQLException Si no se puede conectar a la base de datos.
     */
    public static void connect(String url, String databaseName, String user, String password) throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:mysql://" + url + "/" + databaseName, user, password);
        } else {
            throw new SQLException("Ya hay una conexión abierta.");
        }
    }

    /**
     * Conectarse a un servidor de base de datos MySQL con el driver de MySQL.
     * <p>
     * El usuario y la contraseña son por defecto 'root' y '' respectivamente.
     *
     * @param url          URL del servidor de base de datos.
     * @param databaseName Nombre de la base de datos.
     * @throws SQLException Si no se puede conectar a la base de datos.
     */
    public static void connect(String url, String databaseName) throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:mysql://" + url + "/" + databaseName, DEFAULT_DATABASE_USERNAME, "");
        } else {
            throw new SQLException("Ya hay una conexión abierta.");
        }
    }

    //endregion

    //region Métodos de información

    /**
     * Obtiene la siguiente información de la conexión:
     * <ul>
     *     <li><b>Database name: </b> Nombre de la base de datos</li>
     *     <li><b>Database version: </b> Version de la base de datos</li>
     *     <li><b>Driver name: </b> Nombre del driver de la base de datos</li>
     *     <li><b>Driver version: </b> Versión del driver de la base de datos</li>
     * </ul>
     */
    public void getDatabaseInfo() {
        // Obtiene información de la base de datos en la que se está conectando
        if (connection != null) {
            try {
                System.out.println("Database name: " + connection.getCatalog());
                System.out.println("Database version: " + connection.getMetaData().getDatabaseProductVersion());
                System.out.println("Driver name: " + connection.getMetaData().getDriverName());
                System.out.println("Driver version: " + connection.getMetaData().getDriverVersion());
            } catch (SQLException e) {
                System.err.println("Error getting database info: " + e.getMessage());
            }
        } else {
            System.err.println("No database connection");
        }
    }

    /**
     * Imprime los datos del {@linkplain ResultSetExtended} pasado como parámetro.
     *
     * @param rse {@linkplain ResultSetExtended} con los datos a imprimir.
     */
    public void printResultSetExtended(ResultSetExtended rse) {
        DBTablePrinter.printResultSet(rse);
    }

    /**
     * Comprueba si la conexión está activa.
     *
     * @return <b>true</b> si la conexión está activa, <b>false</b> en caso contrario.
     */
    public boolean isConnected() {
        return connection != null;
    }

    /**
     * Devuelve la conexión actual.
     *
     * @return {@linkplain Connection} con la conexión actual.
     */
    public Connection getConnection() {
        return connection;
    }

    //endregion

    //region Métodos de desconexión

    /**
     * Desconecta de la base de datos.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error disconnecting from database: " + e.getMessage());
            }
        } else {
            System.err.println("No database connection");
        }
    }

    //endregion

    //region Métodos de consulta

    /**
     * Ejecuta una consulta SQL preparada a la que se le pueden pasar parámetros, y devuelve
     * un {@linkplain ResultSetExtended} con el resultado de la consulta preparada, si no se pasan
     * parámetros, se ejecuta la consulta normal, es decir, una consulta no preparada.
     * <p>
     * El método también comprueba si la consulta modifica la base de datos con un {@code INSERT}, {@code UPDATE} o
     * {@code DELETE} o no y solo se usa {@code SELECT} para una consulta simple, por lo que no hace falta
     * especificar {@linkplain Statement#executeUpdate(String)} o {@linkplain Statement#executeQuery(String)}.
     *
     * <p>
     * Ejemplo de consulta preparada:
     * <pre>
     *     {@code executeSuperQuery("SELECT * FROM table WHERE id = ?", 1);}
     * </pre>
     * Ejemplo de consulta no preparada:
     * <pre>
     *     {@code executeSuperQuery("SELECT * FROM table WHERE id = 1");}
     * </pre>
     * <p>
     * La sentencia SQL preparada puede contener los siguientes tipos de parámetros:
     * <ul>
     *     <li>String</li>
     *     <li>Integer</li>
     *     <li>Double</li>
     *     <li>Float</li>
     *     <li>Boolean</li>
     *     <li>Date</li>
     *     <li>Time</li>
     * </ul>
     * <b>Es importante que los parámetros se pasen en el orden correcto, el orden de los parámetros
     * será el mismo que tenga la consulta SQL.</b>
     *
     * @param query  La consulta SQL a ejecutar.
     * @param values Los valores que se le pasan a la consulta.
     * @return {@linkplain ResultSetExtended} con los resultados de la sentencia SQL.
     * @throws SQLException Si ocurre algún error al ejecutar la consulta.
     */
    public ResultSetExtended executeSuperQuery(String query, Object... values) throws SQLException {
        // Información del programa.
        ResultSetExtended rse;

        // Result set
        ResultSet rs;

        // Si no hay valores, se ejecuta la consulta normal.
        boolean isExecuteUpdate = query.toUpperCase().startsWith("INSERT") || query.toUpperCase().startsWith("UPDATE")
                || query.toUpperCase().startsWith("DELETE");

        if (values.length == 0) {
            // Sentencia SQL normal
            if (query.toUpperCase().startsWith("SELECT")) {
                rs = connection.createStatement().executeQuery(query);
                rse = new ResultSetExtended(20, "Query executed", 0, rs);
            } else if (isExecuteUpdate) {
                int updateStatus = connection.createStatement().executeUpdate(query);
                rse = getUpdatedMessageType(query, updateStatus);
            } else {
                rse = new ResultSetExtended(20, "Error", -1);
            }

        } else {
            // Prepara la consulta
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Se rellena el preparedStatement con los valores que se le pasan a la consulta,
            // comprueba de que tipo es cada valor y lo mete en el preparedStatement.
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof String) {
                    preparedStatement.setString(i + 1, (String) values[i]);
                }
                if (values[i] instanceof Integer) {
                    preparedStatement.setInt(i + 1, (Integer) values[i]);
                }
                if (values[i] instanceof Double) {
                    preparedStatement.setDouble(i + 1, (Double) values[i]);
                }
                if (values[i] instanceof Float) {
                    preparedStatement.setFloat(i + 1, (Float) values[i]);
                }
                if (values[i] instanceof Boolean) {
                    preparedStatement.setBoolean(i + 1, (Boolean) values[i]);
                }
                if (values[i] instanceof Date) {
                    preparedStatement.setDate(i + 1, (Date) values[i]);
                }
                if (values[i] instanceof Time) {
                    preparedStatement.setTime(i + 1, (Time) values[i]);
                }

            }

            // Comprobar si se hace uso de executeUpdate o executeQuery
            if (isExecuteUpdate) {
                // Establecemos la información del programa
                int updateStatus = preparedStatement.executeUpdate(); // Ejecuta la consulta
                rse = getUpdatedMessageType(query, updateStatus);
            } else if (query.toUpperCase().startsWith("SELECT")) {
                rs = preparedStatement.executeQuery(); // Ejecuta la consulta
                rse = new ResultSetExtended(StatementCodes.COD_SELECT, "Query successful", rs);
            } else { // Si no es ninguna de las anteriores, es un error
                rse = getUpdatedMessageType(query, -1);
            }
        }

        return rse;
    }

    //endregion

    //region Métodos de privados

    /**
     * Devuelve un {@linkplain ResultSetExtended} con el estado de la sentencia SQL.
     * <p>
     * Si la sentencia SQL es una sentencia INSERT, UPDATE o DELETE, devuelve un {@linkplain ResultSetExtended}
     * con el estado de la sentencia y el mensaje de éxito correspondiente y la cantidad de filas afectadas.
     *
     * @param query        La sentencia SQL.
     * @param updateStatus El estado de la sentencia SQL.
     * @return Un {@linkplain ResultSetExtended} con el estado de la sentencia SQL.
     */
    private ResultSetExtended getUpdatedMessageType(String query, int updateStatus) {
        ResultSetExtended rse;

        if (query.toUpperCase().startsWith("INSERT")) {
            rse = new ResultSetExtended(StatementCodes.COD_INSERT, "Insert executed", updateStatus);
        } else if (query.toUpperCase().startsWith("UPDATE")) {
            rse = new ResultSetExtended(StatementCodes.COD_UPDATE, "Update executed", updateStatus);
        } else if (query.toUpperCase().startsWith("DELETE")) {
            rse = new ResultSetExtended(StatementCodes.COD_DELETE, "Delete executed", updateStatus);
        } else {
            rse = new ResultSetExtended(StatementCodes.COD_ERROR, "Error");
        }
        return rse;
    }

    //endregion

}

