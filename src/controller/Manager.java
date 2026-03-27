package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Manager {

    protected <E> List<E> fetchEntities(String query, ResultSetMapper<E> mapper) {
        List<E> entities = new ArrayList<>();

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                entities.add(mapper.map(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching entities: " + e.getMessage());
        }

        return entities;
    }

    protected <E> List<E> fetchEntities(String query, PreparedStatementSetter setter, ResultSetMapper<E> mapper) {
        List<E> entities = new ArrayList<>();

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            setter.set(preparedStatement);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(mapper.map(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching entities: " + e.getMessage());
        }

        return entities;
    }

    protected <E> E fetchSingleEntity(String query, PreparedStatementSetter setter, ResultSetMapper<E> mapper) {
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            setter.set(preparedStatement);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapper.map(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching entity: " + e.getMessage());
        }

        return null;
    }

    protected boolean executeUpdate(String query, PreparedStatementSetter setter) {
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            setter.set(preparedStatement);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error executing update: " + e.getMessage());
            return false;
        }
    }

    protected boolean executeUpdate(String query, PreparedStatementSetter setter, String duplicateKeyMsg) {
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            setter.set(preparedStatement);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new IllegalArgumentException(duplicateKeyMsg);
            }
            System.err.println("Error executing update: " + e.getMessage());
            return false;
        }
    }

    protected void validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
    }

    protected void validateNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    @FunctionalInterface
    protected interface PreparedStatementSetter {
        void set(PreparedStatement preparedStatement) throws SQLException;
    }

    @FunctionalInterface
    protected interface ResultSetMapper<E> {
        E map(ResultSet resultSet) throws SQLException;
    }
}
