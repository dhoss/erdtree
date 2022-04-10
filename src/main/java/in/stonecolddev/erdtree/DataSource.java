package in.stonecolddev.erdtree;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

  private static HikariConfig config = new HikariConfig();
  private static HikariDataSource ds;

  static {
      config.setJdbcUrl("jdbc:postgresql://localhost/posts");
      config.setUsername("posts");
      config.setPassword("posts");
      config.addDataSourceProperty( "cachePrepStmts" , "true" );
      config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
      config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
      ds = new HikariDataSource(config);
  }

  private DataSource() {}

  public static Connection getConnection() throws SQLException {
      return ds.getConnection();
  }
}
