package game;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestSQL {

	public static void main(String[] args) {
		
		//declare a connection and sql statement to execute
		Connection conn = null;
		
		try {
			
			//load the database driver
			Class.forName("org.sqlite.JDBC");
			System.out.print("Driver Loaded");
			
			//create connection string and connect to database
			String dbURL = "jdbc:sqlite:products.db";
			conn = DriverManager.getConnection(dbURL);
			
			if (conn != null) {
				System.out.println("connected to database");
				
				//show meta data for database
				DatabaseMetaData db = (DatabaseMetaData) conn.getMetaData();
				System.out.println("Driver Name: " + db.getDriverName());
				System.out.println("Driver Version: " + db.getDriverVersion());
				System.out.println("Product Name: " + db.getDatabaseProductName());
				System.out.println("Product Version: " + db.getDatabaseProductVersion());
				
				//create table using prepared statement
                String sqlCreateTable = "CREATE TABLE IF NOT EXISTS PLAYERS " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " NAME TEXT NOT NULL, " +
                        " SCORE INT NOT NULL)";

                try (PreparedStatement pstmtCreateTable = conn.prepareStatement(sqlCreateTable)) {
                	pstmtCreateTable.executeUpdate();
                	System.out.println("Table Successfully Created");
                }
                
				//insert data using a prepared statement
//                String sqlInsert = "INSERT INTO PLAYERS (NAME, SCORE) VALUES (?, ?)";
//                try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
//
//                	//execute calls to prepared statement
//                	pstmtInsert.setString(1, "Fiona");
//                	pstmtInsert.setInt(2, 0);
//                	pstmtInsert.executeUpdate();
//                	
//                	System.out.println("1 records inserted");
//                }

				//select data
                String sqlSelect = "SELECT * FROM PLAYERS";
                try (PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {
                	
                	ResultSet rs = pstmtSelect.executeQuery();
                	DisplayRecords(rs);
                	rs.close();
                	
                }
				/*
				//update data
                String sqlUpdate = "UPDATE COMPANY SET SALARY = ? WHERE id = ?";
                try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {

                	pstmtUpdate.setDouble(1, 88888.88);
                	pstmtUpdate.setInt(2, 1);
                	pstmtUpdate.executeUpdate();
                	
                }
                
                System.out.println("Results after Update");
                try (PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {
                    ResultSet rs = pstmtSelect.executeQuery();
                    DisplayRecords(rs);
                    rs.close();
                }

				//delete data
                String sqlDelete = "DELETE FROM COMPANY WHERE id = ?";
                try (PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete)) {
                    pstmtDelete.setInt(1, 3);
                    pstmtDelete.executeUpdate();
                }
                
                try (PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {
                    ResultSet rs = pstmtSelect.executeQuery();
                    DisplayRecords(rs);
                    rs.close();
                }
				
				//LIMIT query
                System.out.println("Results after LIMIT 1");
				String sqlLimit = "SELECT * FROM COMPANY ORDER BY id DESC LIMIT 1";
                try (PreparedStatement pstmtSelectLimit = conn.prepareStatement(sqlLimit)) {
                    ResultSet rs = pstmtSelectLimit.executeQuery();
                    DisplayRecords(rs);
                    rs.close();
                }

				//select Query with filter 
                System.out.println("Results after LIKE");
				String sqlSelectFilter = 
						"SELECT * FROM COMPANY WHERE name LIKE ?";
                try (PreparedStatement pstmtSelectLike = 
                		      conn.prepareStatement(sqlSelectFilter)) {
                	pstmtSelectLike.setString(1, "Co%");
                    ResultSet rs = pstmtSelectLike.executeQuery();
                    DisplayRecords(rs);
                    rs.close();
                }*/

			}
			
			//close connection 
			conn.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
    public static void DisplayRecords(ResultSet rs) throws SQLException {
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int score = rs.getInt("score");
            
            System.out.println("/////////////////////////");
            System.out.println("ID: " + id);
            System.out.println("Name: " + name);
            System.out.println("Score: " + score);
        }
    }
    
}
