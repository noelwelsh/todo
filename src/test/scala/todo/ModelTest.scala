package todo

import org.junit.Test
import org.junit.Assert._

class ModelTest:
  @Test def readReturnsCreatedTask(): Unit =
    Model.clear
    val description = "A new task"

    val created = Model.create(description)
    assertEquals(created.description, description)

    val read = Model.read(created.id)
    assertTrue(read.isDefined)
    assertTrue(read.map(task => task == created).getOrElse(false))


  @Test def listReturnsAllTasksInFIFOOrder(): Unit =
    val size = 512

    Model.clear
    for(i <- 0 to size) { Model.create(i.toString) }

    val tasks = Model.list
    assertEquals(tasks.size, size)
    assert(tasks.map(_.description.toInt) == tasks.map(_.description.toInt).sorted)
