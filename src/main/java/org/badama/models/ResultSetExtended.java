package org.badama.models;

import java.sql.ResultSet;


/**
 * ResultSetExtended es una clase que guarda un ResultSet y otros datos adicionales
 * para facilitar la manipulación de los resultados de una consulta.
 *
 * @author Rafael Popescu
 */
public class ResultSetExtended {
    private int id;
    private String infoMessage;
    private int affectedRows;
    private ResultSet resultSet = null;

    public ResultSetExtended() {
    }

    public ResultSetExtended(int id, String infoMessage) {
        this.id = id;
        this.infoMessage = infoMessage;
    }

    public ResultSetExtended(int id, String message, int affectedRows) {
        this.id = id;
        this.infoMessage = message;
        this.affectedRows = affectedRows;
    }

    public ResultSetExtended(int id, String message, int affectedRows, ResultSet resultSet) {
        this.id = id;
        this.infoMessage = message;
        this.affectedRows = affectedRows;
        this.resultSet = resultSet;
    }

    public ResultSetExtended(int id, String infoMessage, ResultSet resultSet) {
        this.id = id;
        this.infoMessage = infoMessage;
        this.resultSet = resultSet;
    }

    public int getId() {
        return id;
    }

    public String getInfoMessage() {
        return infoMessage;
    }

    public int getAffectedRows() {
        return affectedRows;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    public void setAffectedRows(int affectedRows) {
        this.affectedRows = affectedRows;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
