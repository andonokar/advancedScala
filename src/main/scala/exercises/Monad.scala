package exercises

object Monad extends App {
  class LazyProcessor[+A](value: => A) {
    lazy val m = value
    def use: A = value
    def flatMap[B](f: (=> A) => LazyProcessor[B]): LazyProcessor[B] = f(m)
  }
  object LazyProcessor {
    def apply[A](value: => A): LazyProcessor[A] = new LazyProcessor(value)
  }

  val instance = LazyProcessor {
    println("lia")
    42
  }
  val flat = instance.flatMap(x => LazyProcessor(x * 10))
  // 2: map and flatten in terms  of flatMap
  /*
    Monad[T] { // List
      def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)

      def map[B](f: T => B): Monad[B] = flatMap(x => unit(f(x))) // Monad[B]
      def flatten(m: Monad[Monad[T]]): Monad[T] = m.flatMap((x: Monad[T]) => x)

      List(1,2,3).map(_ * 2) = List(1,2,3).flatMap(x => List(x * 2))
      List(List(1, 2), List(3, 4)).flatten = List(List(1, 2), List(3, 4)).flatMap(x => x) = List(1,2,3,4)
    }

   */
}
