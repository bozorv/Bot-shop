package org.example.Databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {


	public static Connection connection()  {

		try {
			Class.forName("org.postgresql.Driver");
		Connection postgres = DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/NasibaMCHJ",
				"postgres",
				"2003"
		);
		return postgres;

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

return  null;

	}
}