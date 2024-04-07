import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
public class DinicAlgorithm0 {
    private int[] level;//хранит уровень вершины в графе
    private int[] pointer;//указатель на текущее ребро при обходе графа
    private Queue<Integer> queue;//очередь для обхода графа в ширину
    private int[][] graph;//матрица смежности графа
    public DinicAlgorithm0(int n) {
        level = new int[n];
        pointer = new int[n];
        queue = new LinkedList<>();
        graph = new int[n][n];
    }
    public void addEdge(int u, int v, int cap) {
        graph[u][v] += cap;
    }
    private boolean bfs(int source, int sink) { // для нахождения допустимой сети для заданной остаточной сети
        Arrays.fill(level, -1);
        level[source] = 0;
        queue.offer(source);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v = 0; v < graph.length; v++) {
                if (graph[u][v] > 0 && level[v] == -1) {
                    level[v] = level[u] + 1;
                    queue.offer(v);
                }
            }
        }
        return level[sink] != -1;
    }
    private int dfs(int u, int sink, int flow,List<Integer> path) { // для поиска блокирующего потока
        if (u == sink || flow == 0) {
            if (u == sink) {
                path.add(sink); 
            }
            return flow;
        }
        for (int v = pointer[u]; v < graph.length; v++) {
            pointer[u] = v;
            if (graph[u][v] > 0 && level[v] == level[u] + 1) {
                int f = dfs(v, sink, Math.min(flow, graph[u][v]), path);
                if (f > 0) {
                    graph[u][v] -= f;
                    graph[v][u] += f;
                    path.add(u);
                    return f;
                }
            }
        }
        return 0;
    }
    public int getMaxFlow(int source, int sink) {
        int flow = 0;
        int iteration = 0;

        while (bfs(source, sink)) {
            Arrays.fill(pointer, 0);
            List<Integer> path = new ArrayList<>();
            while (true) {
                int f = dfs(source, sink, Integer.MAX_VALUE,path);
                if (f == 0) {
                    break;
                }
                flow += f;
            }
            try {
                PrintWriter writer = new PrintWriter("D:/JAVAPROG/IssledOperJAVA/laba5_Dinica/src/graph" + iteration + ".dot");
                writer.println("digraph ResidualGraph {");
                for (int u = 0; u < graph.length; u++) {
                    for (int v = 0; v < graph.length; v++) {
                        if (graph[u][v] > 0) {
                            if (u == source) {
                                writer.println(u + " [style=filled, fillcolor=green];");
                            }
                            if (v == sink) {
                                writer.println(v + " [style=filled, fillcolor=green];");
                            }
                            writer.println(u + " -> " + v + " [label=\"" + graph[u][v] + "\"];");
                        }
                    }
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < path.size(); i++) {
                    int u = path.get(i);

                    if (i > 0) {
                        sb.append(">-");
                    }
                    sb.append(u);
                }
                writer.println("\"" + sb.reverse().toString() + "\";");
                writer.println("}");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("итерация " + iteration);
            iteration++;
            System.out.println(path);
        }
        return flow;
    }
    public void getMinCut(int source, int sink) {
        getMaxFlow(source, sink);
        Arrays.fill(level, -1);
        queue.clear();
        level[source] = 0;
        queue.offer(source);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v = 0; v < graph.length; v++) {
                if (graph[u][v] > 0 && level[v] == -1) {
                    level[v] = level[u] + 1;
                    queue.offer(v);
                }
            }
        }
        int minCut = 0;
        boolean[] visited = new boolean[graph.length]; // массив для отметки посещенных вершин
        visited[source] = true; // исток уже посещен
        // проходим по всем ребрам и добавляем вес только если одна вершина принадлежит разрезу, а другая - нет
        for (int u = 0; u < graph.length; u++) {
            for (int v = 0; v < graph.length; v++) {
                if (graph[u][v] > 0 && ((level[u] == -1 && level[v] != -1) || (level[u] != -1 && level[v] == -1))) {
                    // проверяем, что ребро не ведет от стока к истоку
                    if (!visited[u] || !visited[v]) {
                        minCut += graph[u][v];
                        visited[u] = true;
                        visited[v] = true;
                    }
                }
            }
        }
        System.out.println("Минимальный разрез: " + minCut);
        try {
            PrintWriter writer = new PrintWriter("D:/JAVAPROG/IssledOperJAVA/laba5_Dinica/src/mincut.dot");
            writer.println("digraph MinCut {");
            for (int u = 0; u < graph.length; u++) {
                for (int v = 0; v < graph.length; v++) {
                    if (graph[u][v] > 0 && ((level[u] == -1 && level[v] != -1) || (level[u] != -1 && level[v] == -1))) {
                        // проверяем, что ребро не ведет от стока к истоку
                        if (!visited[u] || visited[v]) {
                            writer.println(v + " -> " + u + " [label=\"" + graph[u][v] + "\"];");
                        }
                    }
                }
            }
            writer.println("}");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}