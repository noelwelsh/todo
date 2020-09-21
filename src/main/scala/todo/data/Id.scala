package todo
package data

import io.circe._

final case class Id(toInt: Int):
  def next: Id = this.copy(toInt = toInt + 1)
object Id:
  given idOrdering as Ordering[Id]:
    def compare(x: Id, y: Id): Int =
      if x < y
        -1
      else if x == y
        0
      else
        1

  given idEncoder as Codec[Id]:
    def apply(c: HCursor): Decoder.Result[Id] =
      c.downField("id").as[Int].map(id => Id(id))

    def apply(id: Id): Json =
      Json.obj("id" -> Json.fromInt(id.toInt))
