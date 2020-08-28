package todo

import java.time.ZonedDateTime

class DefaultActions() extends Actions:
  def create(description: String): Task =
    Model.create(description)

  def read(id: Id.Id): Option[Task] =
     None

  def update(id: Id.Id, description: String, completed: Boolean): Option[Task] =
    None

  def delete(id: Id.Id): Boolean =
    true

  def list: List[Task] =
    List(Task.Active(Id(0), "Learn Scala"),
         Task.Active(Id(1), "..."),
         Task.Active(Id(2), "Profit!"))

  def complete(id: Id.Id): Option[Task] =
    None
