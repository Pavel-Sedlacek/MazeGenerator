import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33
import org.lwjgl.system.MemoryUtil
import java.lang.Exception
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.sqrt
import kotlin.properties.Delegates
import kotlin.system.measureTimeMillis

class Player {

    private lateinit var vertices: FloatArray
    private lateinit var indices: IntArray
    private lateinit var shader: Shaders

    private var increment by Delegates.notNull<Float>()

    private lateinit var field: List<List<Boolean>>
    private var x by Delegates.notNull<Int>()
    private var y by Delegates.notNull<Int>()
    private var vboId: Int = 0
    private var vaoId: Int = 0
    private var eboId: Int = 0

    private var colorsLoc: Int = 0

    lateinit var fb: FloatBuffer

    fun init(field: List<List<Boolean>>, coords: Pair<Int, Int>, indices: IntArray, shader: Shaders, increment: Float) {

        this.vertices = floatArrayOf(
            -1 + coords.first * increment, 1 - coords.second * increment, 0f,
            -1 + coords.first * increment + increment, 1 - coords.second * increment, 0f,
            -1 + coords.first * increment + increment, 1 - coords.second * increment - increment, 0f,
            -1 + coords.first * increment, 1 - coords.second * increment - increment, 0f
        )

        x = coords.first
        y = coords.second
        this.indices = indices
        this.shader = shader
        this.increment = increment
        this.field = field

        vaoId = GL33.glGenVertexArrays()
        vboId = GL33.glGenBuffers()
        eboId = GL33.glGenBuffers()

        colorsLoc = GL33.glGetUniformLocation(this.shader.shaderId, "fC")

        println(this.shader.shaderId)

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

        GL33.glUseProgram(shader.shaderId)
        GL33.glBindVertexArray(vaoId)
        GL33.glUniform3f(colorsLoc, 1.0f, 0.0f, 0.0f)
        MemoryUtil.memFree(ib)

    }

    fun gameLoop() {
        updateColor()
        render()
    }

    private fun updateColor() {
        GL33.glUseProgram(shader.shaderId)
        GL33.glBindVertexArray(vaoId)
        GL33.glUniform3f(colorsLoc, 1.0f, 0.0f, 0.0f)
    }

    fun move(horizontal: Boolean, vertical: Boolean, value: Boolean) {

        try {
            if (horizontal) {
                if (value) {
                    if (field[y][x + 1]) {
                        for (i in 0 until 4) {
                            this.vertices[i * 3] += increment
                        }
                        x += 1
                    }
                } else {
                    if (field[y][x - 1]) {
                        for (i in 0 until 4) {
                            this.vertices[i * 3] += -increment
                        }
                        x -= 1
                    }
                }
            }
            if (vertical) {
                if (value) {
                    if (field[y - 1][x]) {
                        for (i in 0 until 4) {
                            this.vertices[i * 3 + 1] += increment
                        }
                        y -= 1
                    }
                } else {
                    if (field[y + 1][x]) {
                        for (i in 0 until 4) {
                            this.vertices[i * 3 + 1] += -increment
                        }
                        y += 1
                    }
                }
            }
        } catch (e: Exception) {}

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboId)
        fb = BufferUtils.createFloatBuffer(vertices.size)
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
