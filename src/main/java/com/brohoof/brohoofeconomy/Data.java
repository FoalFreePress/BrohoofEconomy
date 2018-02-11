package com.brohoof.brohoofeconomy;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * This class handles data transfer to and from the SQL server.
 *
 */
public class Data {

    private Connection connection;
    private final Settings s;
    private Logger logger;

    public Data(final BrohoofEconomyPlugin plugin) {
        s = plugin.getSettings();
        logger = plugin.getLogger();
        createTables();
    }

    /**
     * Saves an account, or creates one if it does not exist.
     * 
     * @param acc
     */
    public void saveAccount(Account acc) {
        if (acc == null)
            return;
        try {
            if (acc.getId() < 1)
                // New account
                try {
                    PreparedStatement statement = connection.prepareStatement(String.format("INSERT INTO %splayers (uuid, name, cash) VALUES (?, ?, ?);", s.dbPrefix));
                    statement.setString(1, acc.getUuid().toString());
                    statement.setString(2, acc.getName());
                    statement.setDouble(3, acc.getCash());
                    statement.executeUpdate();
                    statement.close();
                    return;
                } catch (final SQLException e) {
                    error(e);
                    return;
                }
            // Old account
            PreparedStatement statement = connection.prepareStatement(String.format("UPDATE %splayers SET name = ?, cash = ? WHERE uuid = ?;", s.dbPrefix));
            statement.setString(1, acc.getName());
            statement.setDouble(2, acc.getCash());
            statement.setString(3, acc.getUuid().toString());
            statement.executeUpdate();
            statement.close();
        } catch (final SQLException e) {
            error(e);
            return;
        }
    }

    public ArrayList<Account> getAllAccounts() {
        ArrayList<Account> list = new ArrayList<Account>(0);
        try {
            final String query = "SELECT * FROM " + s.dbPrefix + "players";
            ResultSet rs = getResultSet(query);
            while (rs.next()) {
                final String stringuuuid = rs.getString("uuid");
                if (isNull(stringuuuid))
                    throw new NoDataException();
                int id = rs.getInt("id");
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                String name = rs.getString("name");
                double cash = rs.getDouble("cash");
                list.add(new Account(id, uuid, name, cash));
            }
            return list;
        } catch (SQLException | IllegalArgumentException | NoDataException e) {
            error(e);
            return list;
        }
    }

    public Optional<Account> getAccount(String name) {
        int id;
        UUID uuid;
        double cash;
        final String query = "SELECT * FROM " + s.dbPrefix + "players where name = \"" + name + "\";";
        ResultSet rs;
        try {
            rs = getResultSet(query);
            if (rs.next()) {
                final String stringuuuid = rs.getString("uuid");
                if (isNull(stringuuuid))
                    throw new NoDataException();
                id = rs.getInt("id");
                uuid = UUID.fromString(rs.getString("uuid"));
                name = rs.getString("name");
                cash = rs.getDouble("cash");
            } else
                throw new NoDataException();
        } catch (SQLException | IllegalArgumentException | NoDataException e) {
            error(e);
            return Optional.<Account>empty();
        }
        return Optional.<Account>of(new Account(id, uuid, name, cash));
    }

    public Optional<Account> getAccount(UUID uuid) {
        int id;
        String name;
        double cash;
        final String query = "SELECT * FROM " + s.dbPrefix + "players where uuid = \"" + uuid.toString() + "\";";
        ResultSet rs;
        try {
            rs = getResultSet(query);
            if (rs.next()) {
                final String stringuuuid = rs.getString("uuid");
                if (isNull(stringuuuid))
                    throw new NoDataException();
                id = rs.getInt("id");
                uuid = UUID.fromString(rs.getString("uuid"));
                name = rs.getString("name");
                cash = rs.getDouble("cash");
            } else
                throw new NoDataException();
        } catch (SQLException | IllegalArgumentException | NoDataException e) {
            error(e);
            return Optional.<Account>empty();
        }
        return Optional.<Account>of(new Account(id, uuid, name, cash));
    }

    /**
     * Creates a new table in the database
     *
     * @param pQuery
     *            the query
     * @return
     */
    private boolean createTable(final String pQuery) {
        try {
            executeQuery(pQuery);
            return true;
        } catch (final SQLException e) {
            error(e);
        }
        return false;
    }

    /**
     * Create tables if needed
     */
    private final void createTables() {
        // Generate the information about the various tables
        final String players = "CREATE TABLE `" + s.dbPrefix + "players` (`id` INT NOT NULL AUTO_INCREMENT, `uuid` CHAR(36) NOT NULL, `name` VARCHAR(16) NOT NULL, `cash` DOUBLE NOT NULL, PRIMARY KEY (`id`), UNIQUE INDEX `id_UNIQUE` (`id` ASC), UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC));";
        // Generate the database tables
        if (!tableExists(s.dbPrefix + "players"))
            createTable(players);
    }

    /**
     * Method used to handle errors
     *
     * @param e
     *            Exception
     */
    public void error(final Throwable e) {
        if (s.stackTraces) {
            e.printStackTrace();
            return;
        }
        if (e instanceof SQLException) {
            logger.severe("SQLException: " + e.getMessage());
            return;
        }
        if (e instanceof IllegalArgumentException)
            // It was probably someone not putting in a valid UUID, so we can ignore.
            // p.severe("IllegalArgumentException: " + e.getMessage());
            return;
        if (e instanceof NoDataException) {
            // If true, then it was caused by data in Account not being found.
            if (e.getMessage().equalsIgnoreCase("No Account found."))
                return;
            logger.severe("NoDataException: " + e.getMessage());
            return;
        }
        if (e instanceof NoClassDefFoundError)
            // Handle Plugins not found.
            // p.severe("NoClassDefFoundError: " + e.getMessage());
            return;
        if (e instanceof IOException) {
            logger.severe("IOException: " + e.getMessage());
            return;
        }
        // Or e.getCause();
        logger.severe("Unhandled Exception " + e.getClass().getName() + ": " + e.getMessage());
        e.printStackTrace();
    }

    /**
     * Executes an SQL query. Throws an exception to allow for other methods to handle it.
     *
     * @param query
     *            the query to execute
     * @return number of rows affected
     * @throws SQLException
     *             if an error occurred
     */
    private int executeQuery(final String query) throws SQLException {
        if (s.showQuery)
            logger.info(query);
        if (connection == null || connection.isClosed()) {
            final String connect = new String("jdbc:mysql://" + s.dbHost + ":" + s.dbPort + "/" + s.dbDatabase + "?autoReconnect=true&useSSL=false");
            connection = DriverManager.getConnection(connect, s.dbUser, s.dbPass);
            logger.info("Connecting to " + s.dbUser + "@" + connect + "...");
        }
        return connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeUpdate(query);
    }

    /**
     * Private method for getting an SQL connection, then submitting a query. This method throws an SQL Exception to allow another method to handle it.
     *
     * @param query
     *            the query to get data from.
     * @return the data
     * @throws SQLException
     *             if an error occurs
     */
    private ResultSet getResultSet(final String query) throws SQLException {
        if (s.showQuery)
            logger.info(query);
        if (connection == null || connection.isClosed()) {
            final String connect = new String("jdbc:mysql://" + s.dbHost + ":" + s.dbPort + "/" + s.dbDatabase + "?autoReconnect=true&useSSL=false");
            connection = DriverManager.getConnection(connect, s.dbUser, s.dbPass);
            logger.info("Connecting to " + s.dbUser + "@" + connect + "...");
        }
        return connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(query);
    }

    /**
     * Forces a refresh of the connection object
     */
    public void forceConnectionRefresh() {
        try {
            if (connection == null || connection.isClosed()) {
                final String connect = new String("jdbc:mysql://" + s.dbHost + ":" + s.dbPort + "/" + s.dbDatabase + "?autoReconnect=true&useSSL=false");
                connection = DriverManager.getConnection(connect, s.dbUser, s.dbPass);
                logger.info("Connecting to " + s.dbUser + "@" + connect + "...");
            } else {
                connection.close();
                final String connect = new String("jdbc:mysql://" + s.dbHost + ":" + s.dbPort + "/" + s.dbDatabase + "?autoReconnect=true&useSSL=false");
                connection = DriverManager.getConnection(connect, s.dbUser, s.dbPass);
                logger.info("Connecting to " + s.dbUser + "@" + connect + "...");
            }
        } catch (final SQLException e) {
            error(e);
        }
    }

    /**
     * Checks if a string is "null"
     *
     * @param string
     *            the string to check
     * @return true if the string is equal to null, the string is empty, the string is "null", or if the string is ",", else false.
     */
    public boolean isNull(final String string) {
        if (string == null || string.isEmpty() || string.equalsIgnoreCase("null") || string.equalsIgnoreCase(","))
            return true;
        return false;
    }

    /**
     * checks if a table exists
     *
     * @param pTable
     *            the table name.
     * @return true if the table exists, or false if either the table does not exists, or another error occurs.
     */
    private boolean tableExists(final String pTable) {
        try {
            return getResultSet("SELECT * FROM " + pTable) != null;
        } catch (final SQLException e) {
            // Handle both ' and "
            if (e.getMessage().equalsIgnoreCase("Table '" + s.dbDatabase + "." + pTable + "' doesn't exist") || e.getMessage().equalsIgnoreCase("Table \"" + s.dbDatabase + "." + pTable + "\" doesn't exist"))
                return false;
            error(e);
        }
        return false;
    }
}
