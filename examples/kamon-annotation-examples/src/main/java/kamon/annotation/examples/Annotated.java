package kamon.annotation.examples;

// tag:activation:start
import kamon.annotation.EnableKamon;
import kamon.annotation.Count;

@EnableKamon
public class Annotated {

	@Count(name="counter")
	public void countedMethod() {}
}
// tag:activation:end
