import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.*

class Rectangle(
    private val vertices: FloatArray,
    private val indices: IntArray,
    private val shader: Shaders,
    private val color: Boolean
) {

    private var colors = if (color) {
        floatArrayOf(
            0f, 0f, 0f,
            0f, 0f, 0f,
            0f, 0f, 0f,
            0f, 0f, 0f,
        )
    } else {
        floatArrayOf(
            1f, 1f, 1f,
            1f, 1f, 1f,
            1f, 1f, 1f,
            1f, 1f, 1f,
        )
    }

    private var vboId: Int = 0
    private var vaoId: Int = 0
    private var eboId: Int = 0
    private var colId: Int = 0

    lateinit var cb: FloatBuffer
    lateinit var fb: FloatBuffer

    fun init() {

        vaoId = GL33.glGenVertexArrays()
        vboId = GL33.glGenBuffers()
        eboId = GL33.glGenBuffers()
        colId = GL33.glGenBuffers()

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

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, colId)

        cb = BufferUtils.createFloatBuffer(colors.size)
            .put(colors)
            .flip()
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cb, GL33.GL_STATIC_DRAW)
        GL33.glVertexAttribPointer(1, 3, GL33.GL_FLOAT, false, 0, 0)
        GL33.glEnableVertexAttribArray(1)

        MemoryUtil.memFree(ib)
    }

    private var x = 0.0

    fun updateColor(point: MutableList<Float>) {
        x += 0.01
        if (color) {
            for (i in colors.indices) {
                colors[i] =
                    if (i % 3 == 3) {
                        (sqrt(
                            ((this.vertices[0] - point[0]).toDouble()
                                .pow(2.0) +
                                    (this.vertices[1] - point[1]).toDouble()
                                .pow(2.0))
                        ) - sin(x) * .5).toFloat()
                    } else {
                        sin(x).toFloat()
                    }
            }
        } else {
            for (i in colors.indices) {
                colors[i] = if (i % 3 == 1) {
                    (sqrt(
                        ((this.vertices[0] - point[0]).toDouble()
                            .pow(2.0) +
                                (this.vertices[1] - point[1]).toDouble()
                            .pow(2.0))
                    ) - sin(x) * .5).toFloat()
                } else {
                    sin(x).toFloat()
                }
            }
        }
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, colId)
        cb.clear().put(colors).flip()
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cb, GL33.GL_STATIC_DRAW)

        for (i in vertices.indices) vertices[i] += (round(Math.random() * 2 -1) * 0.001f).toFloat()
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboId)
        fb.clear()
            .put(vertices)
            .flip()
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW)

    }

    fun render() {
        GL33.glUseProgram(shader.shaderId)
        GL33.glBindVertexArray(vaoId)
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.size, GL33.GL_UNSIGNED_INT, 0)
    }



}
