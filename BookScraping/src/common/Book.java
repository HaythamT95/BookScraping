package common;

public class Book {
	private String author;
	private String name;
	private String link;
	private String PDFlink;
	private String imgLink;

	public Book(String author, String name, String link, String pDFlink, String imgLink) {
		this.author = author;
		this.name = name;
		this.link = link;
		this.PDFlink = pDFlink;
		this.imgLink = imgLink;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPDFlink() {
		return PDFlink;
	}

	public void setPDFlink(String pDFlink) {
		PDFlink = pDFlink;
	}

	public String getImgLink() {
		return imgLink;
	}

	public void setImgLink(String imgLink) {
		this.imgLink = imgLink;
	}
	
	@Override
	public String toString() {
		return "Book [author=" + author + ", name=" + name +  "]";
	}
}
