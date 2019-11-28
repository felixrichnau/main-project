package example

import java.io.File
import java.nio.charset.CodingErrorAction

import example.Program._

import scala.io.Codec
import scala.io.Source
import scala.util.Try
import scala.io.StdIn.readLine

object Main extends App {
  Program
    .readFile(args)
    .fold(println, file => Program.iterate(index(file)))
}

object Program {

  case class Index(filesInIndex: Array[File])

  case class FileIndex(name: String, words: List[String])

  sealed trait ReadFileError

  case object MissingPathArg extends ReadFileError

  case class NotDirectory(error: String) extends ReadFileError

  case class FileNotFound(t: Throwable) extends ReadFileError

  def readFile(args: Array[String]): Either[ReadFileError, File] = {
    for {
      path <- args.headOption.toRight(MissingPathArg)
      file <- Try(new java.io.File(path))
        .fold(
          throwable => Left(FileNotFound(throwable)),
          file =>
            if (file.isDirectory) Right(file)
            else Left(NotDirectory(error = s"Path [$path] is not a directory"))
        )
    } yield file
  }

  def index(file: File): Index = {
    val files = file.asInstanceOf[File].listFiles.filter(_.isFile)
    Index(filesInIndex = files)
  }

  def iterate(indexedFiles: Index): Unit = {
    val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.IGNORE)
    codec.onUnmappableCharacter(CodingErrorAction.IGNORE)
    val files = indexedFiles.filesInIndex.toList
      .flatMap { a =>
        val bufferedSource = Source.fromFile(a)(codec)
        val fileIndexed = if (a.isFile) {
          Some(
            FileIndex(
              a.getName,
              bufferedSource.getLines.toList.flatMap(_.split(" ").toList)
            )
          )
        } else {
          None
        }
        bufferedSource.close
        fileIndexed

      }
      .filter(_.words.nonEmpty)

    searchFile()

    def searchFile(): Unit = {

      println("search")
      val searchString = readLine()
      if (!searchString.equals("quit")) {
        val searchList = searchString.split(" ").toList
        val res = files
          .map(
            a =>
              (
                a.name,
                toPercentage(
                  searchList.count(word => a.words.exists(_ == word)),
                  searchList.size
                )
              )
          )
          .filter(_._2 > 0)
          .sortBy(_._2)(Ordering[Float].reverse).take(10)

        if (res.isEmpty) {
          println("no matches")
        } else {
          res.foreach(
            entry => print(entry._1 + " : " + Math.round(entry._2) + "% ")
          )
          println("")
        }
        searchFile()
      } else sys.exit
    }
  }

  def toPercentage(a: Int, b: Int): Float = {
    val div = a.toFloat / b * 100
    if (div.isNaN) 0.0f else div
  }
}