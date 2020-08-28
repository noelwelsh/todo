package todo

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.server.staticcontent._

object AssetService:

  def service(blocker: Blocker)(using cs: ContextShift[IO]): HttpRoutes[IO] =
    fileService(FileService.Config("./assets", blocker))
