package at.phillip.sql;

import at.phillip.states.ServerStates;
import at.phillip.states.ServerTypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PermsSQL {

    private final MySQLConnector connector;

    public PermsSQL(MySQLConnector connector){
        this.connector = connector;
    }

    public void createServerTable(){
        String sql = "CREATE TABLE IF NOT EXISTS cloud_perms (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "player_id VARCHAR(255) NOT NULL," +
                "perms VARCHAR(255) NOT NULL)";
        try (Statement statement = connector.getConnection().createStatement()){
            statement.execute(sql);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void insertPerms(int player_id, String perms){
        String sql = "INSERT INTO cloud_perms " +
                "(player_id, perms) VALUES " +
                "(?, ?)";

        try (PreparedStatement preparedStatement = connector.connection.prepareStatement(sql)){
            preparedStatement.setInt(1, player_id);
            preparedStatement.setString(2, perms);

            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deletePerms(int player_id, String perms){
        String sql = "DELETE FROM cloud_perms where player_id=? and perms=?";

        try (PreparedStatement preparedStatement = connector.connection.prepareStatement(sql)){
            preparedStatement.setInt(1, player_id);
            preparedStatement.setString(2, perms);

            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public List<String> getPerms(int player_id){
        String sql = "SELECT * FROM cloud_perms where player_id=?";
        List<String> list = new ArrayList<>();
        try(PreparedStatement preparedStatement = connector.connection.prepareStatement(sql)){
            preparedStatement.setInt(1, player_id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    list.add(resultSet.getString("perms"));
                }
            } catch (SQLException e){
            }
        } catch (SQLException e){
        }
        return list;
    }

}
