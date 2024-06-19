package exercises

object PatternMatchingExercise extends App {
  object matcher {
    def unapply(n: Int): Option[String] = if (n<10) Some("Singe digit") else if (n%2==0) Some("even number")
    else Some("no property")
  }

  val n = 42
  println(n match {
    case matcher(answer) => s"$answer"
  })
}
