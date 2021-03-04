import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL33
import java.lang.Exception
import kotlin.math.sin

fun main() {

    GLFW.glfwInit()

    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3)
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)

    val window: Long = GLFW.glfwCreateWindow(1080, 1080, "Maze", 0,0)

    if (window == 0L) {
        GLFW.glfwTerminate()
        throw Exception("Unable to open window")
    }

    GLFW.glfwMakeContextCurrent(window)
    GL.createCapabilities()
    GL33.glViewport(0,0, 1080, 1080)

    val maze = Maze()

    maze.init()
    while (!GLFW.glfwWindowShouldClose(window)) {
        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT)

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
            GLFW.glfwSetWindowShouldClose(window, true)
        }

        maze.update()

        maze.render()

        GLFW.glfwSwapBuffers(window)

        GLFW.glfwPollEvents()
    }

    GLFW.glfwTerminate()

}

