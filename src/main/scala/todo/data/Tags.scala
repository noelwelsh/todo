package todo
package data

import io.circe._
import io.circe.syntax._


opaque type Tags = List[Tag]
object Tags:
  def apply(tags: List[Tag]): Tags = tags

  extension (t: Tags):
    def toList: List[Tag] = t

  given tagsCodec as Codec[Tags]:
    def apply(c: HCursor): Decoder.Result[Tags] =
      c.as(Decoder.decodeList(Tag.tagCodec)).map(t => Tags(t))

    def apply(t: Tags): Json =
      Json.arr(
        t.toArray.map(tag => tag.asJson): _*
      )
