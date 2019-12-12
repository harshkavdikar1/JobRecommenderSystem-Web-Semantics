package sparql_layer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.FileManager;
import org.apache.log4j.PropertyConfigurator;

public class Sparql_Query_Engine {

	static String defaultNameSpace = "http://org.semweb/assign4/people#";
	Model model = null;
	Model schema = null;
	static String locationRDF = "src/resource_data/City.rdf";
	static String personRDF = "src/resource_data/Person.rdf";
	

	public static void main(String[] args) {
		
		String log4jConfPath = "src/resource_data/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		
		StringBuilder queryString = new StringBuilder();
		queryString.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdf-schema#" + "> ");
		queryString.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdf-syntax-ns#" + "> ");
		queryString.append("PREFIX getLoc: <http://www.semanticweb.org/hp-pc/ontologies/SER-531/Team-14/Location#> ");
		queryString.append("PREFIX getPer: <http://www.semanticweb.org/hp-pc/ontologies/SER-531/Team-14/Applicants#> ");

		Sparql_Query_Engine queryObj = new Sparql_Query_Engine();
		System.out.println("Load City RDF");
		queryObj.populateAllRDF();
		//queryObj.getCityName(queryObj.model);
		//queryObj.getLatitudeLongitude("New York",queryObj.model, queryString);
		queryObj.getAllLatLongMajor("Master",queryObj.model, queryString);
	}

	private void getAllLatLongMajor(String major, Model model2, StringBuilder queryString) {
		String query_lat = "SELECT   ?loc ?lat ?long  WHERE {\n" + 
				"?s getPer:has_Name ?o .\n" + 
				"?s getPer:lives_in ?loc .\n" + 
				"?s getPer:schoolLevel \""+major+"\" .\n" + 
				"?s1 getLoc:has_Name ?o1 .\n" + 
				"?s1 getLoc:has_Latitude ?lat .\n" + 
				"?s1 getLoc:has_Longitude ?long .\n" + 
				"FILTER(?o1=?loc)\n" + 
				"}";
		queryString.append(query_lat);
		Query query = QueryFactory.create(queryString.toString());
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		try {
			ResultSet response = qexec.execSelect();

			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();
				RDFNode location = soln.get("?loc");
				RDFNode latitude = soln.get("?lat");
				RDFNode longitude = soln.get("?long");
				
				if (location != null)
					System.out.println("Location -  " + location.toString());
				else
					System.out.println("Not Found!");
				if (latitude != null)
					System.out.println("Latitude -  " + latitude.toString());
				else
					System.out.println("Not Found!");
				if (longitude != null)
					System.out.println("Longitude -  " + longitude.toString());
				else
					System.out.println("Not Found!");

			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		finally {
			qexec.close();
		}
		
		
	}

	private void getLatitudeLongitude(String city, Model model2, StringBuilder queryString) {
		
		String query_lat = "SELECT  ?lat ?long WHERE {\n" + 
				"?s getLoc:has_Name ?o .\n" + 
				"?s getLoc:has_Latitude ?lat .\n" + 
				"?s getLoc:has_Longitude ?long ;\n" + 
				"FILTER(?o =\""+city+"\")\n" + 
				"}";
		
		queryString.append(query_lat);
		Query query = QueryFactory.create(queryString.toString());
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		try {
			ResultSet response = qexec.execSelect();

			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();
				RDFNode latitude = soln.get("?lat");
				RDFNode longitude = soln.get("?long");
				
				if (latitude != null)
					System.out.println("Latitude -  " + latitude.toString());
				else
					System.out.println("Not Found!");
				if (longitude != null)
					System.out.println("Longitude -  " + longitude.toString());
				else
					System.out.println("Not Found!");

			}
		} finally {
			qexec.close();
		}
		
	}

	private void populateAllRDF() {
		model = ModelFactory.createOntologyModel();
		InputStream locationInstance = FileManager.get().open(locationRDF);
		InputStream personInstance = FileManager.get().open(personRDF);
		
		model.read(locationInstance, defaultNameSpace);
		model.read(personInstance, defaultNameSpace);
		try {
			locationInstance.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getCityName(Model model) {
		// Hello to Me - focused search
		runQuery("SELECT DISTINCT ?o WHERE {?s get:has_Name ?o FILTER(?o > \"Miami\")} LIMIT 2",
				model); // add the query string

	}

	private void runQuery(String queryRequest, Model model) {

		StringBuffer queryStr = new StringBuffer();

		// Establish Prefixes
		// Set default Name space first
		//queryStr.append("PREFIX people" + ": <" + defaultNameSpace + "> ");
		queryStr.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdf-schema#" + "> ");
		queryStr.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-rdf-syntax-ns#" + "> ");
		queryStr.append("PREFIX foaf" + ": <" + "http://xmlns.com/foaf/0.1/" + "> ");
		queryStr.append("PREFIX get: <http://www.semanticweb.org/hp-pc/ontologies/SER-531/Team-14/Location#> ");

		// Now add query
		queryStr.append(queryRequest);
		Query query = QueryFactory.create(queryStr.toString());
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		try {
			ResultSet response = qexec.execSelect();

			while (response.hasNext()) {
				QuerySolution soln = response.nextSolution();
				RDFNode name = soln.get("?o");
				if (name != null)
					System.out.println("City -  " + name.toString());
				else
					System.out.println("Not Found!");

			}
		} finally {
			qexec.close();
		}
	}

}




//
//
//URL url = new URL(serviceEndpoint);
//HttpURLConnection connection = (HttpURLConnection)  url.openConnection();
//connection.setRequestMethod("HEAD");
//connection.connect();
//String contentType = connection.getContentType();
//System.out.println(contentType);
