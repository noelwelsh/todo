package todo

import cats.implicits._
import scala.collection.mutable
import todo.data._

object InMemoryModel extends Model:
  val defaultTasks = List(
    Id(0) -> Task(State.completedNow, "Complete Effective Scala Week 2", None, List(Tag("programming"), Tag("scala"))),
    Id(1) -> Task(State.Active, "Complete Effective Scala Week 3", Some("Finish the todo list exercise"), List(Tag("programming"), Tag("scala"), Tag("encapsulation"), Tag("sbt"))),
    Id(2) -> Task(State.Active, "Make a sandwich", Some("Cheese and salad or ham and tomato?"), List(Tag("food"), Tag("lunch")))
  )

  private var nextId: Id = Id(3)
  private val idStore: mutable.LinkedHashMap[Id, Task] =
    mutable.LinkedHashMap.from(defaultTasks)

  private def getNextId: Id =
    val id = nextId
    nextId = id.next
    id

  def create(task: Task): Id =
    val id = getNextId
    idStore += (id -> task)
    id

  def read(id: Id): Option[Task] =
    idStore.get(id)

  def complete(id: Id): Option[Task] =
    update(id)(t => t.complete)

  def update(id: Id)(f: Task => Task): Option[Task] =
    idStore.updateWith(id)(opt => opt.map(f))

  def delete(id: Id): Boolean =
    var found = false
    idStore.updateWith(id){
      case None => None
      case Some(_) =>
        found = true
        None
    }

    found

  def tasks: Tasks =
    Tasks(idStore)

  def tags: Tags =
    Tags(idStore.values.flatMap(task => task.tags).toList.distinct)

  def tasks(tag: Tag): Tasks =
    Tasks(idStore.filter{ case (k, v) => v.tags.contains(tag) })

  def clear: Unit =
    idStore.clear()
