package exercises

import scala.annotation.tailrec


trait MySet[A] extends (A => Boolean){
  override def apply(v1: A): Boolean = contains(v1)
  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]
  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit
  def -(elem: A): MySet[A]
  def &(set: MySet[A]): MySet[A]
  def --(set: MySet[A]): MySet[A]
  def unary_! : MySet[A]
}

class EmptySet[A] extends MySet[A]{
  override def contains(elem: A): Boolean = false
  override def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)
  override def ++(anotherSet: MySet[A]): MySet[A] = anotherSet
  override def map[B](f: A => B): MySet[B] = new EmptySet[B]
  override def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  override def filter(predicate: A => Boolean): MySet[A] = this
  override def foreach(f: A => Unit): Unit = ()
  override def -(elem: A): MySet[A] = throw new NoSuchElementException
  override def &(set: MySet[A]): MySet[A] = this
  override def --(set: MySet[A]): MySet[A] = this
  override def unary_! : MySet[A] = new PropetyBasedSet[A](x => true)
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
  override def contains(elem: A): Boolean = if (head == elem) true else tail.contains(elem)
  override def +(elem: A): MySet[A] = if (contains(elem)) this else new NonEmptySet(elem, this)
  override def ++(anotherSet: MySet[A]): MySet[A] = new NonEmptySet(head, tail ++ anotherSet)
  override def map[B](f: A => B): MySet[B] = new NonEmptySet(f(head), tail.map(f))
  override def flatMap[B](f: A => MySet[B]): MySet[B] = f(head) ++ tail.flatMap(f)
  override def filter(predicate: A => Boolean): MySet[A] =
    if (predicate(head)) new NonEmptySet(head, new EmptySet) ++ tail.filter(predicate)
    else tail.filter(predicate)
  override def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }
  override def -(elem: A): MySet[A] = if (head == elem) tail else tail - elem + head
  override def &(set: MySet[A]): MySet[A] = {
//    if (set contains head) tail.&(set) + head
//    else tail & set
    filter(set)
  }
  override def --(set: MySet[A]): MySet[A] = {
//    if (set contains head) tail -- set
//    else tail -- set + head
    filter(!set)
  }
  override def unary_! : MySet[A] = new PropetyBasedSet[A](!this.contains(_))
//  override def unary_! : MySet[A] = new NonEmptySet(head, tail) {
//    override def apply(v1: A): Boolean = !contains(v1)
//  }

}

class PropetyBasedSet[A](property: A => Boolean) extends MySet[A]{
  override def contains(elem: A): Boolean = property(elem)
  override def +(elem: A): MySet[A] =
    new PropetyBasedSet[A](x => property(x) || x == elem)
  override def ++(anotherSet: MySet[A]): MySet[A] =
    new PropetyBasedSet[A](x => property(x) || anotherSet(x))
  override def map[B](f: A => B): MySet[B] = ???
  override def flatMap[B](f: A => MySet[B]): MySet[B] = ???
  override def foreach(f: A => Unit): Unit = ???
  override def filter(predicate: A => Boolean): MySet[A] = new PropetyBasedSet[A](x => property(x) && predicate(x))
  override def -(elem: A): MySet[A] = filter(_ != elem)
  override def --(set: MySet[A]): MySet[A] = filter(!set)
  override def &(set: MySet[A]): MySet[A] = filter(set)
  override def unary_! : MySet[A] = new PropetyBasedSet[A](!property(_))
}

object MySet {
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)

    buildSet(values.toSeq, new EmptySet[A])
  }
}

object Test extends App{
  val teste = MySet(1,2,3,4,5)
  val teste3 = MySet(3,4,5,6,7)
  val teste2 = new NonEmptySet("lia", new NonEmptySet("gostosa", new EmptySet))
//  teste.filter(_ < 4).foreach(println)
//  (teste - 3).foreach(println)
  (teste & teste3).foreach(println)
  println()
  (teste -- teste3).foreach(println)
//  for {
//    n1 <- teste
//    n2 <- teste2
//  } yield println(n1 + n2)
}