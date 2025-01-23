package at.phillip.sql;

import at.phillip.states.ServerStates;
import at.phillip.states.ServerTypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlayersSQL {

    private final MySQLConnector connector;

    public PlayersSQL(MySQLConnector connector){
        this.connector = connector;
    }

    public void createServerTable(){
        String sql = "CREATE TABLE IF NOT EXISTS cloud_players (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "player_uuid VARCHAR(255) NOT NULL," +
                "player_name VARCHAR(255) NOT NULL," +
                "online INT(1) NOT NULL)";
        try (Statement statement = connector.getConnection().createStatement()){
            statement.execute(sql);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void insertPlayer(String player_uuid, String player_name, boolean online){
        String sql = "INSERT INTO cloud_players " +
                "(player_uuid, player_name, online) VALUES " +
                "(?, ?, ?)";

        try (PreparedStatement preparedStatement = connector.connection.prepareStatement(sql)){
            preparedStatement.setString(1, player_uuid);
            preparedStatement.setString(2, player_name);
            preparedStatement.setBoolean(3, online);

            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<String> getPlayerNames(){
        String sql = "SELECT player_name FROM cloud_players";
        List<String> list = new ArrayList<>();
        try(PreparedStatement preparedStatement = connector.connection.prepareStatement(sql)){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    list.add(resultSet.getString("player_name"));
                }
            } catch (SQLException e){
            }
        } catch (SQLException e){
        }
        return list;
    }

    public int getPlayerId(String player_name){
        String sql = "SELECT id FROM cloud_players WHERE player_name=?";
        try(PreparedStatement preparedStatement = connector.connection.prepareStatement(sql)){
            preparedStatement.setString(1, player_name);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    return resultSet.getInt("id");
                }
            } catch (SQLException e){
            }
        } catch (SQLException e){
        }
        return 0;
    }

    public boolean isPlayerOnline(String player_name){
        String sql = "SELECT online FROM cloud_players WHERE player_name=?";
        try(PreparedStatement preparedStatement = connector.connection.prepareStatement(sql)){
            preparedStatement.setString(1, player_name);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()){
                    return resultSet.getBoolean("online");
                }
            } catch (SQLException e){
            }
        } catch (SQLException e){
        }
        return false;
    }
}
