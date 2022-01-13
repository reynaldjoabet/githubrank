# githubrank
 ## About Project
 Exposes an endpoint that returns a list of contributors sorted by the number of contributions. It makes use of GitHub REST API v3 (`https://developer.github.com/v3/`).It also handles GitHub’s API rate limit restriction using a token that can be set as an environment variable of name GH_TOKEN The endpoint is implemented with pagination in mind

___
### How to run project

- An authentication token from github should be created before the following steps. 
- Once the authentication token is created, it can be manually set in the appplication in a private variable or passed as an environment variable with the name GH_TOKEN. 
- By default this application uses the manual option
- Using the terminal, enter the SBT shell by typing sbt 
- Then type run on the SBT shell and the server will start on port 8080
- Once started, use Postman or a browswer and go to a sample endpoint
`http://localhost:8080/org/nameOfOrganisation/contributors` , replacing `nameOfOrganisation` with an appropriate organisation name

- Using `https://api.github.com/organizations` will return some organisations, whose names that can be used to test the application

___
## Sample Endpoints
 - `http://localhost:8080/org/moneyspyder/contributors` 
 - `http://localhost:8080/org/ministrycentered/contributors`
 - `http://localhost:8080/org/gumgum/contributors`
 - `http://localhost:8080/org/wesabe/contributors`
 - `http://localhost:8080/org/klout/contributors`

___
 ### Stack
 - http4s-blaze-server
 - http4s-circe
 - circe-generic
 - http4s-blaze-client
 - http4s-dsl