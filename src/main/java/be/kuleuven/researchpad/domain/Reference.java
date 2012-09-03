package be.kuleuven.researchpad.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Reference extends Base {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "document_id")
	private Document document;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "reference_id")
	private Document referenced;
	
	@Column
	private String name;
	
	@Column
	private String page;
	
	@Column(columnDefinition="TEXT")
	private String fullReference;
	
	@Column
	private String type;
	
	@Column
	private int rank = 0;

	public Reference() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Document getReferenced() {
		return referenced;
	}

	public void setReferenced(Document referenced) {
		this.referenced = referenced;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getFullReference() {
		return fullReference;
	}

	public void setFullReference(String fullReference) {
		this.fullReference = fullReference;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}	

}
