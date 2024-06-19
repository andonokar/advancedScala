package lectures.part1as

object AdvancedPatternMatching extends App {
 val numbers = List(1)
 val description = numbers match {
   case head :: Nil => println(s"$head")
   case _ =>
 }

  class Person(val name: String, val age: Int)
  object Person{
    def unapply(person: Person): Option[(String, Int)] = Some((person.name, person.age))
    def unapply(age: Int): Option[String] = Some(if (age < 21) "minor" else "major")
  }
  val bob = new Person("bob", 25)
  val greeting = bob match {
    case Person(n, a) => s"$n, $a"
  }

  val legalStatus = bob.age match {
    case Person(status) => s"$status"
  }

  case class Or[A, B](a: A, b: B)
  val either = Or(2, "two")
  val humanDescription = either match {
    case number Or string => s"$number, $string"
  }
  val vararg = numbers match {
    case List(1, _*) => "Starting with 1"
  }
  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting with 1, 2"
    case _ => "something else"
  }

  println(decomposed)

  // custom return types for unapply
  // isEmpty: Boolean, get: something.

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty = false
      def get: String = person.name
    }
  }

  println(bob match {
    case PersonWrapper(n) => s"This person's name is $n"
    case _ => "An alien"
  })


}
