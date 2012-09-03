package be.kuleuven.researchpad.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.XPathOperations;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import be.kuleuven.researchpad.dao.AuthorDao;
import be.kuleuven.researchpad.dao.DocumentDao;
import be.kuleuven.researchpad.domain.Author;

@Service
@Transactional
public class SearchServiceImpl implements SearchService {

	@Autowired
	private AuthorDao authorDao;

	@Autowired
	private DocumentDao documentDao;

	private static String openAccessKey = "hpsmj73cm2q8hytrm5gzkcjn";
	private static String metaDataKey = "x36b3f3qaxnzt2h9ea28283p";
	@SuppressWarnings("unused")
	private static String imagesKey = "aybbpeujp6n4ze89c3ekcj2j";

	/**
	 * 0: not found
	 * 1: parsable
	 * 2: PDF
	 */
	public int getDocumentType(String doi) {
		// wait function (otherwise the API gets overloaded)
		try {
			synchronized(this) {
				Thread.sleep(1000); // do nothing for 1000 miliseconds (1 second)
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		try {
			String query = "http://api.springer.com/openaccess/app?q=doi:"
					+ doi + "&api_key=" + openAccessKey + "";
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc2 = builder.parse(query);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
	
			NamespaceContext ctx = new NamespaceContext() {
				public String getNamespaceURI(String prefix) {
					String uri;
					if (prefix.equals("meta")) {
						uri = "http://www.springer.com/app/meta";
					} else if(prefix.equals("pam")) {
						uri = "http://prismstandard.org/namespaces/pam/2.0/";
					} else if(prefix.equals("prism")) {
						uri = "http://prismstandard.org/namespaces/basic/2.0/";
					} else if(prefix.equals("dc")) {
						uri = "http://purl.org/dc/elements/1.1/";
					} else if(prefix.equals("xhtml")) {
						uri = "http://www.w3.org/1999/xhtml";
					} else {
						uri = null;
					}
					return uri;
				}
	
				@SuppressWarnings("rawtypes")
				public Iterator getPrefixes(String val) {
					return null;
				}
	
				public String getPrefix(String uri) {
					return null;
				}
			};
	
			xpath.setNamespaceContext(ctx);
	
			// Retrieve data
			NodeList nl = (NodeList) xpath.evaluate(
					"//response/records/result[1]/Publisher/Journal/Volume/Issue/Article/Body", doc2,
					XPathConstants.NODESET);
			if(nl.item(0) != null && nl.item(0).getChildNodes().getLength() > 3) {
				return 1;
			}
			nl = (NodeList) xpath.evaluate(
					"//response/records/result[1]/Publisher/Journal/Volume/Issue/Article/BodyRef", doc2,
					XPathConstants.NODESET);
			for(int i = 0; i < nl.getLength(); i++) {
				NamedNodeMap attributes = nl.item(i).getAttributes();
				Attr affil = (Attr) attributes
						.getNamedItem("TargetType");
				if(affil != null && affil.getValue().equals("OnlinePDF")) {
					return 2;
				}
			}
			
			
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} 
	}
	
	public List<be.kuleuven.researchpad.domain.Document> searchAuthorDocs(String firstName, String lastName) {
		List<be.kuleuven.researchpad.domain.Document> docs = new ArrayList<be.kuleuven.researchpad.domain.Document>();
		
		try {
			String query = "http://api.springer.com/metadata/pam?q=name:" + firstName + " " + lastName + "&api_key=" + metaDataKey + "";
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc2 = builder.parse(query);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
	
			NamespaceContext ctx = new NamespaceContext() {
				public String getNamespaceURI(String prefix) {
					String uri;
					if (prefix.equals("meta")) {
						uri = "http://www.springer.com/app/meta";
					} else if(prefix.equals("pam")) {
						uri = "http://prismstandard.org/namespaces/pam/2.0/";
					} else if(prefix.equals("prism")) {
						uri = "http://prismstandard.org/namespaces/basic/2.0/";
					} else if(prefix.equals("dc")) {
						uri = "http://purl.org/dc/elements/1.1/";
					} else if(prefix.equals("xhtml")) {
						uri = "http://www.w3.org/1999/xhtml";
					} else {
						uri = null;
					}
					return uri;
				}
	
				@SuppressWarnings("rawtypes")
				public Iterator getPrefixes(String val) {
					return null;
				}
	
				public String getPrefix(String uri) {
					return null;
				}
			};
	
			xpath.setNamespaceContext(ctx);
	
			// Retrieve data
			NodeList nl = (NodeList) xpath.evaluate(
					"//response/records/pam:message/xhtml:head/pam:article", doc2,
					XPathConstants.NODESET);
			for(int i = 0; i < nl.getLength(); i++) {
				NodeList childs = nl.item(i).getChildNodes();
				be.kuleuven.researchpad.domain.Document doc = new be.kuleuven.researchpad.domain.Document();
				for(int j = 0; j < childs.getLength(); j++) {
					if(childs.item(j).getNodeName().equals("prism:doi")) {
						doc.setDoi(childs.item(j).getTextContent());
					} else if(childs.item(j).getNodeName().equals("dc:title")) {
						doc.setName(childs.item(j).getTextContent());
					} else if(childs.item(j).getNodeName().equals("prism:publicationDate")) {
						doc.setPublicationYear(childs.item(j).getTextContent().substring(0,4));
					} else if(childs.item(j).getNodeName().equals("dc:creator")) {
						String[] parts = childs.item(j).getTextContent().split(",");
						Author a = new Author();
						a.setFirstName(parts[1]);
						a.setLastName(parts[0]);
						Author foundAuthor = authorDao.findAuthorByName(a.getLastName(), a.getFirstName());
						if(foundAuthor == null) {
							foundAuthor = a;
							authorDao.create(foundAuthor);
						}
						doc.addAuthor(foundAuthor);
					}
				}
				be.kuleuven.researchpad.domain.Document found = documentDao.findDocumentByTitle(doc.getName());
				if(found == null) {
					found = doc;
				}
				docs.add(found);
			}
			
			return docs;
		} catch (Exception e) {
			return docs;
		}
 	}

	public int importByDoi(String doi) {
		try {
			String query = "http://api.springer.com/openaccess/app?q=doi:"
					+ doi + "&api_key=" + openAccessKey + "";
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc2 = builder.parse(query);
			
			be.kuleuven.researchpad.domain.Document doc = new be.kuleuven.researchpad.domain.Document();

			doc = this.createDocFromNode(doc2, doc);
			documentDao.create(doc);
			return doc.getId();
		} catch (Exception ex) {
			// TODO logging
			return 0;
		}
	}

	private be.kuleuven.researchpad.domain.Document createDocFromNode(
			org.w3c.dom.Document doc2, be.kuleuven.researchpad.domain.Document doc) throws XPathExpressionException, IOException {

		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();

		NamespaceContext ctx = new NamespaceContext() {
			public String getNamespaceURI(String prefix) {
				String uri;
				if (prefix.equals("meta"))
					uri = "http://www.springer.com/app/meta";
				else
					uri = null;
				return uri;
			}

			@SuppressWarnings("rawtypes")
			public Iterator getPrefixes(String val) {
				return null;
			}

			public String getPrefix(String uri) {
				return null;
			}
		};

		xpath.setNamespaceContext(ctx);

		// META DATA
		Node nl = (Node) xpath.evaluate(
				"//response/records/result[1]/Publisher/meta:Info", doc2,
				XPathConstants.NODE);
		// check whether there are results:
		if(nl == null) {
			return null;
		}
		
		// Node nl = (Node) expr.evaluate(doc2, XPathConstants.NODE);
		NodeList metaInfo = nl.getChildNodes();
		String title = "";
		for(int i = 0; i < metaInfo.getLength(); i++) {
			if(metaInfo.item(i).getNodeName().equals("meta:Title")) {
				title = metaInfo.item(i).getTextContent();
				break;
			}
		}
		doc.setType(0);		// default: no content found
		doc.setName(title);
		// check whether the document doesn't already exist
		be.kuleuven.researchpad.domain.Document contained = documentDao.findDocumentByTitle(title);
		if(contained != null) {
			if(contained.isComplete()) {
				// doc in system and complete: return it
				return contained;
			} else {
				// doc not in system and not complete
				doc = contained;
			}
		}
		for(int i = 0; i < metaInfo.getLength(); i++) {
			if(metaInfo.item(i).getNodeName().equals("meta:DOI")) {
				doc.setDoi(metaInfo.item(i).getTextContent());
			} else if(metaInfo.item(i).getNodeName().equals("meta:DOI")) {
				doc.setPublicationYear(metaInfo.item(i).getTextContent().substring(0, 4));
			} else if(metaInfo.item(i).getNodeName().equals("meta:MetaPressId")) {
				String url = "http://www.springerlink.com/content/" + metaInfo.item(i).getTextContent();
				// check if PDF exists
				URL u = new URL(url+"/fulltext.pdf"); 
				HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection (); 
				huc.setRequestMethod ("GET"); 
				huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
				huc.connect () ; 
				if(huc.getResponseCode () == HttpURLConnection.HTTP_OK) {
					url = url+"/fulltext.pdf";
					doc.setType(2);
				}
				
				doc.setUrl(url);
			}
		}

		// AUTHOR INFO
		nl = (Node) xpath
				.evaluate(
						"//response/records/result[1]/Publisher/Journal/Volume/Issue/Article/ArticleHeader/AuthorGroup",
						doc2, XPathConstants.NODE);
		NodeList authorGroup = nl.getChildNodes();
		for (int i = 1; i < authorGroup.getLength(); i++) {
			try {
				// AUTHOR
				if (authorGroup.item(i).getNodeName().equals("Author")) {
					NodeList authorInfo = authorGroup.item(i).getChildNodes();
					
					NodeList names = null;
					String firstName = "";
					String lastName = "";
					if(authorInfo.getLength() == 1) {
						names = authorInfo.item(0).getChildNodes();
						firstName = names.item(0).getTextContent();
						lastName = names.item(1).getTextContent();
					} else {
						names = authorInfo.item(1).getChildNodes();
						firstName = names.item(1).getTextContent();
						lastName = names.item(3).getTextContent();
					}
					NodeList contact = null;
					if(authorInfo.getLength() > 2) {
						contact = authorInfo.item(3).getChildNodes();
					}
	
					Author author = authorDao.findAuthorByName(lastName, firstName);
					if (author == null) {
						author = new Author();
						author.setFirstName(firstName);
						author.setLastName(lastName);
						if(contact != null) {
							author.setEmail(contact.item(1).getTextContent());
						}
	
						NamedNodeMap attributes = authorGroup.item(i)
								.getAttributes();
						Attr affil = (Attr) attributes
								.getNamedItem("AffiliationIDS");
						if (affil != null) {
							String[] affiliates = affil.getTextContent().split(" ");
							// only get info from first linked affiliation
							Node affiliation = (Node) xpath
									.evaluate(
											"//response/records/result[1]/Publisher/Journal/Volume/Issue/Article/ArticleHeader/AuthorGroup/Affiliation[@ID='"
													+ affiliates[0] + "'][1]",
											doc2, XPathConstants.NODE);
							NodeList affiliateInfo = affiliation.getChildNodes();
							author.setUniversity(affiliateInfo.item(1)
									.getTextContent());
						}
						authorDao.create(author);
					}
					doc.addAuthor(author);
				}
			} catch(Exception e) {
				// nothing
			}
		}

		// CONTENT
		try {		
			nl = (Node) xpath
					.evaluate(
							"//response/records/result[1]/Publisher/Journal/Volume/Issue/Article/Body",
							doc2, XPathConstants.NODE);
			String content = "The document text isn't available";
			if(nl != null) { 
				DOMSource source = new DOMSource(nl);
				// preprocessing	
				StringReader xsl = new StringReader(
						"<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'>"
								+ "<xsl:template match='@* | node()'>"
								+ 	"<xsl:copy>"
								+ 		"<xsl:apply-templates select='@* | node()' />"
								+ 	"</xsl:copy>"
								+ "</xsl:template>"
								+ "<xsl:template match='Section1'>"
								+ 	"<xsl:apply-templates select='@* | node()' />"
								+ "</xsl:template>"
								+ "<xsl:template match='Section2'>"
								+ 	"<xsl:apply-templates select='@* | node()' />"
								+ "</xsl:template>"
								+ "<xsl:template match='Section3'>"
								+ 	"<xsl:apply-templates select='@* | node()' />"
								+ "</xsl:template>"
								+ "<xsl:template match='Heading'>"
								+ 	"<h2>"
								+ 		"<xsl:apply-templates select='@* | node()' />"
								+ 	"</h2>"
								+ "</xsl:template>"
								+ "<xsl:template match='Para'>"
								+ 	"<p>"
								+ 		"<xsl:apply-templates select='@* | node()' />"
								+ 	"</p>"
								+ "</xsl:template>"
								+ "<xsl:template match='CitationRef[@CitationID]'>"
								+ 	"<span class='ref' id='/ref/document/{@CitationID}' onclick='clickFunctionxcx' data-role='button' data-inline='true'>"
								+ 		"<xsl:apply-templates />"
								+ 	"</span>"
								+ "</xsl:template>"
						+ "</xsl:stylesheet>");
				StringWriter writer = new StringWriter();
				TransformerFactory tFactory = TransformerFactory.newInstance();
				Transformer transformer = tFactory
						.newTransformer(new javax.xml.transform.stream.StreamSource(
								xsl));
	
				transformer.transform(source,
						new javax.xml.transform.stream.StreamResult(writer));
	
				content = writer.toString();
				String copyContent = content;
				// <Body> ... </Body> verwijderen
				
				try {
					content = content.split("<Body>")[1];
					content = content.split("</Body>")[0];
					doc.setType(1);
				} catch(Exception e) {
					content = copyContent;
				}
			} 
			
			// postprocessing
			// process references
			HashMap<Integer, be.kuleuven.researchpad.domain.Reference> citationList = new HashMap<Integer, be.kuleuven.researchpad.domain.Reference>();
			NodeList references = (NodeList) xpath.evaluate("//response/records/result[1]/Publisher/Journal/Volume/Issue/Article/ArticleBackmatter/Bibliography/Citation",doc2, XPathConstants.NODESET);
			for(int i = 0; i < references.getLength(); i++) {
				NamedNodeMap attrs = references.item(i).getAttributes();
				String refId = "";
				for(int j = 0 ; j<attrs.getLength() ; j++) {
			        Attr attribute = (Attr)attrs.item(j);  
			        if(attribute.getName().equals("ID")) {
			        	refId = attribute.getValue();
			        }
			    }
				int type = 1;
				String path = "BibArticle";
				String docName = (String) xpath.evaluate("//response/records/result[1]/Publisher/Journal/Volume/Issue/Article/ArticleBackmatter/Bibliography/Citation[@ID='" + refId + "']/BibArticle/ArticleTitle/text()",doc2, XPathConstants.STRING);
				if(docName.equals("")) {
					docName = (String) xpath.evaluate("//response/records/result[1]/Publisher/Journal/Volume/Issue/Article/ArticleBackmatter/Bibliography/Citation[@ID='" + refId + "']/BibBook/BookTitle/text()",doc2, XPathConstants.STRING);
					path = "BibBook";
					type = 2;
				}
				if(docName.equals("")) {
					docName = (String) xpath.evaluate("//response/records/result[1]/Publisher/Journal/Volume/Issue/Article/ArticleBackmatter/Bibliography/Citation[@ID='" + refId + "']/BibBook/BibChapter/text()",doc2, XPathConstants.STRING);
					path = "BibChapter";
					type = 3;
				}
				if(docName.equals("")) {
					// short reference (only BibUnstructured)
					type = 4;
				}
				be.kuleuven.researchpad.domain.Document found = null;
				if(type != 4) {
					found = documentDao.findDocumentByTitle(docName.trim());
					if(found == null) {
						// document isn't in the system yet; create it
						found = new be.kuleuven.researchpad.domain.Document();
						found.setName(docName);
						String year = (String) xpath.evaluate("//response/records/result[1]/Publisher/Journal/Volume/Issue/Article/ArticleBackmatter/Bibliography/Citation[@ID='" + refId + "']/" + path + "/Year/text()",doc2, XPathConstants.STRING);
						found.setPublicationYear(year);
						found.setComplete(false);
						NodeList authors = (NodeList) xpath.evaluate("//response/records/result[1]/Publisher/Journal/Volume/Issue/Article/ArticleBackmatter/Bibliography/Citation[@ID='" + refId + "']/" + path + "/BibAuthorName",doc2, XPathConstants.NODESET);
						// get the authors of the reference
						for(int j = 0; j < authors.getLength(); j++) {
							NodeList authorDetails = authors.item(j).getChildNodes();
							String firstName = authorDetails.item(1).getTextContent();
							String lastName = authorDetails.item(3).getTextContent();
							// check whether this author already exists
							Author a = authorDao.findAuthorByName(lastName, firstName);
							if(a == null) {
								// create author
								a = new Author();
								a.setFirstName(firstName);
								a.setLastName(lastName);
								authorDao.create(a);
							}
							found.addAuthor(a);
						}
						
						documentDao.create(found);
					}
				}
				// get refNr
				NodeList referenceContent = references.item(i).getChildNodes();
				String referenceNr = referenceContent.item(1).getTextContent().trim();
				if(referenceNr.indexOf(".") > 0) {
					referenceNr = referenceNr.substring(0, referenceNr.length()-1);
				}
				// create reference
				be.kuleuven.researchpad.domain.Reference reference = new be.kuleuven.researchpad.domain.Reference();
				reference.setDocument(doc);
				reference.setReferenced(found);
				String page = (String) xpath.evaluate("//response/records/result[1]/Publisher/Journal/Volume/Issue/Article/ArticleBackmatter/Bibliography/Citation[@ID='" + refId + "']/BibUnstructured/text()",doc2, XPathConstants.STRING);
				reference.setPage(page); // extra: VolumeID, FirstPage, LastPage	
				int referenceNumber = 0;
				try {
					referenceNumber = Integer.parseInt(referenceNr);
				} catch(NumberFormatException e) {
					
				}
				reference.setRank(referenceNumber);
				reference.setName(referenceNumber + ". ");
				doc.addReference(reference);
				
				citationList.put(referenceNumber, reference);
			}
			
			// adjust the links to refer to the actual references
			StringBuffer sb = new StringBuffer();
			// tweak the following
			//<span class="ref" id="/ref/document/B16" data-role="button" data-inline="true">16</span>
			// <span class='ref' id='/ref/document/{@CitationID}' data-role='button' data-inline='true'>
			// "<span class='ref' id='/ref/document/{@CitationID}' onclick='clickFunction(xcx)' data-role='button' data-inline='true'>"
			Pattern pattern = Pattern.compile("(<span class=\"ref\" id=\"/ref/document/)([A-Z]*[0-9]*)(\" onclick=\"clickFunctionxcx\" data-role=\"button\" data-inline=\"true\">)");
			//Pattern pattern = Pattern.compile("(<a href='document/link/)([^']*)('>)");
			Matcher matcher = pattern.matcher(content);
			while(matcher.find()) {
				String oldLinkPart = matcher.group(2);
				// get reference NR
				Pattern intsOnly = Pattern.compile("\\d+");
				Matcher makeMatch = intsOnly.matcher(oldLinkPart);
				makeMatch.find();
				String inputInt = makeMatch.group();
				citationList.get(Integer.parseInt(inputInt));
				// some references are not linked to a document, their docID should be 0
				be.kuleuven.researchpad.domain.Reference refDoc = citationList.get(Integer.parseInt(inputInt));
				int refDocId = 0;
				if(refDoc != null) {
					be.kuleuven.researchpad.domain.Document tmp = refDoc.getReferenced();
					if(tmp != null) {
						refDocId = tmp.getId();
					}
				}	
				//System.out.println(inputInt);
				//System.out.println(matcher.group(1) + refDocId + matcher.group(3));
				String lastPart = matcher.group(3).replaceFirst("xcx", "(" + refDocId + ")");
			    matcher.appendReplacement(sb, matcher.group(1) + refDocId + lastPart);
			}
			matcher.appendTail(sb);
			content = sb.toString();
			doc.setContent(content);
			doc.setComplete(true);
			
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<be.kuleuven.researchpad.domain.Document> searchResults(
			String title) {
		try {
			title = title.toLowerCase();
			RestTemplate rest = new RestTemplate();
			String query = "http://api.springer.com/openaccess/app?q=title:"
					+ title + "&api_key=" + openAccessKey + "";
			Source result = rest.getForObject(query, Source.class);
			HashMap<String, String> namespaces = new HashMap<String, String>();

			// add the meta namespace to access those elements
			namespaces.put("meta", "http://www.springer.com/app/meta");
			XPathOperations template = new Jaxp13XPathTemplate();
			((Jaxp13XPathTemplate) template).setNamespaces(namespaces);

			List<Node> records = template.evaluateAsNodeList(
					"//response/records/result/Publisher/meta:Info", result);
			List<be.kuleuven.researchpad.domain.Document> results = new ArrayList<be.kuleuven.researchpad.domain.Document>();
			for (Node n : records) {
				NodeList childs = n.getChildNodes();
				be.kuleuven.researchpad.domain.Document doc = new be.kuleuven.researchpad.domain.Document();
				for(int j = 0; j < childs.getLength(); j++) {
					if(childs.item(j).getNodeName().equals("meta:Title")) {
						doc.setName(childs.item(j).getTextContent());
					} else if(childs.item(j).getNodeName().equals("meta:Date")) {
						doc.setPublicationYear(childs.item(j).getTextContent().substring(0, 4));
					} else if(childs.item(j).getNodeName().equals("meta:DOI")) {
						doc.setDoi(childs.item(j).getTextContent());
					} else if(childs.item(j).getNodeName().equals("meta:Authors")) {
						NodeList authors = childs.item(j).getChildNodes();
						// add authors
						for (int i = 0; i < authors.getLength(); i++) {
							Author a = new Author();
							String[] name = authors.item(i).getTextContent()
									.split(", ");
							if(name.length > 1) {
								a.setFirstName(name[1]);
							}
							if(name.length > 0) {
								a.setLastName(name[0]);
							}
							doc.addAuthor(a);
						}
					}
				}
				results.add(doc);
			}
			return results;
		} catch (Exception ex) {
			// TODO logging
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}

	public be.kuleuven.researchpad.domain.Document completeDocument(
			be.kuleuven.researchpad.domain.Document doc) {
		
		try {
			String query = "http://api.springer.com/openaccess/app?q=title:"
					+ doc.getName() + "&api_key=" + openAccessKey + "";
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document doc2 = builder.parse(query);

			doc = this.createDocFromNode(doc2, doc);
			if(doc != null) {	// avoid merge with null entity
				documentDao.update(doc);
			} else {
				// doc is not found, retrieve pdf
				
			}
			
			return doc;
		} catch (Exception e) {
			// TODO log
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public be.kuleuven.researchpad.domain.Document search(String title) {
/*
		try {
			// GET DOI
			String metaPressId = this.getMetaPressIdofDocument(title);
			// GET HTML url
			be.kuleuven.researchpad.domain.Document doc = this
					.retrieveDocument(metaPressId,
							new be.kuleuven.researchpad.domain.Document());
			return doc;
		} catch (Exception e) {
			// TODO log
			System.out.println(e.getMessage());
			e.printStackTrace();
		}*/
		return null;
	}

	/*private String getMetaPressIdofDocument(String title) {
		try {
			RestTemplate rest = new RestTemplate();
			String query = "http://api.springer.com/openaccess/app?q=title:"
					+ title + "&api_key=" + openAccessKey + "";
			Source result = rest.getForObject(query, Source.class);
			HashMap<String, String> namespaces = new HashMap<String, String>();

			// add the meta namespace to access those elements
			namespaces.put("meta", "http://www.springer.com/app/meta");
			XPathOperations template = new Jaxp13XPathTemplate();
			((Jaxp13XPathTemplate) template).setNamespaces(namespaces);

			String ns = template
					.evaluateAsString(
							"//response/records/result/Publisher/meta:Info/meta:MetaPressId",
							result);

			return ns;
			// something went wrong while requesting the document
		} catch (Exception ex) {
			// TODO logging
			return "";
		}
	}*/

	/**
	 * view-source:http://www.springerlink.com/content/h1n460uk0v40u120/fulltext
	 * .html
	 * 
	 * @param metaPressId
	 * @return
	 * @throws IOException
	 */
	/*private be.kuleuven.researchpad.domain.Document retrieveDocument(
			String metaPressId, be.kuleuven.researchpad.domain.Document paper)
			throws IOException {
		// retrieve the landing page, while tricking it to believe we use
		// Firefox :)
		Document doc = Jsoup
				.connect(
						"http://www.springerlink.com/content/" + metaPressId
								+ "/fulltext.html").userAgent("Mozilla").get();
		paper.setName(doc.getElementsByClass("Heading1").text());
		Elements pubYear = doc.getElementsByClass("Affiliation");
		// publication year: last 4 chars from the string
		String year = pubYear.text().trim();
		paper.setPublicationYear(year.substring(year.length() - 4));

		// deal with the references (since they are contained within the actual
		// content)
		// references
		HashMap<Integer, be.kuleuven.researchpad.domain.Reference> citationList = new HashMap<Integer, be.kuleuven.researchpad.domain.Reference>();
		Iterator<Element> referenceIterator = doc
				.getElementsByClass("Citation").select("tr").iterator();
		while (referenceIterator.hasNext()) {
			Element row = referenceIterator.next();
			Elements columns = row.select("td");
			if (columns.size() == 2) {
				String nr = columns.get(0).text()
						.substring(0, columns.get(0).text().length() - 1);

				be.kuleuven.researchpad.domain.Reference reference = this
						.processReference(columns.get(1).text(), nr);
				paper.addReference(reference);
				reference.setDocument(paper);
				citationList.put(Integer.parseInt(nr), reference);
			}
		}
		// link references in the paper to the corresponding reference
		referenceIterator = doc.getElementsByTag("cite").iterator();
		while (referenceIterator.hasNext()) {
			Element referenceLink = referenceIterator.next();
			String referenceLinkNr = referenceLink.text();
			referenceLink.html("<span class='ref' id='/ref/document/"
					+ citationList.get(Integer.parseInt(referenceLinkNr))
							.getId()
					+ "' data-role='button' data-inline='true'>"
					+ referenceLinkNr + "</span>");
		}

		// get abstract
		Elements abstractContent = doc.getElementsByClass("Abstract");
		Elements abstractHeader = abstractContent.get(0).getElementsByClass(
				"AbstractHeading");
		String abstractHeaderText = abstractHeader.text();
		abstractHeader.html("<h2>" + abstractHeaderText + "</h2>");
		Iterator<Element> sections = doc.getElementsByClass(
				"AbstractSectionHeading").iterator();
		while (sections.hasNext()) {
			Element section = sections.next();
			String abstractHeaderSection = section.text();
			section.html("<h3>" + abstractHeaderSection + "</h3>");
		}
		// get fulltext
		Elements fullText = doc.getElementsByClass("Fulltext");
		Iterator<Element> headings = fullText.get(0)
				.getElementsByClass("heading2").iterator();
		while (headings.hasNext()) {
			Element heading = headings.next();
			String header = heading.text();
			heading.html("<h2>" + header + "</h2>");
		}
		// merge abstract and fulltext
		paper.setContent(abstractContent.html() + fullText.html());
		paper.setIntroduction(abstractContent.html());
		// set url
		paper.setUrl("http://www.springerlink.com/content/" + metaPressId
				+ "/fulltext.html");

		// authors
		Elements authors = doc.getElementsByClass("AuthorGroup");
		String[] authorBlocks = authors.html().split("sup>");
		// get the table containing the organisation information
		Iterator<Element> tableIter = doc.select("table").iterator();
		// map the number to the organisation info
		HashMap<Integer, String> organisationInfo = new HashMap<Integer, String>();
		Outer: while (tableIter.hasNext()) {
			Element organisationTable = tableIter.next();
			Iterator<Element> rowIter = organisationTable.select("tr")
					.iterator();
			while (rowIter.hasNext()) {
				Element row = rowIter.next();
				Elements columns = row.select("td");
				if (columns.size() == 2
						&& !columns.get(0).text().trim().equals("")) {
					organisationInfo.put(Integer.parseInt(columns.get(0).text()
							.substring(1, columns.get(0).text().length() - 2)),
							columns.get(1).text());
				} else if (columns.size() == 2
						&& columns.get(0).text().trim().equals("")) {
					break Outer;
				}
			}
		}
		ArrayList<Author> docAuthors = new ArrayList<Author>();
		for (int i = 0; i < authorBlocks.length; i += 2) {
			String fullName = Jsoup.parse(
					authorBlocks[i].substring(0, authorBlocks[i].length() - 1),
					"UTF-8").text();
			String[] nameParts = fullName.split("\\u00a0");

			String firstName = "";
			for (int j = 0; j < nameParts.length - 1; j++) {
				String[] tmp = nameParts[j].split(" ");
				String part = nameParts[j];
				if (tmp.length > 1) {
					part = tmp[1];
				}
				if (!part.trim().toLowerCase().equals("and")
						&& !part.trim().equals(",")) {
					firstName += part + " ";
				}
			}
			firstName = firstName.trim();
			String lastName = nameParts[nameParts.length - 1].trim();
			// check whether the author is already in the system
			Author docAuthor = authorDao.findAuthorByName(lastName, firstName);
			if (docAuthor == null) {
				// author not found: create author:
				docAuthor = new Author();
				docAuthor.setFirstName(firstName);
				docAuthor.setLastName(lastName);
				// link the organization to the author
				String[] authorRefs = authorBlocks[i + 1].split(",");
				// only the first organization, for now
				String number = Jsoup.parse(authorRefs[0], "UTF-8").text()
						.split("\\u00a0")[0].trim();
				docAuthor.setUniversity(organisationInfo.get(Integer
						.parseInt(number)));
				authorDao.create(docAuthor);
			}
			docAuthors.add(docAuthor);
		}
		paper.setAuthors(docAuthors);
		paper.setComplete(true);
		if (paper.getId() == 0) {
			documentDao.create(paper);
		} else {
			documentDao.update(paper);
		}

		return null;
	}*/

	/**
	 * TYPE 1: Organization: [link] title;ID TYPE 2: Organization: title. [link]
	 * TYPE 3: Organization: title. year. TYPE 4: author, author: [link] title:
	 * publisher; year. TYPE 5: author, author: title. publication year, (Suppl)
	 * : S45-9. (potentional PubMed indication) TYPE 6: Organization: [link]
	 * title year. TYPE 7: author: title. Publication year, page
	 * 
	 * @param reference
	 * @return
	 */
	/*private be.kuleuven.researchpad.domain.Reference processReference(
			String reference, String nr) {
		be.kuleuven.researchpad.domain.Document found;
		be.kuleuven.researchpad.domain.Reference ref = new Reference();
		ref.setName(nr);
		ref.setFullReference(reference);
		// TYPE 4: author, author: [link] title: publisher; year.
		if (reference.matches("(.*,)*.*:\\s\\[.*]\\s.*:\\s.*;.*")) {
			String[] parts = reference.split("\\]");
			String[] tmp = parts[1].split("\\:");
			String[] tmp2 = parts[0].split("\\[");
			String[] tmp3 = tmp[1].split(";");
			found = documentDao.findDocumentByTitle(tmp[0].trim());
			// document not found
			if (found == null) {
				found = new be.kuleuven.researchpad.domain.Document();
				found.setName(tmp[0].trim());
				found.setUrl(tmp2[1]);
				found.setPublicationYear(tmp3[1].trim().substring(0, 4));
				// deal with authors
				found.setAuthors(this.findAuthorsFroMCSString(tmp2[0]));
				documentDao.create(found);
			}
			ref.setReferenced(found);
			ref.setPage(tmp3[0].trim());
			ref.setType("4");
			System.out.println("TYPE 4" + reference);
			// TYPE 1: Organization: [link] title;ID
		} else if (reference.matches(".*:\\s\\[.*]\\s.*;.*")) {
			String title = reference.substring(reference.indexOf("]"),
					reference.lastIndexOf(";")).trim();
			found = documentDao.findDocumentByTitle(title);
			if (found == null) {
				found = new be.kuleuven.researchpad.domain.Document();
				found.setName(title);
				found.setUrl(reference.substring(reference.indexOf("[") + 1,
						reference.indexOf("]") - 1));
				found.setAuthors(this.findAuthorsFroMCSString(reference
						.substring(0, reference.indexOf(":"))));
			}
			ref.setReferenced(found);
			ref.setPage(reference.substring(reference.lastIndexOf(";") + 1));
			ref.setType("1");
			System.out.println("TYPE 1" + reference);
			// TYPE 2: Organization: title. [link]
		} else if (reference.matches(".*:\\s.*.\\[.*]")) {
			String title = reference.substring(reference.indexOf(":") + 1,
					reference.lastIndexOf("[") - 1);
			found = documentDao.findDocumentByTitle(title);
			if (found == null) {
				found = new be.kuleuven.researchpad.domain.Document();
				found.setName(title);
				found.setUrl(reference.substring(reference.indexOf("[") + 1,
						reference.indexOf("]")));
				found.setAuthors(this.findAuthorsFroMCSString(reference
						.substring(0, reference.indexOf(":"))));
			}
			ref.setReferenced(found);
			ref.setPage("");
			ref.setType("2");
			System.out.println("TYPE 2" + reference);
			// TYPE 6: Organization: [link] title year.
		} else if (reference.matches(".*:\\s\\[.*]\\s.*\\s\\d{4}.*")) {
			String[] tmp = reference.split("\\]");
			String[] tmp2 = tmp[1].split(" ");
			String title = tmp2[0];
			found = documentDao.findDocumentByTitle(title);
			if (found == null) {
				found = new be.kuleuven.researchpad.domain.Document();
				found.setName(title);
				String[] tmp4 = reference.split("\\[");
				String[] tmp5 = tmp4[1].split("\\]");
				found.setUrl(tmp5[0]);
				found.setAuthors(this.findAuthorsFroMCSString(tmp4[0]
						.substring(0, tmp4.length - 2)));
			}
			ref.setReferenced(found);
			ref.setPage("");
			ref.setType("6");
			System.out.println("TYPE 6" + reference);
			// TYPE 5: author, author: title. publication year, (Suppl) : S45-9.
			// (potentional PubMed indication)
		} else if (reference.matches("(.*,)+.*:\\s.*.\\s.*\\s.*")) {
			int index = reference.indexOf(".");
			String[] tmp = reference.split("\\.");
			int q = reference.indexOf("?");
			if (q > 0 && q < index) {
				index = q;
				tmp = reference.split("\\?");
			}
			String title = reference.substring(reference.indexOf(":") + 1,
					index).trim();
			found = documentDao.findDocumentByTitle(title);

			if (found == null) {
				found = new be.kuleuven.researchpad.domain.Document();
				found.setName(title);
				String[] tmp2 = tmp[1].split("\\,");
				String[] tmp3 = tmp2[tmp2.length - 2].split(" ");
				found.setPublicationYear(tmp3[tmp3.length - 1]);
				found.setAuthors(this.findAuthorsFroMCSString(reference
						.substring(0, reference.indexOf(":"))));
				documentDao.create(found);
			}
			ref.setReferenced(found);
			ref.setPage(tmp[1]);
			ref.setType("5");
			System.out.println("TYPE 5" + reference);
			// TYPE 7: author: title. Publication year, page
		} else if (reference.matches(".*:\\s.*.\\s.*,.*:\\s.*")) {
			String title = reference.substring(reference.indexOf(":") + 2,
					reference.indexOf("."));
			String[] parts = reference.split("\\:");
			String[] tmp = reference.split("\\.");
			if (reference.indexOf("?") > 0
					&& reference.indexOf(".") > reference.indexOf("?")) {
				tmp = reference.split("\\?");
			}
			String[] tmp3 = tmp[1].split("\\,");
			found = documentDao.findDocumentByTitle(title);
			if (found == null) {
				found = new be.kuleuven.researchpad.domain.Document();
				found.setName(title);
				found.setAuthors(this.findAuthorsFroMCSString(parts[0]));
				String year = tmp3[0].substring(tmp3[0].length() - 4);
				try {
					Integer.parseInt(year);
					found.setPublicationYear(year);
				} catch (NumberFormatException e) {
					found.setPublicationYear("year");
				}
				documentDao.create(found);
			}
			ref.setReferenced(found);
			ref.setPage(tmp3[1]);
			ref.setType("7");
			System.out.println("TYPE 7" + reference);
			// TYPE 3: Organization: title. year.
		} else if (reference.matches(".*:\\s.*.\\s.*")) {
			String[] tmp = reference.split("\\:");
			String[] tmp2 = tmp[1].split("\\.");
			String title = tmp2[0].trim();
			found = documentDao.findDocumentByTitle(title);
			if (found == null) {
				found = new be.kuleuven.researchpad.domain.Document();
				found.setName(title);
				found.setPublicationYear(tmp2[1].trim().substring(0, 4));
				found.setAuthors(this.findAuthorsFroMCSString(tmp[0].trim()));
			}
			ref.setReferenced(found);
			ref.setPage("");
			ref.setType("3");
			System.out.println("TYPE 3" + reference);
		} else {
			System.out.println("NOT MATCHED: " + reference);
		}

		return ref;
	}*/

	/*private List<Author> findAuthorsFroMCSString(String asList) {
		String[] authorStrings = asList.split("\\,");
		ArrayList<Author> authors = new ArrayList<Author>();
		for (String as : authorStrings) {
			int index = as.trim().indexOf(" ");
			String lastName = "";
			String firstName = "";
			if (index < 1) {
				lastName = as.trim();
			} else {
				lastName = as.substring(0, index).trim();
				firstName = as.substring(index).trim();
			}
			if (!lastName.equals("")) {
				Author a = authorDao.findAuthorByName(lastName, firstName);
				if (a == null) {
					a = new Author();
					a.setFirstName(firstName);
					a.setLastName(lastName);
					authorDao.create(a);
				}
				authors.add(a);
			}
		}
		return authors;
	}*/

}
