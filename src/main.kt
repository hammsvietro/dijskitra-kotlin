import java.io.File
import java.io.InputStream
import java.util.PriorityQueue


const val INFINITY: Int = 9999999

fun buildValuedMatrix(fileName: String): List<List<Int>> {
  val inputStream: InputStream = File(fileName).inputStream()
  val lineList = mutableListOf<String>()
  inputStream.bufferedReader().forEachLine { lineList.add(it) }
  return lineList.map{s -> s.split(' ').map{c -> c.toInt(10)}}
}

fun getVertex(vertices: List<Vertex>, desiredSource: Int): Vertex? {
  return vertices.firstOrNull{ it.source == desiredSource }
}

fun main() {

  val list: List<List<Int>> = buildValuedMatrix("graph.txt")

  val sorter: Comparator<Vertex> = Comparator.comparing(Vertex::distance)
  val graph: MutableList<Vertex> = mutableListOf()
  val q: PriorityQueue<Vertex> = PriorityQueue(sorter)



  println("detected ${list.size} vertex")

  println("type the origin vertex: ")
  val a = readLine()?.toInt()

  println("type the destination vertex: ")
  val b = readLine()?.toInt()

  for (i in list.indices) {
    val v = Vertex(i, null, if (i == a) 0 else INFINITY)

    q.add(v)
    graph.add(v)
  }

  println(list[0].size - 1)
  while (!q.isEmpty()) {

    val v = q.poll()

    for(i in list[v.source].indices) {
      if(i != v.source && i != INFINITY) {

        val distance = list[v.source][i] + v.distance

        val neighbor: Vertex? = getVertex(graph, i)

        when {
          neighbor == null -> {
            throw Error("something went wrong, try again")
          }
          distance < neighbor.distance -> {
            neighbor.distance = distance
            neighbor.prev = v.source
          }
        }

      }
    }
    if(v.source == b) {

      println("found shrtest path: ${v.distance}")
    }

  }
}