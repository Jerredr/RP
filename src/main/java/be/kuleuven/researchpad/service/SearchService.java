package be.kuleuven.researchpad.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface SearchService {
	
	public be.kuleuven.researchpad.domain.Document search(String title);
	
	public be.kuleuven.researchpad.domain.Document completeDocument(be.kuleuven.researchpad.domain.Document doc);
	
	public List<be.kuleuven.researchpad.domain.Document> searchResults(String title);
	
	public int importByDoi(String doi);
	
	public List<be.kuleuven.researchpad.domain.Document> searchAuthorDocs(String firstName, String lastName);
	
	public int getDocumentType(String doi);

}
