package todo

import scala.collection.mutable

object Model:
  private var nextId: Id.Id = Id(0)
  private val tasks: mutable.HashMap[Id.Id, Task] =
    mutable.HashMap.empty

  private def getNextId: Id.Id =
    val id = nextId
    nextId = id.next
    id

  def create(description: String): Task =
    val id = getNextId
    val task = Task.Active(id, description)
    task

  def read(id: Id.Id): Option[Task] =
    None

  def update(id: Id.Id)(f: Task => Task): Option[Task] =
    tasks.updateWith(id)(opt => opt.map(f))

  def delete(id: Id.Id): Boolean =
    var found = false
    tasks.updateWith(id){
      case None => None
      case Some(_) =>
        found = true
        None
    }

    found

  def list: List[Task] =
    List.empty

  def clear: Unit =
    tasks.clear()
