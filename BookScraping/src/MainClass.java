import java.io.IOException;

public class MainClass {

	public static void main(String[] args) throws IOException {
		//Call read books method
		ReadBooksFromWeb.ReadBooks();
		//connect to DB
		DBWrite.connectToDB();
		//Create table if does not exist
		DBWrite.createTable();
		//Read books from computer and save to DB
		DBWrite.ReadBooksFromComputer();
	}

}
