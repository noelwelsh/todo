package todo
package data

import munit._
import io.circe._
import io.circe.syntax._

class BijectionSuite extends munit.FunSuite:
  def testBijection[A](
    name: String,
    original: A
  )(using loc: munit.Location, codec: Codec[A]): Unit = {
    test(name) {
      val decoded = original.asJson.as[A]
      decoded match {
        case Right(o) => assertEquals(original, o)
        case Left(e) => fail(s"""Bijection failed for $name
                                |Value: $original
                                |JSON:  ${original.asJson.spaces2}
                                |Error: ${e.toString}""".stripMargin)
      }
    }
  }

  val task1 = Task(State.Active, "Description", Some("Notes"), List(Tag("a"), Tag("b")))
  val task2 = Task(State.completedNow, "Get this done", Some("Like dinner"), List(Tag("x"), Tag("y")))

  val tasks = Tasks(List(Id(1) -> task1, Id(2) -> task2))

  testBijection("Task", task1)
  testBijection("Task", task2)
  testBijection("Tag", Tag("dummy"))
  testBijection("State:Active", State.Active)
  testBijection("State:Completed", State.Completed(java.time.ZonedDateTime.now()))
  testBijection("Id", Id(0))
  testBijection("Tasks", tasks)
