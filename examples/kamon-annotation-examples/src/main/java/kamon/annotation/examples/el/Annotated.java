package kamon.annotation.examples.el;

// tag:el-support:start
import kamon.annotation.EnableKamon;
import kamon.annotation.Count;

@EnableKamon
public class Annotated {

	@Count(name="counter", tags = "#{'my-awesome-counter':'1', 'environment':'prod'}")
	public void countedMethod() {}
}	
// tag:el-support:end
