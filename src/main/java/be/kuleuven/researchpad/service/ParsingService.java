package be.kuleuven.researchpad.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface ParsingService {
	
	public void parsePdf(String url);
}
