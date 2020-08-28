package todo

trait Actions:
  /*
   * Create an active task from a description
   */
  def create(description: String): Task
  /*
   * Get the task associated with the given ID if one exists.
   */
  def read(id: Id.Id): Option[Task]
  /*
   * Get the task associated with the given ID if one exists.
   */
  def update(id: Id.Id, description: String, completed: Boolean): Option[Task]
  /*
   * Delete the task associated with the given ID if one exists. Returns true if
   * the task was deleted, otherwise false to indicate no task was associated
   * with the ID.
   */
  def delete(id: Id.Id): Boolean
  /*
   * Get a list of all tasks, sorted by ID.
   */
  def list: List[Task]
  /*
   * Complete the task with the given ID and return the updated task if one exists.
   */
  def complete(id: Id.Id): Option[Task]
