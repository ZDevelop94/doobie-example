
import cats.effect.implicits.concurrentParSequenceOps
import cats.effect.{IO, IOApp}
import cats.syntax.all.*

import doobie.*
import doobie.syntax.all.*

import scala.annotation.tailrec


object Main extends IOApp.Simple {
  println("Hello, World!")

  private final val repo: Repo = new Repo

  override def run: IO[Unit] = {
    val xa = Transactor.fromDriverManager[IO](
      driver = "org.postgresql.Driver", url = "jdbc:postgresql://localhost:5432/local", user = "onetestuser",
      password = "password", logHandler = None
    )

    //postgresql://onetestuser@localhost/one
    val staff: Seq[(String, String, Int)] = Seq(
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Bob", "Rogg", 50000),
      ("Zob", "Dob", 60000),
      ("Fob", "Hob", 70000),
      ("Hob", "Mob", 80000),
      ("Hob", "Mob", 80000),
      ("Hob", "Mob", 80000),
      ("Hob", "Mob", 80000),
      ("Hob", "Mob", 80000),
      ("Hob", "Mob", 80000),
      ("Hob", "Mob", 80000),
      ("Hob", "Mob", 80000),
      ("Hob", "Mob", 80000),
      ("Hob", "Mob", 80000),
      ("Hob", "Mob", 80000),
      ("Hob", "Mob", 80000),
      ("Hob", "Mob", 80000),
      ("Hob", "Mob", 80000),
      ("Hob", "Mob", 80000),
    )

    val timesTorun = 50

    def function(): IO[Long] = {
      for {
        //_ <- IO.println("Creating Tables") >> repo.createTables().transact(xa) >> IO.println("Inserting Data")
        _ <- IO.println("Inserting Data")
        before <- IO(System.currentTimeMillis())
        _ <- IO.parTraverseN(3)(staff)(member => repo.insertData(member._1, member._2, member._3).transact(xa)).void
        //staffIOs = staff.map(repo.insertData).map(_.transact(xa))
        //_ <- staffIOs.parSequenceN(3)
        timeTaken <- IO(System.currentTimeMillis() - before)
        _ <- IO.println(s"time taken: $timeTaken")
      } yield timeTaken
    }

    def benchmark(count: Int, resultss: IO[List[Long]]): IO[List[Long]] = {
      if(count == timesTorun) {
        resultss
      } else {
        (function(), resultss).flatMapN { (timeTaken, results) =>
          benchmark(count + 1, IO(results :+ timeTaken))
        }
      }
    }

    for {
      results <- benchmark(0, IO(List.empty[Long]))
      _ <- IO.println(results.sum / timesTorun.toLong)
    } yield ()

  }
}