package routes

import cats.data.NonEmptyList
import org.http4s.dsl.Http4sDsl
import cats.effect.IO
import org.http4s.{HttpRoutes, MediaType}
import client.GitHubClient
import org.http4s.headers.{`Cache-Control`, `Content-Type`}
import model.Contributor.entityEncoder
import org.http4s.CacheDirective.{`max-age`, `no-cache`,`must-revalidate`}

import scala.concurrent.duration.DurationInt
object GitHubRoutes  extends Http4sDsl[IO]{
    val httpRoutes: HttpRoutes[IO] = HttpRoutes.of[IO]{
        case GET-> Root /"org"/org_name/"contributors" => GitHubClient.getContributors(org_name)
          .flatMap( Ok(_, `Content-Type`(MediaType.application.json),
              `Cache-Control`(NonEmptyList(`no-cache`(), List(`max-age`(2.minutes),`must-revalidate`))))
          )

    }
  
}
