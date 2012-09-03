package be.kuleuven.researchpad.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table
public class Document extends Base {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	
	@Column
	private String name;
	
	@Column(columnDefinition="LONGTEXT")
	private String content;
	
	@Column
	private String publicationYear;
	
	@OneToMany(mappedBy = "document")
	private Set<PersonDocument> personDocuments = new HashSet<PersonDocument>(0);
	
	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "AUTHOR_DOCUMENT", joinColumns = { 
			@JoinColumn(name = "DOCUMENT_ID") }, 
			inverseJoinColumns = { @JoinColumn(name = "AUTHOR_ID") })
	private List<Author> authors = new ArrayList<Author>();
	
	@OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@OrderBy("rank")
	private Set<Reference> references = new HashSet<Reference>(0);
	
	@OneToMany(mappedBy = "referenced", fetch=FetchType.EAGER)
	private Set<Reference> referenced = new HashSet<Reference>(0);
	
	@Column
	private String url;
	
	@Column(columnDefinition="TEXT")
	private String introduction;
	
	@Column
	private boolean complete = false;
	
	@Column
	private String doi;
	
	/**
	 * 0 : not found
	 * 1 : found, content is parsed to HTML
	 * 2 : PDF found
	 */
	@Column
	private int type = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<PersonDocument> getPersonDocuments() {
		return this.personDocuments;
	}

	public void setPersonDocuments(Set<PersonDocument> personDocuments) {
		this.personDocuments = personDocuments;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public String getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}

	public Set<Reference> getReferences() {
		return references;
	}

	public void setReferences(Set<Reference> references) {
		this.references = references;
	}

	public Set<Reference> getReferenced() {
		return referenced;
	}

	public void setReferenced(Set<Reference> referenced) {
		this.referenced = referenced;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	@Override
	public String toString() {
		String content = "Title: " + this.getName() + "\n";
		content += "Publication year: " + this.getPublicationYear() + "\n";
		content += "Url: " + this.getUrl() + "\n";
		content += "Introduction: " + this.getIntroduction() + "\n";
		content += "Content: " + this.getContent() + "\n";
		
		return content;
	}
	
	public void addReference(Reference ref) {
		this.references.add(ref);
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}
	
	public void addAuthor(Author a) {
		this.authors.add(a);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
