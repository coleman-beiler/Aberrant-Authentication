//package API;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import javax.sql.DataSource;
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//public class DatabaseController{
//        private static DataSource datasource;
//        public static DataSource getDataSource(){
//            if(datasource == null) {
//                HikariConfig config = new HikariConfig();
//                config.setJdbcUrl("jdbc:mysql://localhost/authentication?useSSL=false");
//                config.setUsername("root");
//                config.setPassword("Passw0rd");
//                config.setMaximumPoolSize(10);
//                config.setAutoCommit(false);
//                config.addDataSourceProperty("cachePrepStmts", "true");
//                config.addDataSourceProperty("prepStmtCacheSize", "250");
//                config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//                datasource = new HikariDataSource(config);
//            }
//            return datasource;
//        }
//        public void start(){
//        Connection connection = null;
//        PreparedStatement stmt = null;
//        ResultSet resultSet = null;
//                try{
//                    DataSource dataSource = DatabaseController.getDataSource();
//                    connection = dataSource.getConnection();
//                    stmt = connection.prepareStatement("SELECT * FROM ACCOUNT_LOGIN_CREDENTIALS;");
//                    resultSet = stmt.executeQuery();
//                    while (resultSet.next()){
//                            System.out.println(resultSet.getString(1) + "," + resultSet.getString(2) + "," + resultSet.getString(3) + "," + resultSet.getString(4));
//                    }
//                }
//                catch (Exception e){
//                        try{
//                                connection.rollback();
//                        }
//                        catch (SQLException e1)
//                        {
//                                e1.printStackTrace();
//                        }
//                        e.printStackTrace();
//                }
//        }
//
//
//}
