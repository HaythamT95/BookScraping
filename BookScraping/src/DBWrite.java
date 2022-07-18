import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.Book;

public class DBWrite {
	public static Connection conn;

	// Start the database connection
	//
	@SuppressWarnings("deprecation")
	public static void connectToDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			System.out.println("Driver definition failed");
		}

		try {
			String schemaName = "books";
			String connectionName = "root";
			String connectionPassword = "Aa123456";

			conn = DriverManager.getConnection("jdbc:mysql://localhost/" + schemaName + "?serverTimezone=IST",
					connectionName, connectionPassword);
			System.out.println("SQL connection succeed");

		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	//create DB table if not exists
	public static void createTable() {
		try {
			PreparedStatement ps = conn.prepareStatement(
					"CREATE TABLE IF NOT EXISTS book(idBook int AUTO_INCREMENT, bookName varchar(45), author varchar(45), bookPDF longblob, bookCover blob, PRIMARY KEY(idBook,bookName))");
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("Create table finished");
		}
	}

	// Read books from computer and send it to insertBook function
	public static void ReadBooksFromComputer() {
		// TODO Auto-generated method stub
		File folder = new File("D:\\PDFBooks\\");
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile() && file.toString().endsWith(".pdf")) {
				System.out.println(file.getName());
				int lengthTrimFrom = file.toString().indexOf("by");
				int lengthTrimTo = file.toString().length() - 4;
				String author = file.toString().substring(lengthTrimFrom + 3, lengthTrimTo);
				String bookName = file.toString().substring(file.toString().indexOf("D:\\PDFBooks\\") + 12,
						lengthTrimFrom - 1);

				Book book = new Book(author, bookName, "", "", "");
				System.out.println(book.toString());
				insertBook(book);
			}
		}
	}

	// Insert book details into database
	public static void insertBook(Book book) {
		try {
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM " + "book WHERE bookName= ? ");
			System.out.println(book.getName());
			preparedStatement.setString(1, book.getName());
			ResultSet rs = preparedStatement.executeQuery();
			// book does not exist
			if (!rs.isBeforeFirst()) {
				PreparedStatement ps = conn
						.prepareStatement("insert into book(bookName,author,bookPDF,bookCover) values (?,?,?,?)");
				InputStream image = new FileInputStream(new File("D:\\PDFBooks\\" + book.getName() + ".jpg"));
				InputStream pdf = new FileInputStream(
						new File("D:\\PDFBooks\\" + book.getName() + " by " + book.getAuthor() + ".pdf"));
				ps.setString(1, book.getName());
				ps.setString(2, book.getAuthor());
				ps.setBlob(3, pdf);
				ps.setBlob(4, image);
				ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
