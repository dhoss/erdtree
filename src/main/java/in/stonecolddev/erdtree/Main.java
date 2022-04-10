package in.stonecolddev.erdtree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        try (Connection con = DataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(
                     """
                         WITH RECURSIVE cte (id, message, path, parent_id, depth)  AS (
                             SELECT  id,
                                 message,
                                 array[id] AS path,
                                 parent_id,
                                 1 AS depth
                             FROM    comments
                             WHERE   parent_id IS NULL
                                                  
                             UNION ALL
                                                  
                             SELECT  comments.id,
                                 comments.message,
                                 cte.path || comments.id,
                                 comments.parent_id,
                                 cte.depth + 1 AS depth
                             FROM    comments
                             JOIN cte ON comments.parent_id = cte.id
                             )
                             SELECT id, message, path, depth FROM cte
                             ORDER BY path;
                         """
             );
             ResultSet rs = pst.executeQuery()
        ) {
            System.out.println("id message path depth");
            while (rs.next()) {
                System.out.printf(
                        """
                                %d %s %s %d
                                """,
                        rs.getInt("id"),
                        rs.getString("message"),
                        rs.getString("path"),
                        rs.getInt("depth")
                );
            }
        }
    }
}