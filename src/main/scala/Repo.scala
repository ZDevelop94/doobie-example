
import doobie.*
import doobie.implicits.*

import java.util.UUID

class Repo {
  def createTables() =
    sql"""
          create table staff (
          uuid TEXT 	       PRIMARY KEY  NOT NULL,
          first_name 				 VARCHAR (100)  NOT NULL,
          surname 			     VARCHAR (100)	NOT NULL,
          salary             INTEGER        NOT NULL
          )
       """.update.run

  def insertData(firstName: String, surname: String, salary: Int) =
    val uuid = UUID.randomUUID().toString

    sql"""INSERT INTO staff (uuid, first_name, surname, salary)
         VALUES ($uuid, $firstName, $surname, $salary)
       """.update.run

  def find(firstName: String): ConnectionIO[Option[StaffMember]] =
    sql"select uuid, first_name, surname, salary from staff where first_name = $firstName".query[StaffMember].option

}