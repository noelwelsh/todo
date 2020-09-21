package todo

import cats.implicits._
import java.io.File
import java.nio.file.Files
import java.nio.charset.StandardCharsets
import io.circe.{Decoder, Encoder}
import io.circe.parser._
import io.circe.syntax._
import scala.collection.mutable
import todo.data._

object PersistentModel extends Model:
  /*
   * Load JSON-encoded data from a file.
   *
   * Given a file name, load JSON data from that file, and decode it into the
   * type A. Throws an exception on failure.
   */
  def load[A](fileName: String)(using decoder: Decoder[A]): A = {
    val bytes = Files.readAllBytes(new File(fileName).toPath())
    val str = new String(bytes, StandardCharsets.UTF_8)

    // In a production system we would want to pay more attention to error
    // handling than we do here, but this is sufficient for the case study.
    decode[A](str) match {
      case Right(result) => result
      case Left(error) => throw error
    }
  }

  /**
   * Save data to a file in JSON format.
   *
   * Given a file name and some data, saves that data to the file in JSON
   * format. If the file already exists it is overwritten.
   */
  def save[A](fileName: String, data: A)(using encoder: Encoder[A]): Unit = {
    val path = new File(fileName).toPath()
    val json = data.asJson
    Files.write(path, json.spaces2.getBytes(StandardCharsets.UTF_8))
    ()
  }

  def create(task: Task): Id =
    ???

  def read(id: Id): Option[Task] =
    ???

  def update(id: Id)(f: Task => Task): Option[Task] =
    ???

  def delete(id: Id): Boolean =
    ???

  def tasks: Tasks =
    ???

  def tasks(tag: Tag): Tasks =
    ???

  def complete(id: Id): Option[Task] =
    ???

  def tags: Tags =
    ???

  def clear(): Unit =
    ???
