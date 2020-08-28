package todo

trait Actions:
  def create(description: String): Task
  def read(id: Id.Id): Option[Task]
  def update(id: Id.Id, description: String, completed: Boolean): Option[Task]
  def delete(id: Id.Id): Boolean
  def list: List[Task]
  // Not strictly needed, but a useful utility
  def complete(id: Id.Id): Option[Task]
