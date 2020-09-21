package todo

import todo.data._

import munit._

abstract class ModelSuite extends munit.FunSuite:
  def model: Model

  override def beforeEach(context: BeforeEach): Unit =
    model.clear()

  override def afterEach(context: AfterEach): Unit =
    model.clear()

  val task1 = Task(State.Active, "An active task", Some("The notes"), List(Tag("a"), Tag("b")))
  val task2 = Task(State.completedNow, "An inactive task", None, List(Tag("c")))

  test("Created tasks can be read"){
    val id1 = model.create(task1)
    assertEquals(model.read(id1), Some(task1))

    val id2 = model.create(task2)
    assertEquals(model.read(id2), Some(task2))
  }

  test("Updated tasks can be read"){
    val id = model.create(task1)
    val updated = model.update(id)(_ => task2)

    assertEquals(updated, Some(task2))
  }

  test("Deleted tasks are no longer read"){
    val id1 = model.create(task1)
    val id2 = model.create(task2)

    assert(model.delete(id1))

    assertEquals(model.read(id1), None)
    assertEquals(model.read(id2), Some(task2))
  }

  test("Tasks returns all inserted tasks in insertion order"){
    val id1 = model.create(task1)
    val t1 = model.tasks

    assertEquals(t1.toList, List((id1 -> task1)))

    val id2 = model.create(task2)
    val t2 = model.tasks

    assertEquals(t2.toList, List((id1 -> task1), (id2 -> task2)))
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
    
    assert(model.read(id1).get.state.active)
    assert(model.read(id2).get.state.completed)

    model.complete(id1)

    assert(model.complete(id1).get.state.completed)
    assert(model.complete(id2).get.state.completed)
  }

  test("tags returns all tags"){
    val id1 = model.create(task1)
    assertEquals(model.tags.toList, List(Tag("a"), Tag("b")))

    val id2 = model.create(task2)
    assertEquals(model.tags.toList, List(Tag("a"), Tag("b"), Tag("c")))
  }
