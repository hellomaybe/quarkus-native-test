package quarkusNativeTest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;

@Path("/hello")
@ApplicationScoped
@RegisterForReflection
public class GreetingResource {
	@Inject
	EventBus bus;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/event/{name}")
    public Uni<String> hello(String name) {
    	return bus.<String>request("hello_event", name)        
                .onItem().transform(Message::body);
    }

	@ConsumeEvent(value = "hello_event", local = false)
	public Uni<String> hello_event(String name){
		 return Uni.createFrom().item(name);
	}
}