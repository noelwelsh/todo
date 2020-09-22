package todo

import cats.implicits._
import java.io.File
import java.nio.file.Files
import java.nio.charset.StandardCharsets
import io.circe.{Decoder, Encoder}
import io.circe.parser._
import io.circe.syntax._
import scala.collection.mutable
import todo.data._

/**
 * The PersistentModel is a model that saves all data to file, meaning that
 * tasks persist between restarts.
 */
object PersistentModel extends Model:
  /*
   * Load JSON-encoded data from a file.
   *
   * Given a file name, load JSON data from that file, and decode it into the
   * type A. Throws an exception on failure.
   */
  def load[A](fileName: String)(using decoder: Decoder[A]): A = {
    val bytes = Files.readAllBytes(new File(fileName).toPath())
    val str = new String(bytes, StandardCharsets.UTF_8)

    // In a production system we would want to pay more attention to error
    // handling than we do here, but this is sufficient for the case study.
    decode[A](str) match {
      case Right(result) => result
      case Left(error) => throw error
    }
  }

  /**
   * Save data to a file in JSON format.
   *
   * Given a file name and some data, saves that data to the file in JSON
   * format. If the file already exists it is overwritten.
   */
  def save[A](fileName: String, data: A)(using encoder: Encoder[A]): Unit = {
    val path = new File(fileName).toPath()
    val json = data.asJson
    Files.write(path, json.spaces2.getBytes(StandardCharsets.UTF_8))
    ()
  }

  /* There are two pieces of state we need to implement the model:
   * - the tasks
   * - the next Id
   * (The InMemoryModel uses the same.)
   *
   * We keep the state in two files, named below, but provide a utility that
   * reads and writes them both as one. If the two get out of sync ... well,
   * stuff breaks.
   */
  object State:
    val tasksFileName = "tasks.json"
    val idFileName = "id.json"

    /*
     * If the tasks and id files do not exist, create them with initial values
     */
    def initialize(): Unit =
      val t = new File(tasksFileName)
      val i = new File(idFileName)

      if !t.exists() then save(tasksFileName, Tasks.empty)
      if !i.exists() then save(idFileName, Id(0))

    /**
     * Delete the tasks and id file if they exist.
     */
    def clear(): Unit =
      val t = new File(tasksFileName)
      val i = new File(idFileName)

      if t.exists() then t.delete()
      if i.exists() then i.delete()


    /*
     * Transform the current tasks and id and return an additional value that is
     * the result of the method call.
     */
    def withState[A](f: (Tasks, Id) => (Tasks, Id, A)): A =
      initialize()

      val tasks = load[Tasks](tasksFileName)
      val id = load[Id](idFileName)

      val (nextTasks, nextId, a) = f(tasks, id)

      save(tasksFileName, nextTasks)
      save(idFileName, nextId)

      a

    /*
     * Transform just the current tasks
     */
    def withTasks[A](f: Tasks => (Tasks, A)): A =
      withState{ (tasks, id) =>
        val (nextTasks, a) = f(tasks)
        (nextTasks, id, a)
      }


  def create(task: Task): Id =
    State.withState{(tasks, id) =>
      (Tasks(tasks.toList :+ (id -> task)), id.next, id)}

  def read(id: Id): Option[Task] =
    State.withTasks{ tasks =>
      (tasks, tasks.toMap.get(id))
    }

  def update(id: Id)(f: Task => Task): Option[Task] =
    State.withTasks{ tasks =>
      val map = tasks.toMap
      val updated = map.get(id).map(f)
      (updated.map(t => Tasks(map.updated(id, t))).getOrElse(tasks),
       updated)
    }

  def delete(id: Id): Boolean =
    State.withTasks{ tasks =>
      val map = tasks.toMap
      val deleted = map.contains(id)

      (Tasks(map - id), deleted)
    }

  def tasks: Tasks =
    State.withTasks{ tasks => (tasks, tasks) }

  def tasks(tag: Tag): Tasks =
    State.withTasks{ tasks =>
      val tagged = Tasks(tasks.toList.filter{ case (k, v) => v.tags.contains(tag) })
      (tasks, tagged)
    }

  def complete(id: Id): Option[Task] =
    State.withTasks{ tasks =>
      val map = tasks.toMap
      val updated = map.get(id).map(_.complete)

      updated.map{ t =>
        (Tasks(map.updated(id, t)), updated)
      }.getOrElse(tasks, updated)
    }


  def tags: Tags =
    State.withTasks{ tasks =>
      (tasks, Tags(tasks.toList.flatMap{ case (_, task) => task.tags }.distinct))
    }

  def clear(): Unit =
    State.clear()
