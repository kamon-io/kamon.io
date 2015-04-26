// tag:activation:start
import kamon.annotation.EnableKamon;
import kamon.annotation.Count;

@EnableKamon
public class Annotated {

	@Count(name="counter")
	public void countedMethod() {}
}
// tag:activation:end

// tag:el-support:start
import kamon.annotation.EnableKamon;
import kamon.annotation.Count;

@EnableKamon
public class Annotated {

	@Count(name="counter", tags = "#{'my-awesome-counter':'1', 'environment':'prod'}")
	public void countedMethod() {}
}	
// tag:el-support:end

// tag:el-support-instrument-name:start
import kamon.annotation.EnableKamon;
import kamon.annotation.Count;

@EnableKamon
public class Annotated {

	private final Long id;

	public Annotated(id: Long) {
		this.id = id;
	}

 	@Count(name = "${'count:' += this.id}", tags = "#{'my-awesome-counter':'1', 'env':'prod'}")
  	public void countedMethod() {}
	
}	
// tag:el-support-instrument-name:end


// tag:static-methods:start
import kamon.annotation.EnableKamon;
import kamon.annotation.Count;

@EnableKamon
public class AnnotatedStatic {

 	@Count(name = "counter")
  	public static void countedMethod() {}
	
}	
// tag:static-methods:end
