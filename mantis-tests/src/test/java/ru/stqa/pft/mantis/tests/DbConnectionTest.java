package ru.stqa.pft.mantis.tests;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import org.testng.annotations.Test;
import ru.stqa.pft.mantis.model.UserData;
import ru.stqa.pft.mantis.model.Users;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnectionTest {

    @Test
    public void testDbConnection() {
        Connection conn = null;
        try {
            conn = (Connection) DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/bugtracker?user=root&password=");
            Statement st = (Statement) conn.createStatement();
            ResultSet resultSet = st.executeQuery("select id,username,password,email from mantis_user_table");
            Users users = new Users();
            while (resultSet.next()) {
                users.add(new UserData().withId(resultSet.getInt("id")).
                        withUsername(resultSet.getString("username"))
                        .withPassword(resultSet.getString("password"))
                        .withEmail(resultSet.getString("email")));
            }
            resultSet.close();
            st.close();
            conn.close();
            System.out.println(users);

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError" + ex.getErrorCode());
        }
    }
}
