package todo

import io.circe.{Encoder, Json}
import java.time.ZonedDateTime

enum Task:
  def id: Id.Id
  def complete: Task =
    this match
      case a: Active => Completed(a.id, a.description, ZonedDateTime.now())
      case c: Completed => c

  case Active(id: Id.Id, description: String)
  case Completed(id: Id.Id, description: String, completedAt: ZonedDateTime)

object Task:
  given taskEncoder as Encoder[Task]:
    def apply(t: Task): Json =
      t match {
        case Active(id, description) =>
          Json.obj(
            "type" -> Json.fromString("active"),
            "id" -> Json.fromInt(id.toInt),
            "description" -> Json.fromString(description)
          )

        case Completed(id, description, completedAt) =>
          Json.obj(
            "type" -> Json.fromString("completed"),
            "id" -> Json.fromInt(id.toInt),
            "description" -> Json.fromString(description),
            "completedAt" -> Json.fromString(completedAt.toString)
          )
      }
