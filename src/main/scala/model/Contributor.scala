package model

import org.http4s.EntityEncoder
import io.circe.Codec
import cats.effect.IO
import io.circe.generic.semiauto.deriveCodec
import org.http4s.circe.jsonEncoderOf
final case class Contributor( name:String,contributions:Int)

object  Contributor{
     implicit  val encoder:Codec[Contributor]= deriveCodec[Contributor]
    implicit val entityEncoder: EntityEncoder[IO,List[Contributor]]=jsonEncoderOf[IO, List[Contributor]]
}