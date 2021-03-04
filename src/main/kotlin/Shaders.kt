import org.lwjgl.opengl.GL33
import java.io.File

class Shaders {

    companion object {

        const val vertexShader: String = "src/main/resources/shaders/vertex_shader.glsl"
        const val fragmentShader: String = "src/main/resources/shaders/fragment_shader.glsl"

        fun loadShader(shaderPath: String): String = File(shaderPath).bufferedReader().readText()
    }

    private var fragmentId: Int = 0
    private var vertexId: Int = 0
    var shaderId: Int = 0

    fun init(vertexShader: String, fragmentShader: String) {
        vertexId = GL33.glCreateShader(GL33.GL_VERTEX_SHADER)
        fragmentId = GL33.glCreateShader(GL33.GL_FRAGMENT_SHADER)

        GL33.glShaderSource(vertexId, vertexShader)
        GL33.glCompileShader(vertexId)

        println(GL33.glGetShaderInfoLog(vertexId))

        GL33.glShaderSource(fragmentId, fragmentShader)
        GL33.glCompileShader(fragmentId)

        println(GL33.glGetShaderInfoLog(fragmentId))

        shaderId = GL33.glCreateProgram()

        GL33.glAttachShader(shaderId, vertexId)
        GL33.glAttachShader(shaderId, fragmentId)
        GL33.glLinkProgram(shaderId)

        println(GL33.glGetProgramInfoLog(shaderId))

        GL33.glDeleteShader(vertexId)
        GL33.glDeleteShader(fragmentId)
    }
}
