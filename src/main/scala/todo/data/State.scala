package todo
package data

import io.circe._
import java.time.ZonedDateTime

enum State:
  case Active
  case Completed(data: ZonedDateTime)

object State:
  def completedNow: State =
    Completed(ZonedDateTime.now())

  given stateCodec as Codec[State]:
    def apply(c: HCursor): Decoder.Result[State] =
      c.downField("state").as[String].flatMap {
        case "active" =>
          Right(Active)

        case "completed" =>
          c.downField("date")
            .as[String]
            .map(s => ZonedDateTime.parse(s))
            .map(d => Completed(d))

        case err =>
          Left(
            DecodingFailure(
              s"The task type ${err} is not one of the expected types of 'active' or 'completed'",
              List.empty
            )
          )
      }

    def apply(s: State): Json =
      s match
        case Active =>
          Json.obj("state" -> Json.fromString("active"))
        case Completed(date) =>
          Json.obj(
            "state" -> Json.fromString("completed"),
            "date" -> Json.fromString(date.toString)
          )
