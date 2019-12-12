package trash;

import java.io.IOException;
import java.io.InputStream;

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

import model.LatLongPair;

public class SparqlEngine {

	static String defaultNameSpace = "http://www.semanticweb.org/mayank/ontologies/2015/10/job-recommend#";
	static String jobDescriptionRDF = "";
	static String locationRDF = "src/resource_data/City.rdf";
	static String personRDF = "";
	static String jobRecommendOwl = "/Users/mayankkataruka/Desktop/Work/ASU_Study/1st_sem/SER_531_Semantic_Web_Engineering/SER_531_workspace/Job_Reco/src/resource_data/JobRecommenderSystem.owl";

	private static Model city = null;
	private static Model schema = null;

	public SparqlEngine() throws IOException {

		populateCityRDF();
		// populateJob();
		// populateLocation();
		// populatePerson();

	}

	private void populateCityRDF() {
		// System.out.println(System.getProperty("user.dir"));
		// InputStream owlSchema = getClass().getResourceAsStream(jobRecommendOwl);
		InputStream city_rdf = FileManager.get().open(locationRDF);
		city = ModelFactory.createOntologyModel();
		city.read(city_rdf, defaultNameSpace);
		city.close();

	}

//	private void populateJob() throws IOException {
//		InputStream job = getClass().getResourceAsStream(jobDescriptionRDF);
//		model.read(job, defaultNameSpace);
//		job.close();
//
//	}
//
//	private void populateLocation() throws IOException {
//		InputStream location = getClass().getResourceAsStream(locationRDF);
//		model.read(location, defaultNameSpace);
//		location.close();
//	}
//
//	private void populatePerson() throws IOException {
//		InputStream person = getClass().getResourceAsStream(personRDF);
//		model.read(person, defaultNameSpace);
//		person.close();
//	}

	private StringBuilder initialQuery() {
		StringBuilder queryString = new StringBuilder();
		// TODO Append more initial prefixes needed in query string
		queryString.append("PREFIX rdfs" + ": <" + "http://www.w3.org/2000/01/rdf-schema#" + "> ");
		queryString.append("PREFIX rdf" + ": <" + "http://www.w3.org/1999/02/22-" + "rdf-syntax-ns#" + "> ");
		queryString.append("PREFIX get: <http://www.semanticweb.org/hp-pc/ontologies/SER-531/Team-14/Location#> ");
		return queryString;
	}

	public LatLongPair query_get_latlong_from_city(String city_name, Model model) {

		LatLongPair latLong = new LatLongPair();
		StringBuilder queryString = initialQuery();
		queryString.append("SELECT DISTINCT ?o WHERE {\n" + "  ?s get:has_Name ?o\n" + "  FILTER(?o > \"Miami\")\n"+ "}\n" + "LIMIT 2");

		Query query = QueryFactory.create(queryString.toString());
		QueryExecution queryexec = QueryExecutionFactory.create(query, model);

		ResultSet response = queryexec.execSelect();
		System.out.println(response.toString());
		while (response.hasNext()) {
			QuerySolution solution = response.nextSolution();
			RDFNode latitude = solution.get("?o");
			// RDFNode longitude = solution.get("?longitude");

			if (latitude != null) {
				System.out.println(latitude.toString());
			}
//			if (longitude != null) {
//				System.out.println(longitude.toString());
//			}

		}

		// queryexec.close();

		return null;

	}

//	public static void main(String[] args) throws IOException {
//
//		System.out.println("Hello");
//		SparqlEngine test = new SparqlEngine();
//		test.query_get_latlong_from_city("Miami",test.city);
//
//		// System.out.println(System.getProperty("user.dir"));
//
//	}

}
