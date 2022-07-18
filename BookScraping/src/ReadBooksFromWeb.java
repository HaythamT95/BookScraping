import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import common.Book;

public class ReadBooksFromWeb {
	public static void ReadBooks() throws IOException {
		// Make a URL to the web page
		Document doc = Jsoup.connect("https://openlibrary.org/").userAgent("Chrome/103.0.5060.114").get();
		Element readableBooks1 = doc.select("div.carousel-section").get(2);//get second div (might need to change later according to website)
		Elements readableBooks = readableBooks1.select("div.cta-button-group");
		Set<String> aHref = new HashSet<>();// Save book link
		String BorrowOrRead = "";// Is book borrow or read?
		String author, bookName, image, pdf;
		author = bookName = image = pdf = "";
		Set<Book> books = new HashSet<>();// Save readable and download able books

		//create Directory to save books
		File directory = new File("D:\\PDFBooks\\");
	    if (! directory.exists()){
	        directory.mkdir();
	    }
		
		// save link for each book
		Elements e1 = doc.select("div.book-cover > a");
		Set<String> LinkForBooks = new HashSet<>();
		for (Element i : e1) {
			String str = i.attr("href");
			LinkForBooks.add("https://openlibrary.org/" + str);
		}
		
		System.out.println(LinkForBooks.size());
		int counter = 0;
		Loop: for (String link : LinkForBooks) {
			doc = Jsoup.connect(link).userAgent("Chrome/103.0.5060.114").timeout(45000).get();
			for (Element repository : readableBooks) {
				BorrowOrRead = repository.getElementsByTag("a").first().text();
				System.out.println("***" + BorrowOrRead);
				if (BorrowOrRead.equals("Read")) {
					Elements img = doc.select("img[class=\"cover\"]");
					for (Element im : img) {
						String url = im.attr("src");
						if (url.endsWith("M.jpg")) {
							image = url;
							System.out.println(url);
						}
					}
					// save author name
					Elements a = doc.select("a[itemprop=author]");
					for (Element span : a) {
						author = span.text();
						System.out.println(span.text());
					}
					// save book name
					Elements h1 = doc.select("h1[itemprop=name]");
					for (Element name : h1) {
						bookName = name.text();
						System.out.println(name.text());
					}
					// save book pdf
					Elements e2 = doc.select("a[href]");
					for (Element e : e2) {
						String url = e.attr("href");
						if (url.endsWith(".pdf")) {
							counter++;
							aHref.add(url);
							pdf = url;
							Book book = new Book(author, bookName, link, pdf, image);
							URL u = new URL("https:" + book.getImgLink());
							BufferedImage img1 = ImageIO.read(u);
							File file = new File("D:\\PDFBooks\\" + book.getName() + ".jpg");
							ImageIO.write(img1, "jpg", file);
							books.add(book);
							System.out.println(url);
							if (counter == 2)
								break Loop;
						}
					}
				}
				break;
			}
		}

		Download download;
		File out;
		// add all book pdf links to a set aHref.size()
		for (Book book : books) {
			try {
				doc = Jsoup.connect(book.getPDFlink()).ignoreContentType(true).get();
				out = new File("D:\\PDFBooks\\" + book.getName() + " by " + book.getAuthor() + ".pdf");
				download = new Download(book.getPDFlink(), out);
				download.saveToComputer(download.getOut());
			}
			catch(Exception e) {
				System.out.println("error");
			}
		} // end loop

	}
}
