package client
import org.http4s.client._
import org.http4s.client.Client
import fs2.{Chunk, Stream}
import cats.effect.IO
import io.circe.Json
import org.http4s.circe.jsonOf
import org.http4s.headers.{Accept, Authorization}
import org.http4s.{AuthScheme, Credentials, MediaType, Method, Uri}
import org.http4s.client.dsl.Http4sClientDsl
import org.slf4j.LoggerFactory
import model._
import org.http4s.client.blaze._

import scala.concurrent.duration.DurationInt
import cats.effect.IOApp
import org.http4s.Header
import org.typelevel.ci.CIString




object GitHubClient extends Http4sClientDsl[IO]{
    private val logger=LoggerFactory.getLogger(this.getClass)
   private  val token=""
  
   private val client= JavaNetClientBuilder[IO]
                                   .withReadTimeout(8.minutes)
                                   .create

/**
  * 
  *
  * @param client
  * @param accessToken
  * @param org_name
  * @return
  */
    
    private def fetchRepositoriesJson(client: Client[IO], accessToken: String,org_name:String): Stream[IO,Json] = {
    val baseUrl="https://api.github.com/orgs"
    Stream.unfoldLoopEval(1) { page =>
      logger.info(s"Fetching repository page $page")
      val request = Method.GET(
        Uri.unsafeFromString(s"${baseUrl}/${org_name}/repos?page=${page}&per_page=200"),
          //Accept(MediaType.application.json),
          Header.Raw(CIString("Accept"), "application/vnd.github.v3+json"),
          Authorization(Credentials.Token(AuthScheme.Bearer,accessToken))
      )
      client.expect[Vector[Json]](request)(jsonOf[IO,Vector[Json]]).map { repositories  =>
        val nextPage = if (repositories.nonEmpty) Some(page + 1) else None
        (repositories , nextPage)
      }
    }.flatMap(repositories => Stream.chunk(Chunk.vector(repositories )))
  }

  /**
    * 
    *
    * @param client
    * @param accessToken
    * @param repo_url
    * @return  A stream of Json
    */
  private def fetchContributorsJson(client: Client[IO], accessToken: String,repo_url:String): Stream[IO,Json] = {
      //https://api.github.com/repos/animikii/payola/contributors
    Stream.unfoldLoopEval(1) { page =>
      logger.info(s"Fetching contributor page $page")
      val request = Method.GET(
        Uri.unsafeFromString(s"$repo_url?page=${page}&per_page=200"),
          //Accept(MediaType.application.json),
          Header.Raw(CIString("Accept"), "application/vnd.github.v3+json"),
          Authorization(Credentials.Token(AuthScheme.Bearer,accessToken))
      )
      client.expect[Vector[Json]](request)(jsonOf[IO,Vector[Json]]).map { contributors  =>
        val nextPage = if (contributors.nonEmpty) Some(page + 1) else None
        (contributors , nextPage)
      }
    }.flatMap(contributors => Stream.chunk(Chunk.vector(contributors )))
  }

  /**
    * 
    *
    * @param org_name
    * @return a list of contributors
    */
def getContributors(org_name:String): IO[List[Contributor]] = for{
          repositoryJson<-fetchRepositoriesJson(client,token,org_name)
                                    .compile.toList
          contributorsUrl=repositoryJson.map(_.\\("contributors_url").head.toString().replaceAll("\"",""))// extract the  contributors_url field, remove the double quotes around the string
          v=println(contributorsUrl)
          contributorsJson<- IO.parTraverseN(contributorsUrl.length +1)(contributorsUrl)(fetchContributorsJson(client,token,_).compile.toList ) // for each url in the list, we past that to the function and get a list of contributors for that repo
          contributors=contributorsJson.flatten.map(j=>Contributor(j.\\("login").head.toString().replaceAll("\"",""),j.\\("contributions").head.toString().toInt))// flatten and extract two relevant fields
                         .groupBy(_.name).map(grouped=>Contributor(grouped._1,grouped._2.map(_.contributions).sum))// group the list by contributor name and then sum the total contributions
  c=println(contributors)
            } yield contributors.toList.sortBy(_.contributions) // sort in ascending order of number of contributors



//override def run(args: List[String]): IO[ExitCode] = getContributors("engineyard").as(ExitCode.Success)

  }