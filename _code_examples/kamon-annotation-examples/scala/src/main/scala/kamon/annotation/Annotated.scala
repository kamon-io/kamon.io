// tag:activation:start
@EnableKamon
class Annotated {

	@Count(name="counter")
	def countedMethod:Unit = {}
}	
// tag:activation:end