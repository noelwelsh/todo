package todo
package data

import io.circe._

final case class Tag(tag: String)
object Tag:
  // def apply(tag: String): Tag = tag

  // extension (tag: Tag):
  //   def toString: String = tag

  given tagCodec as Codec[Tag]:
    def apply(c: HCursor): Decoder.Result[Tag] =
      c.downField("tag").as[String].map(n => Tag(n))

    def apply(t: Tag): Json =
      Json.obj("tag" -> Json.fromString(t.tag))
