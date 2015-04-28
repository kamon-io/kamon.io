// tag:activation:start
import kamon.annotation.EnableKamon
import kamon.annotation.Count

@EnableKamon
class Annotated {

	@Count(name="counter")
	def countedMethod:Unit = {}
}	
// tag:activation:end


package object el {
// tag:el-support:start
import kamon.annotation.EnableKamon
import kamon.annotation.Count

@EnableKamon
class Annotated {

	@Count(name="counter", tags = "#{'counter':'my-awesome-counter', 'environment':'prod'}")
	def countedMethod:Unit = {}	
}
// tag:el-support:end	
}


package object elName {
// tag:el-support-instrument-name:start
import kamon.annotation.EnableKamon
import kamon.annotation.Count

@EnableKamon
case class Annotated(id: Long) {

 	@Count(name = "${'count:' += this.id}", tags = "#{'my-awesome-counter':'1', 'env':'prod'}")
  	def countedMethod(): Unit = {}
	
}
// tag:el-support-instrument-name:end	
}


package object static {
// tag:static-methods:start
import kamon.annotation.EnableKamon
import kamon.annotation.Count

@EnableKamon
object AnnotatedObject {

 	@Count(name = "counter")
  	def countedMethod(): Unit = {}
	
}	
// tag:static-methods:end
}