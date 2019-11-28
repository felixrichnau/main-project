# main-project
Scala project for taking a directory and searching through files inside of it


A simple file-searching solution. It loads in the files of specified directory and then searches those files for the words user specified.

Since it's a rather simple solution, a word is just everything separated by space, so 
```
"hello world" 
```
is 
```
"hello"
```
and 
```
"world"
```
Would be easy to add other delimiters but since it's a simple solution, only that for now.


## To Run
Inside the project folder ( /main-project ) :
```
$ sbt
$ run <directory>
search
$ <words to search by, delimited by space>
no matches found
$ quit
```

For example

```
$ sbt
$ run /Users/felix/Desktop/felix
search
$ hello world
file.txt : 50% codegeek.txt : 50% vol08.iss0001-0071.txt 
search
$ quit
```
  
  
