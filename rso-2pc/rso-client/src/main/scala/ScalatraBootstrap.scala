import _root_.akka.actor.ActorSystem
import com.mac.rso._
import org.scalatra._
import javax.servlet.ServletContext


class ScalatraBootstrap extends LifeCycle {
  val system = ActorSystem()

  override def init(context: ServletContext) {
    context.mount(new MainServlet(system), "/*")
  }
}
