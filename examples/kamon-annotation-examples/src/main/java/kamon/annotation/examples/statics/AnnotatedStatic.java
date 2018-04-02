package kamon.annotation.examples.statics;

// tag:static-methods:start
import kamon.annotation.EnableKamon;
import kamon.annotation.Count;

@EnableKamon
public class AnnotatedStatic {

 	@Count(name = "counter")
  	public static void countedMethod() {}
	
}	
// tag:static-methods:end
