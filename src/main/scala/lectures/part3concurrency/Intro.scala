package lectures.part3concurrency

import java.util.concurrent.Executors

object Intro extends App {
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("running in parallel")
  })
  aThread.start()
  aThread.join() // blocks until finishes running
  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
  threadHello.start()
  threadGoodbye.start()
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool"))
  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 second")
  })
  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 seconds")
  })
//  pool.shutdown() // throws on main thread
//  pool.shutdownNow() // interrupts all tasks
  println(pool.isShutdown)
  pool.shutdown()

}
