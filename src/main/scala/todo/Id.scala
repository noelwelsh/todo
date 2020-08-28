package todo

object Id:
  opaque type Id = Int
  def apply(id: Int): Id = id

  extension (id: Id):
    def toInt: Int = id
    def next: Id = id + 1

  given idOrdering as Ordering[Id]:
    def compare(x: Id, y: Id): Int =
      if x < y
        -1
      else if x == y
        0
      else
        1
