package todo

import cats.implicits._
import scala.collection.mutable
import todo.data._

/**
 * The InMemoryModel stores all the tasks in RAM, and hence they are lost when
 * the server restarts.
 */
object InMemoryModel extends Model:
  /* These are the tasks the application starts with. You can change these if you want. */
  val defaultTasks = List(
    Id(0) -> Task(State.completedNow, "Complete Effective Scala Week 2", None, List(Tag("programming"), Tag("scala"))),
    Id(1) -> Task(State.Active, "Complete Effective Scala Week 3", Some("Finish the todo list exercise"), List(Tag("programming"), Tag("scala"), Tag("encapsulation"), Tag("sbt"))),
    Id(2) -> Task(State.Active, "Make a sandwich", Some("Cheese and salad or ham and tomato?"), List(Tag("food"), Tag("lunch")))
  )

  /* Every Task is associated with an Id. Ids must be unique. */
  private var nextId: Id = Id(3)

  /* The idStore stores the associated between Ids and Tasks. We use a
   * LinkedHashMap so we can access elements in insertion order. We need to keep
   * a stable order so the UI doesn't jump around, which would be confusing to
   * the user. */
  private val idStore: mutable.LinkedHashMap[Id, Task] =
    mutable.LinkedHashMap.from(defaultTasks)

  /* Utility to get the next Id value we should use, and update nextId */
  private def getNextId: Id =
    val id = nextId
    nextId = id.next
    id

  def create(task: Task): Id =
    val id = getNextId
    id

  def read(id: Id): Option[Task] =
    idStore.get(id)

  def complete(id: Id): Option[Task] =
    None

  def update(id: Id)(f: Task => Task): Option[Task] =
    idStore.updateWith(id)(opt => opt.map(f))

  def delete(id: Id): Boolean =
    var found = false

    found

  def tasks: Tasks =
    Tasks(idStore)

  def tags: Tags =
    Tags(List.empty)

  def tasks(tag: Tag): Tasks =
    Tasks(idStore)

  def clear(): Unit =
    idStore.clear()
