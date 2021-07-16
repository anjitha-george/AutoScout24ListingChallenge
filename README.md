# Car Listing Project
## Tech Stack
1.	Language : Scala
2.	Framework: Play framework

## How to run the code
1.	Extract the zip file to a local location.
2.	Project requires, JDK 1.8 or higher.
3.	Project requires, sbt version 1.5.1.
4.	Open command line at the root location.
5.	Run command "sbt run" in cmd for running the application.
6.	Open "http://localhost:9000 " in browser.
7.	The car listing report will be displayed.

## How to run the Tests
1.	Run command "sbt test" in cmd for running the tests.
Report Web Application
1.	Open "http://localhost:9000" in browser.
2.	The car listing report with all the four aggregates is displayed. 
File Upload Functionality
1.	Click on “Go to File Upload” link or open "http://localhost:9000/upload" in browser
2.	Upload the files and click on submit.  Files are validated and uploaded. 
3.	To view the listing report based on the uploaded files click on “Go to Report”.
4.	The updated reports are displayed.

## API Endpoints
1.	The API endpoints for the report are as follows:

	a) http://localhost:9000/averagePricePerSellerType – displays the Average Listing Selling Price per Seller Type

	b) http://localhost:9000/listingPercentPerMake – displays the Distribution (in percent) of available cars by Make

	c) http://localhost:9000/averagePriceMostContacted/{percent} – displays the Average price of the {percent}% most contacted listings

	d) http://localhost:9000/mostContactedListingPerMonth/{ lisitngsCountPerMonth } – displays {lisitngsCountPerMonth}  most contacted listings for every month
