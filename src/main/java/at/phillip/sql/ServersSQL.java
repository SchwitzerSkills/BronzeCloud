package at.phillip.sql;

import at.phillip.states.ServerStates;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServersSQL {

    final SQLiteConnector connector;

    public ServersSQL(SQLiteConnector connector){
        this.connector = connector;
    }

    public void createServerTable(){
        String sql = "CREATE TABLE IF NOT EXISTS servers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "server_name VARCHAR(255) NOT NULL," +
                "server_port INTEGER(5) NOT NULL," +
                "server_software VARCHAR(255) NOT NULL," +
                "server_version VARCHAR(255) NOT NULL," +
                "server_state VARCHAR(255) NOT NULL)";
        try (Statement statement = connector.connection.createStatement()){
            statement.execute(sql);
        } catch (SQLException e){
        }
    }

    public void insertServer(String serverName, int port, String software, String version, ServerStates states){
        String sql = "INSERT INTO servers " +
                "(server_name, server_port, server_software, server_version, server_state) VALUES " +
                "(?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connector.connection.prepareStatement(sql)){
            preparedStatement.setString(1, serverName);
            preparedStatement.setInt(2, port);
            preparedStatement.setString(3, software);
            preparedStatement.setString(4, version);
            preparedStatement.setString(5, states.toString());

            preparedStatement.executeUpdate();
        } catch (SQLException e){
        }
    }

    public List<String> getServers(){
        String sql = "SELECT * FROM servers";
        List<String> list = new ArrayList<>();
        try(PreparedStatement preparedStatement = connector.connection.prepareStatement(sql)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    list.add(resultSet.getString("server_name"));
                }
            } catch (SQLException e){
            }
        } catch (SQLException e){
        }
        return list;
    }

    public boolean isServerExist(String serverName){
        String sql = "SELECT COUNT(*) AS count FROM servers WHERE server_name=?";
        try(PreparedStatement preparedStatement = connector.connection.prepareStatement(sql)){
            preparedStatement.setString(1, serverName);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    return resultSet.getBoolean("count");
                }
            } catch (SQLException e){
            }
        } catch (SQLException e){
        }
        return false;
    }

    public void updateStates(String serverName, ServerStates states){
        String sql = "UPDATE servers SET server_state=? WHERE server_name=?";

        try (PreparedStatement preparedStatement = connector.connection.prepareStatement(sql)){
            preparedStatement.setString(1, states.toString());
            preparedStatement.setString(2, serverName);

            preparedStatement.executeUpdate();
        } catch (SQLException e){
        }
    }
}
