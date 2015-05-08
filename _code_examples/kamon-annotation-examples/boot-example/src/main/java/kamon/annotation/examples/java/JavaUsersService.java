package kamon.annotation.examples.java;

import kamon.annotation.EnableKamon;
import kamon.annotation.Segment;

// tag:segments:start
@EnableKamon
public class JavaUsersService {

    @Segment(name = "FindUsers", category = "database", library = "kamon")
    String findUsers() {
        return "Some list of users.";
    }
}
// tag:segments:end