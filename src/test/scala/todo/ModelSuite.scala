package todo

import todo.data._

import munit._

abstract class ModelSuite extends munit.FunSuite:
  val task1 = Task(State.Active, "An active task", Some("The notes"), List(Tag("a"), Tag("b")))
  val task2 = Task(State.completedNow, "An inactive task", None, List(Tag("c")))

  def model: Model

  override def beforeEach(context: BeforeEach): Unit =
    model.clear()

  override def afterEach(context: AfterEach): Unit =
    model.clear()

  // Custom assertions. These make our code more readable and give more useful messages on errors

  def assertTask(
    actual: Option[Task],
    expected: Task
  )(implicit loc: munit.Location): Unit =
    actual match
      case None =>
        fail(s"We expected the task $expected but we received None instead.")
      case Some(task) =>
        assertEquals(task, expected, s"We expected the task $expected but we received $task instead.")

  def assertTaskActive(
    task: Option[Task]
  )(implicit loc: munit.Location): Unit =
    task match
      case None =>
        fail(s"We expected a task but we received None instead.")
      case Some(t) =>
        assert(t.state.active, s"Expected the task's state to be active but it was ${t.state}")

  def assertTaskCompleted(
    task: Option[Task]
  )(implicit loc: munit.Location): Unit =
    task match
      case None =>
        fail(s"We expected a task but we received None instead.")
      case Some(t) =>
        assert(t.state.completed, s"Expected the task's state to be completed but it was ${t.state}")


  test("Created tasks can be read"){
    val id1 = model.create(task1)
    assertTask(model.read(id1), task1)

    val id2 = model.create(task2)
    assertTask(model.read(id2), task2)
  }

  test("Updated tasks can be read"){
    val id = model.create(task1)
    val updated = model.update(id)(_ => task2)

    assertTask(updated, task2)
  }

  test("Deleted tasks are no longer read"){
    val id1 = model.create(task1)
    val id2 = model.create(task2)

    assert(model.delete(id1))

    assertEquals(model.read(id1),
                 None,
                 "The deleted task was still returned when read")
    assertEquals(model.read(id2),
                 Some(task2),
                 "The task that was not deleted was not returned when read")
  }

  test("Tasks returns all inserted tasks in insertion order"){
    val id1 = model.create(task1)
    val t1 = model.tasks

    assertEquals(t1.toList,
                 List((id1 -> task1)),
                 "The list of tasks is different to the tasks that were created")

    val id2 = model.create(task2)
    val t2 = model.tasks

    assertEquals(t2.toList,
                 List((id1 -> task1), (id2 -> task2)),
                 "The list of tasks is different to the tasks that were created, or it is not in order of creation")
  }

  test("Tasks(tag) returns only tasks with given tag"){
    val id1 = model.create(task1)
    val id2 = model.create(task2)

    assertEquals(model.tasks(Tag("a")).toList, List(id1 -> task1))
    assertEquals(model.tasks(Tag("b")).toList, List(id1 -> task1))
    assertEquals(model.tasks(Tag("c")).toList, List(id2 -> task2))
  }

  test("complete changes state to completed, if task is not already completed"){
    val id1 = model.create(task1)
    val id2 = model.create(task2)
    
    assertTaskActive(model.read(id1))
    assertTaskCompleted(model.read(id2))

    model.complete(id1)

    assertTaskCompleted(model.complete(id1))
    assertTaskCompleted(model.complete(id2))
  }

  test("tags returns all tags"){
    val id1 = model.create(task1)
    assertEquals(model.tags.toList, List(Tag("a"), Tag("b")))

    val id2 = model.create(task2)
    assertEquals(model.tags.toList, List(Tag("a"), Tag("b"), Tag("c")))
  }
