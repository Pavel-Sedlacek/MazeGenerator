import java.io.File
import java.lang.StrictMath.pow
import kotlin.math.sqrt

class Maze {

    private val rectangles = mutableListOf<Rectangle>()
    var increment: Float = 0f

    private var point = mutableListOf(
        0f, 0f
    )

    fun init() {
        loadMaze("src/main/resources/Mazes/Maze001.txt")
        for (r: Rectangle in rectangles) {
            r.init()
        }
    }

    fun render() {
        for (r: Rectangle in rectangles) {
            r.render()
        }
    }

    private fun loadMaze(path: String) {
        val shader = Shaders()
        shader.init(Shaders.loadShader(Shaders.vertexShader), Shaders.loadShader(Shaders.fragmentShader))
        val file = File(path).useLines { it.toList() }
        increment = 2.0f / file.size
        var xCurP = -1f
        var yCurP = 1f
        for (j in file.indices) {
            for (i in file.indices) {
                rectangles.add(
                    Rectangle(
                        floatArrayOf(
                            xCurP, yCurP, 0f,
                            xCurP + increment, yCurP, 0f,
                            xCurP, yCurP - increment, 0f,
                            xCurP + increment, yCurP - increment, 0f,
                        ),
                        intArrayOf(
                            0, 1, 2,
                            1, 2, 3
                        ),
                        shader,
                        file[i][j] == '1'
                    )
                )
                yCurP -= increment
            }
            xCurP += increment
            yCurP = 1f
        }
    }

    fun updateColor() {
        for (r: Rectangle in rectangles) {
            r.updateColor(point)
        }
    }

    fun update() {
        updateColor()
    }

    private fun distancefromPoint(xCurP: Float, yCurP: Float): Float =
        sqrt((pow((point[0] - xCurP).toDouble(), 2.0)) + (pow((point[1] - yCurP).toDouble(), 2.0))).toFloat()
}
