import io.circe._
import io.circe.syntax._

opaque type Foo = String
object Foo:
  def apply(s: String): Foo = s

  given fooCodec as Codec[Foo]:
    def apply(c: HCursor): Decoder.Result[Foo] =
      c.downField("foo").as[String].map(f => Foo(f))

    def apply(foo: Foo): Json =
      Json.obj("foo" -> Json.fromString(foo))

  def test: Unit =
    val j = Foo("foo").asJson
    val f = j.as[Foo]

    f match {
      case Left(e) => throw e
      case Right(f2) => assert(Foo("foo") == f2)
    }
