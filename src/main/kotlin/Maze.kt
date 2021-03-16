import java.io.File
import java.lang.StrictMath.pow
import kotlin.math.sqrt

class Maze(private val shader: Shaders) {

    private val rectangles = mutableListOf<Rectangle>()
    var increment: Float = 0f

    lateinit var field: MutableList<MutableList<Boolean>>

    private var point = mutableListOf(
        0f, 0f
    )

    fun init() {
        loadMaze("src/main/resources/Mazes/Maze001.txt")
        for (r: Rectangle in rectangles) {
            r.init()
        }
    }

    private fun loadMaze(path: String) {

        val file = File(path).useLines { it.toList() }
        field = MutableList(file.size) { MutableList(file[0].length) { false } }
        increment = 2.0f / file.size
        var xCurP = -1f
        var yCurP = 1f
        for (j in file.indices) {
            for (i in file.indices) {
                field[i][j] = file[i][j] == '1'
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

    fun gameLoop() {
        for (r: Rectangle in rectangles) {
            r.updateColor(point[0], point[1])
            r.render()
        }
    }
}
