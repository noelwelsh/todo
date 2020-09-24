package todo

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.server.staticcontent._

/**
 * This service is responsible for serving static files. We serve anything found
 * in the assets subdirectory. This includes the user interface.
 */
object AssetService:
  def service(blocker: Blocker)(using cs: ContextShift[IO]): HttpRoutes[IO] =
    fileService(FileService.Config("./assets", blocker))
