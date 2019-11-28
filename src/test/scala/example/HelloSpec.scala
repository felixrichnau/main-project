package example

import java.io.File

import example.Program.NotDirectory
import org.scalatest._

class HelloSpec extends FlatSpec with Matchers {

      "The folder should be " should "Right(Folder)" in {
        val args = Array("/Users/")
        Program.readFile(args) shouldEqual Right(new File("/Users"))
      }
      "The file should be left" should "not a folder" in {
     val args = Array("/test")
      Program.readFile(args) shouldEqual Left(new NotDirectory(s"Path [${args.head}] is not a directory"))
    }

}
