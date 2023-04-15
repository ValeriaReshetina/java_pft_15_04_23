package ru.stqa.pft.addressbook.tests;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnectionTest {

    @Test
    public void testDbConnection() {
        Connection conn = null;
        try {
            conn = (Connection) DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/addressbook?user=root&password=");
            Statement st = (Statement) conn.createStatement();
            ResultSet rs = st.executeQuery("select group_id,group_name,group_header,group_footer from group_list");
            Groups groups = new Groups();
            while (rs.next()) {
                groups.add(new GroupData().withId(rs.getInt("group_id")).withName(rs.getString("group_name"))
                        .withHeader(rs.getString("group_header"))
                        .withFooter(rs.getString("group_footer")));
            }
            rs.close();
            st.close();
            conn.close();
            System.out.println(groups);

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError" + ex.getErrorCode());
        }
    }
}
