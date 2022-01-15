import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.middleware.Timeout
import routes.GitHubRoutes

import scala.concurrent.duration.{Duration, DurationInt}

object ServerApp extends IOApp{

  val routes= Timeout(15.minutes)(GitHubRoutes.httpRoutes)

  val httpApp=routes.orNotFound
  //val routes=GitHubRoutes.httpRoutes
  val server=BlazeServerBuilder[IO]
    .bindHttp(8080,"localhost")
    .withHttpApp(httpApp)
    .resource
    
    

  override def run(args: List[String]): IO[ExitCode] = server.use(_=>IO.never)
                                                       .as(ExitCode.Success)
}
