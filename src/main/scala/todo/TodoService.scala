package todo

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.circe._
import io.circe.Encoder
import io.circe.syntax._

class TodoService(actions: Actions):
  import org.http4s.dsl.io._
  object Description extends QueryParamDecoderMatcher[String]("description")

  val service: HttpRoutes[IO] =
    HttpRoutes.of[IO]{
      case GET -> Root / "task" / IntVar(id) =>
        actions.read(Id(id)) match
          case None => NotFound()
          case Some(task) => Ok(task.asJson)

      case POST -> Root / "task" :? Description(description) =>
        val task = actions.create(description)
        println(task)
        Ok(task.asJson)

      case DELETE -> Root / "task" / IntVar(id) =>
        if actions.delete(Id(id))
          Ok()
        else
          NotFound()

      case POST -> Root / "task" / IntVar(id) / "complete" =>
        actions.complete(Id(id)) match
          case None => NotFound()
          case Some(task) => Ok(task.asJson)

      case GET -> Root / "task" =>
        val tasks = actions.list
        Ok(tasks.asJson)
    }
