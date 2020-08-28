package todo

import java.time.ZonedDateTime

class DefaultActions() extends Actions:
  def create(description: String): Task =
    Model.create(description)

  def read(id: Id.Id): Option[Task] =
     Model.read(id)

  def update(id: Id.Id, description: String, completed: Boolean): Option[Task] =
    Model.update(id){ task =>
      task match
        case a: Task.Active =>
          if completed then Task.Completed(a.id, description, ZonedDateTime.now())
          else Task.Active(a.id, description)

        case c: Task.Completed =>
          if completed then Task.Completed(c.id, description, c.completedAt)
          else Task.Active(c.id, description)
    }

  def delete(id: Id.Id): Boolean =
    Model.delete(id)

  def list: List[Task] =
    Model.list

  def complete(id: Id.Id): Option[Task] =
    Model.update(id)(task => task.complete)
