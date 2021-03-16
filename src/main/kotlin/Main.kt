import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL33
import java.lang.Exception
import kotlin.math.sin

fun main() {

    GLFW.glfwInit()

    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3)
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)

    val window: Long = GLFW.glfwCreateWindow(1080, 1080, "Maze", 0, 0)

    if (window == 0L) {
        GLFW.glfwTerminate()
        throw Exception("Unable to open window")
    }

    GLFW.glfwMakeContextCurrent(window)
    GL.createCapabilities()
    GL33.glViewport(0, 0, 1050, 1050)

    val shader = Shaders()
    shader.init(Shaders.loadShader(Shaders.vertexShader), Shaders.loadShader(Shaders.fragmentShader))

    val maze = Maze(shader)
    val player = Player()

    maze.init()
    player.init(
        maze.field,
        Pair(0, 0),
        intArrayOf(
            0, 1, 3,
            1, 2, 3
        ),
        shader,
        maze.increment
    )

    var down = false
    var up = false
    var left = false
    var right = false


    while (!GLFW.glfwWindowShouldClose(window)) {
        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT)

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
            GLFW.glfwSetWindowShouldClose(window, true)
        }


        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS) {
            if (!down) {
                player.move(false, true, false)
                down = true
            }
        } else {
            down = false
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
            if (!up) {
                player.move(false, true, true)
                up = true
            }
        } else {
            up = false
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS) {
            if (!right) {
                player.move(true, false, true)
                right = true
            }
        } else {
            right = false
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS) {
            if (!left) {
                player.move(true, false, false)
                left = true
            }
        } else {
            left = false
        }

        maze.gameLoop()

        player.gameLoop()

        GLFW.glfwSwapBuffers(window)

        GLFW.glfwPollEvents()
    }

    GLFW.glfwTerminate()

}

