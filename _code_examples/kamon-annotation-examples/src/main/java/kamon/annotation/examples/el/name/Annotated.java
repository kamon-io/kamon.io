package kamon.annotation.examples.el.name;

// tag:el-support-instrument-name:start
import kamon.annotation.EnableKamon;
import kamon.annotation.Count;

@EnableKamon
public class Annotated {

	private final Long id;

	public Annotated(Long id) {
		this.id = id;
	}

 	@Count(name = "${'count:' += this.id}", tags = "#{'my-awesome-counter':'1', 'env':'prod'}")
  	public void countedMethod() {}
	
}	
// tag:el-support-instrument-name:end