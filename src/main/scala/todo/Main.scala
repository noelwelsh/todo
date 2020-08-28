package todo

import cats.effect._
import cats.implicits._
import fs2.Stream
import org.http4s._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.{Router, Server}
import org.http4s.syntax.kleisli._
import org.http4s.server.middleware.CORS

object Main extends IOApp:
  private def app(blocker: Blocker): HttpApp[IO] =
    Router.define(
      "/api" -> CORS(TodoService(DefaultActions()).service)
    )(AssetService.service(blocker)).orNotFound

  private def server(blocker: Blocker): Resource[IO, Server] =
    EmberServerBuilder
      .default[IO]
      .withHost("0.0.0.0")
      .withPort(3000)
      .withHttpApp(app(blocker))
      .build

  private val program: Stream[IO, Unit] =
    for {
      blocker <- Stream.resource(Blocker[IO])
      server <- Stream.resource(server(blocker))
      _ <- Stream.eval(IO(println(s"Started server on: ${server.address}")))
      _ <- Stream.never[IO].covaryOutput[Unit]
    } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    program.compile.drain.as(ExitCode.Success)
