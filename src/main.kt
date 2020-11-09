import java.io.File
import java.io.InputStream
import java.util.PriorityQueue


const val INFINITY: Int = 99999

// Lê arquivo txt na raiz e a transforma em uma matriz valorada
fun buildValuedMatrix(fileName: String): List<List<Int>> {
  val inputStream: InputStream = File(fileName).inputStream()
  val lineList = mutableListOf<String>()
  inputStream.bufferedReader().forEachLine { lineList.add(it) }
  return lineList.map{s -> s.split('\t').map{c -> c.toInt(10)}} // Retorna uma lista de inteiros com o valor de distancia
}

fun getVertex(vertices: List<Vertex>, desiredSource: Int): Vertex { // Busca no grafo o vertice N
  return vertices.first{ it.Source == desiredSource }
}

fun getShortestPath(source: Int, destiny: Vertex, graph: List<Vertex>): List<Int> { // constroi o caminho mais curto, do destino até o inicio depois o inverte
  val shortestPath: MutableList<Int> = mutableListOf()
  var currentVertex: Vertex = destiny
  while(true) {

    shortestPath.add(currentVertex.Source + 1)

    if (currentVertex.Source == source) break

    currentVertex = getVertex(graph, currentVertex.Prev!!)

  }
  return shortestPath.reversed()
}

fun main() {

  val list: List<List<Int>> = buildValuedMatrix("graph.txt")

  val sorter: Comparator<Vertex> = Comparator.comparingInt(Vertex::Distance)
  val graph: MutableList<Vertex> = mutableListOf()
  val q: PriorityQueue<Vertex> = PriorityQueue(sorter)



  println("detected ${list.size}x${list[0].size} vertex")

  println("type the origin vertex: ")
  val a = readLine()!!.toInt() - 1 // Lê nó de partida (subtrai um pois arrays começam em 0)

  println("type the destination vertex: ")
  val b = readLine()!!.toInt() - 1 // Lê nó de destino

  for (i in list.indices) { // Adiciona todas as arestas na fila de prioridade
    val v = Vertex(i, null, if (i == a) 0 else INFINITY)

    q.add(v)
    graph.add(v)
  }

  while (!q.isEmpty()) { // enquanto a fila de prioridade não está vazia

    val v = q.poll() // retirar proximo valor na fila com menor (não atualizado)

    for(i in list[v.Source].indices) { // para todas as arestas do grafo
      if(i != v.Source && list[v.Source][i] != INFINITY) { // se há ligação da aresta testada a vizinha

        val distance = list[v.Source][i] + v.Distance // variavel que tem o valor do ponto de partida até o vizinho

        val neighbor: Vertex? = getVertex(graph, i)

        when {
          neighbor == null -> {
            throw Error("something went wrong, try again")
          }
          // se a nova distancia for menor que a previamente registrada
          distance < neighbor.Distance -> { // Altere o valor de distancia
            neighbor.Distance = distance
            neighbor.Prev = v.Source
            q.add(getVertex(graph, neighbor.Source)) // Readicionar o grafo na fila para ser retestado pois ele pode ter seu caminho alterado
          }
        }
      }
    }
  }

  val destinyVertex = getVertex(graph, b)

  if(destinyVertex.Distance == INFINITY) {
    throw Error("Cannot Find Shortest Path")
  }

  println("FOUND SHORTEST PATH:\nDistance: ${destinyVertex.Distance}")
  println("Path: ${getShortestPath(a, destinyVertex, graph)}")
}