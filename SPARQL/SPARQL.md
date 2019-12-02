1. Get all the Jobs based on filters appplied by applicants


PREFIX getJob: <http://www.semanticweb.org/SER-531/Team-14/Jobs#>
PREFIX getApp: <http://www.semanticweb.org/SER-531/Team-14/Applicants#>
select distinct ?title ?loc ?postdate ?appdeadline ?department ?specialzationRequirement ?graduateLevelReq ?lat ?long ?salary
WHERE{
	SERVICE <http://ec2-54-172-125-252.compute-1.amazonaws.com:3030/Project/>
  	{
      SELECT ?title ?loc ?postdate ?appdeadline ?department ?specialzationRequirement ?graduateLevelReq ?salary
      WHERE {
        ?s  getJob:has_title ?title ;
            getJob:located_in ?loc ;
            getJob:posting_date ?postdate ;
            getJob:application_Deadline ?appdeadline ;
            getJob:belongs_to ?department ;
            getJob:specialzationRequirement ?specialzationRequirement ;
            getJob:graduateLevelRequirement ?graduateLevelReq ;
            getJob:has_Salary ?salary .
      		FILTER (?loc = "Phoenix")
		}
	}
?s1 getLoc:has_Name ?loc ;
    getLoc:has_Latitude ?lat ;
    getLoc:has_Longitude ?long
}

===================================================================================================================================================
2. Visualize Number of Applicants in a location

PREFIX getLoc: <http://www.semanticweb.org/SER-531/Team-14/Location#>
PREFIX getApp: <http://www.semanticweb.org/SER-531/Team-14/Applicants#>

Select ?count ?Lat ?Long 
Where {
  SERVICE <http://34.94.128.250:3030/Project/>
  {
    select (count(?App) as ?count) ?loc
    where {
      ?App getApp:lives_in ?loc.
      FILTER (?loc = "Dallas")
    }
    GROUP BY ?loc
  }
  ?location getLoc:has_Name ?loc;
			getLoc:has_Longitude ?Long;
       	   	getLoc:has_Latitude ?Lat.
}

===================================================================================================================================================
3. Visualize Number of Applicants in a location


PREFIX getLoc: <http://www.semanticweb.org/SER-531/Team-14/Location#>
PREFIX getJob: <http://www.semanticweb.org/SER-531/Team-14/Jobs#>

Select ?count ?Lat ?Long 
Where {
  SERVICE <http://ec2-54-172-125-252.compute-1.amazonaws.com:3030/Project/>
  {
    select (count(?job) as ?count) ?loc
    where {
      ?job getJob:located_in ?loc.
      FILTER (?loc = "Dallas")
    }
    GROUP BY ?loc
  }
  ?location getLoc:has_Name ?loc;
			getLoc:has_Longitude ?Long;
       	   	getLoc:has_Latitude ?Lat.
}

===================================================================================================================================================
4. Get all the Applicants based on filters appplied by Recruiters


PREFIX getLoc: <http://www.semanticweb.org/SER-531/Team-14/Location#>
PREFIX getJob: <http://www.semanticweb.org/SER-531/Team-14/Jobs#>

select distinct ?name ?email ?gender ?expectedGradDate ?expectedSalary ?skills ?major ?university ?schoolLevel ?specialization ?loc ?lat ?long
WHERE{
	SERVICE <http://34.94.128.250:3030/Project/>
	{
		select distinct ?name ?email ?gender ?expectedGradDate ?expectedSalary ?skills ?major ?university ?schoolLevel ?specialization ?loc
		where {
			?person getApp:has_Name  ?name ;
				   getApp:lives_in  ?loc ;
				   getApp:email  ?email ;
				   getApp:gender ?gender ;
				   getApp:expectedGraduationDate  ?expectedGradDate ;
				   getApp:expected_Salary  ?expectedSalary ;
				   getApp:has_skills  ?skills ;
				   getApp:major  ?major ;
				   getApp:school  ?university ;
				   getApp:schoolLevel ?schoolLevel ;
				   getApp:specialization ?specialization ;
				   getApp:schoolLevel "Bachelor" .
			}
	}
?location  getLoc:has_Name  ?loc ;
      	   getLoc:has_Latitude ?lat ;
           getLoc:has_Longitude ?long
}