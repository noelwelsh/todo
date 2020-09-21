package todo
package data

import io.circe._
import io.circe.syntax._


opaque type Tasks = Iterable[(Id, Task)]
object Tasks:
  def apply(tasks: Iterable[(Id, Task)]): Tasks = tasks

  val empty: Tasks = Tasks(List.empty)

  extension (t: Tasks):
    def toList: List[(Id, Task)] =
      t.toList

  val elementDecoder = new Decoder[(Id, Task)]:
    def apply(c: HCursor): Decoder.Result[(Id, Task)] =
      for {
        id <- c.downField("id").as[Int]
        task <- c.downField("task").as[Task]
      } yield (Id(id) -> task)

  given tasksCodec as Codec[Tasks]:
    def apply(c: HCursor): Decoder.Result[Tasks] =
      c.as(Decoder.decodeList(elementDecoder)).map(t => Tasks(t))

    def apply(t: Tasks): Json =
      Json.arr(
        t.toArray.map {
          case (id, task) =>
            Json.obj("id" -> Json.fromInt(id.toInt), "task" -> task.asJson)
        }: _*
      )
