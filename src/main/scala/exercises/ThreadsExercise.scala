package exercises

object ThreadsExercise {
  def testNotifyAll(): Unit = {
    val bell = new Object
    (1 to 10).foreach(i => new Thread(() => {
      bell.synchronized{
        println(s"thread $i waiting")
        bell.wait()
        println(s"thread $i hooray")
      }
    }).start())
    new Thread(() => {
      Thread.sleep(2000)
      println("announcer rock an roll")
      bell.synchronized{
        bell.notifyAll()
      }
    }).start()
  }

  case class Friend(name: String) {
    def bow(other: Friend): Unit = {
      synchronized{
        println(s"$this: I am bowing to my friend $other")
        other.rise(this)
        println(s"$this: my friend $other has risen")
      }
    }
    def rise(other: Friend): Unit = {
      synchronized {
        println(s"$this: I am rising to my friend $other")
      }
    }
    var side = "right"
    def switchSide(): Unit = if (side == "right") side = "left" else side = "right"
    def pass(other: Friend): Unit = {
      while(this.side == other.side) {
        println(s"$this: please $other feel free to pass")
        switchSide()
        Thread.sleep(1000)
      }
    }
  }

  val lia = Friend("Lia")
  val and = Friend("Anderson")
//  new Thread(() => and.bow(lia)).start()
//  new Thread(() => lia.bow(and)).start()

  new Thread(() => lia.pass(and)).start()
  new Thread(() => and.pass(lia)).start()


}
