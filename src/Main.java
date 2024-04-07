import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("D:/JAVAPROG/IssledOperJAVA/laba5_Dinica/gD.txt");
        Scanner scanner = new Scanner(inputFile);
        int n = scanner.nextInt();
        int[][] graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                graph[i][j] = scanner.nextInt();
            }
        }
        DinicAlgorithm0 dinic = new DinicAlgorithm0(n);
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                if (graph[u][v] > 0) {
                    dinic.addEdge(u, v, graph[u][v]);
                }
            }
        }
        // вывод изначального графа
        File outputFile = new File("D:/JAVAPROG/IssledOperJAVA/laba5_Dinica/src/graph100.dot");
        PrintWriter writer = new PrintWriter(outputFile);
        writer.println("digraph G {");
        for (int i = 0; i < n; i++) {
            writer.println(i + " [label=\"" + i + "\"];");
        }
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < n; v++) {
                if (graph[u][v] > 0) {
                    if (u == 0) {
                        writer.println(u + " [style=filled, fillcolor=green];");
                    }
                    if (v == n-1) {
                        writer.println(v + " [style=filled, fillcolor=green];");
                    }
                    writer.println(u + " -> " + v + " [label=\"" + graph[u][v] + "\"];");
                }
            }
        }
        writer.println("}");
        writer.close();
        int maxFlow = dinic.getMaxFlow(0, n - 1);
        System.out.println("Максимальный поток: " + maxFlow);
        dinic.getMinCut(0, n - 1);
    }
}
