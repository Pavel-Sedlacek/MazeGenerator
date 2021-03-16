import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

class Rectangle(
    private val vertices: FloatArray,
    private val indices: IntArray,
    private val shader: Shaders,
    private val color: Boolean
) {


    private var vboId: Int = 0
    private var vaoId: Int = 0
    private var eboId: Int = 0

    private var colorsLoc: Int = 0

    lateinit var fb: FloatBuffer

    fun init() {
        vaoId = GL33.glGenVertexArrays()
        vboId = GL33.glGenBuffers()
        eboId = GL33.glGenBuffers()

        colorsLoc = GL33.glGetUniformLocation(shader.shaderId, "fC")
        GL33.glBindVertexArray(vaoId)
        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, eboId)

        val ib: IntBuffer = BufferUtils.createIntBuffer(indices.size)
            .put(indices)
            .flip()
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW)

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboId)
        fb = BufferUtils.createFloatBuffer(vertices.size)
            .put(vertices)
            .flip()
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW)
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0)
        GL33.glEnableVertexAttribArray(0)

        MemoryUtil.memFree(ib)
    }

    fun updateColor(pX: Float, pY: Float) {
        GL33.glUseProgram(shader.shaderId)
        GL33.glBindVertexArray(vaoId)
        if (color)
            GL33.glUniform3f(colorsLoc, 0.0f, 1.0f, 0.0f)
        else
            GL33.glUniform3f(colorsLoc, 0.0f, 0.0f, 1.0f)
    }

    fun render() {
        GL33.glUseProgram(shader.shaderId)
        GL33.glBindVertexArray(vaoId)
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.size, GL33.GL_UNSIGNED_INT, 0)
    }

}
