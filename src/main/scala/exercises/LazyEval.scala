package exercises

import scala.annotation.tailrec

object LazyEval extends App {
 abstract class MyStream[+A] {
   def isEmpty: Boolean
   def head: A
   def tail: MyStream[A]
   def #::[B >: A](element: B): MyStream[B]
   def ++[B >: A](stream: => MyStream[B]): MyStream[B]
   def foreach(f: A => Unit): Unit
   def map[B](f: A => B): MyStream[B]
   def flatMap[B](f: A => MyStream[B]): MyStream[B]
   def filter(predicate: A => Boolean): MyStream[A]
   def take(n: Int): MyStream[A]
   def takeAsList(n: Int): List[A] = take(n).toList()
   @tailrec
   final def toList[B >: A](acc: List[B] = Nil): List[B] =
     if (isEmpty) acc
     else tail.toList(acc :+ head)
 }

  object EmptyStream extends MyStream[Nothing] {
    override def isEmpty: Boolean = true
    override def head: Nothing = throw new NoSuchElementException
    override def tail: MyStream[Nothing] = throw new NoSuchElementException
    override def #::[B >: Nothing](element: B): MyStream[B] = new Cons[B](element, this)
    override def ++[B >: Nothing](stream: => MyStream[B]): MyStream[B] = stream
    override def foreach(f: Nothing => Unit): Unit = ()
    override def map[B](f: Nothing => B): MyStream[B] = this
    override def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this
    override def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this
    override def take(n: Int): MyStream[Nothing] = this
  }

  class Cons[+A](h: A, t: => MyStream[A]) extends MyStream[A]{
    override def isEmpty: Boolean = false
    override val head: A = h
    override lazy val tail: MyStream[A] = t
    override def #::[B >: A](element: B): MyStream[B] = new Cons[B](element, this)
    override def ++[B >: A](stream: => MyStream[B]): MyStream[B] = new Cons(head, tail ++ stream)
    override def foreach(f: A => Unit): Unit = {
      f(head)
      tail.foreach(f)
    }
    override def map[B](f: A => B): MyStream[B] = new Cons(f(head), tail.map(f))
    override def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)
    override def filter(predicate: A => Boolean): MyStream[A] =
      if (predicate(head)) new Cons(head, tail.filter(predicate))
      else tail.filter(predicate)
    override def take(n: Int): MyStream[A] = {
      if (n > 0) new Cons[A](head, tail.take(n - 1))
      else if (n == 1) new Cons[A](head, EmptyStream)
      else EmptyStream
    }

  }

  object MyStream {
    def from[A](start: A)(generator: A => A): MyStream[A] =
      new Cons(start, from(generator(start))(generator))
  }

  val naturals = MyStream.from(1)(_ + 1)
  println(naturals.head)
  println(naturals.tail.head)
  println(naturals.tail.tail.head)

  val startFrom0 = 0 #:: naturals // naturals.#::(0)
  println(startFrom0.head)

//  startFrom0.take(10000).foreach(println)

  // map, flatMap
  println(startFrom0.map(_ * 2).take(100).toList())
  println(startFrom0.flatMap(x => new Cons(x, new Cons(x + 1, EmptyStream))).take(10).toList())
  println(startFrom0.take(20).filter(_ < 10).toList())
  println


  @tailrec
  def fibonacci(n: BigInt, p: BigInt = 1, last: BigInt = 1): BigInt =
    if (n <= 2) p else fibonacci(n-1, p + last, p)

  def fibonaccistream(first: BigInt = 1, second: BigInt = 1): MyStream[BigInt] =
    new Cons(first, fibonaccistream(second, first + second))

  val fibstream = MyStream.from(BigInt(1))(x => x + 1).map(fibonacci(_))
  println(fibstream.take(10).toList())
  println(fibonaccistream().take(10).toList())

  def eratosthenes(numbers: MyStream[Int]): MyStream[Int] =
    if (numbers.isEmpty) numbers
    else new Cons(numbers.head, eratosthenes(numbers.tail.filter(_ % numbers.head != 0)))
    
  println(eratosthenes(MyStream.from(2)(_ + 1)).take(100).toList())
}
