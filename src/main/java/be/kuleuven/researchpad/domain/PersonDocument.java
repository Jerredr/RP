package be.kuleuven.researchpad.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.IndexColumn;

/**
 * TODO
 * http://www.mkyong.com/hibernate/hibernate-many-to-many-example-join-table
 * -extra-column-annotation/ Alternative (without PersonDocumentId class):
 * http://pastebin.com/70z5ALwx Skill:
 * 
 * @OneToMany(mappedBy = "skill") private Set<SkillProfile> skillProfiles;
 * 
 *                     Profile:
 * @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL) private
 *                     Set<SkillProfile> skillProfiles;
 * 
 * @author Jeroen
 * 
 */

@Entity
@Table
public class PersonDocument extends Base implements Serializable {

	private static final long serialVersionUID = 1296120051929234440L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PERSON_DOCUMENT_ID")
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "IMPORT_DATE", nullable = false, length = 10)
	private Date importDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECENT_DATE", nullable = false, length = 10)
	private Date recentDate;

	@ManyToOne
	@JoinColumn(name = "person_id")
	private Person person;

	@ManyToOne
	@JoinColumn(name = "document_id")
	private Document document;

	@Column(columnDefinition = "TEXT")
	private String notes;

	@OneToMany(mappedBy = "personDocument", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@IndexColumn(name="id")
	private Set<RatingItem> ratingItems = new HashSet<RatingItem>(0);
	
	@Column(columnDefinition = "tinyint(5) default 0")
	private int rating;

	public PersonDocument() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public Date getRecentDate() {
		return recentDate;
	}

	public void setRecentDate(Date recentDate) {
		this.recentDate = recentDate;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Set<RatingItem> getRatingItems() {
		return ratingItems;
	}

	public void setRatingItems(Set<RatingItem> ratingItems) {
		this.ratingItems = ratingItems;
	}

	public void addRatingItem(RatingItem item) {
		this.ratingItems.add(item);
	}

	public void removeRatingItem(RatingItem item) {
		this.ratingItems.remove(item);
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

}
