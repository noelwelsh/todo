package todo
package data

import io.circe._
import io.circe.syntax._
import java.time.ZonedDateTime

final case class Task(
  state: State,
  description: String,
  notes: Option[String],
  tags: List[Tag]
):
  def complete: Task =
    val newState =
      state match
        case State.Active => State.completedNow
        case State.Completed(d) => State.Completed(d)
    this.copy(state = newState)

object Task:
  given taskCodec as Codec[Task]:
    def apply(c: HCursor): Decoder.Result[Task] =
      for {
        state <- c.downField("state").as[State]
        description <- c.downField("description").as[String]
        notes <- c.downField("notes").as[Option[String]]
        tags <- c.downField("tags").as[List[Tag]]
      } yield Task(state, description, notes, tags)

    def apply(t: Task): Json =
      Json.obj(
        "state" -> t.state.asJson,
        "description" -> Json.fromString(t.description),
        "notes" -> t.notes.asJson,
        "tags" -> t.tags.asJson
      )
