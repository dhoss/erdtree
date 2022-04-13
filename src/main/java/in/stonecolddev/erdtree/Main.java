package in.stonecolddev.erdtree;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Main {

    public record Comment(Integer id, String message, List<Integer> path, Integer parentId, Integer depth){}
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
                             SELECT id, message, path, depth, parent_id FROM cte
                             ORDER BY path;
                         """
             );
             ResultSet rs = pst.executeQuery()
        ) {
            HashMap<Integer, Comment> commentMap = new HashMap<>();
            while (rs.next()) {
              //  Optional<Integer> maybeParentId = Optional.ofNullable(rs.getInt("parent_id"));
                int id = rs.getInt("id");
                commentMap.put(
                        id,
                        new Comment(
                                id,
                                rs.getString("message"),
                                Arrays.asList((Integer[]) rs.getArray("path").getArray()),
                                rs.getInt("parent_id"),
                                rs.getInt("depth")));
              //  if (maybeParentId.isPresent()) {
              //      rootNode = new TreeNode<>(currentComment);
              //  }
               }

            for (var comment : commentMap.entrySet()) {
                System.out.printf(
                        """
                                %s -> %s
                                """, comment.getKey(), comment.getValue()
                );
            }

        }
    }
}