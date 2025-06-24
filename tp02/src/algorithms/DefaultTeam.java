package algorithms;

import java.awt.Point;
import java.util.ArrayList;

public class DefaultTeam {
  public Tree2D calculSteiner(ArrayList<Point> points) {
    if (points == null || points.size() == 0) return null;
    int n = points.size();
    // 1. Générer toutes les arêtes possibles
    class Edge {
      int i, j;
      double dist;
      Edge(int i, int j, double d) { this.i = i; this.j = j; this.dist = d; }
    }
    ArrayList<Edge> edges = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        double d = points.get(i).distance(points.get(j));
        edges.add(new Edge(i, j, d));
      }
    }
    // 2. Trier les arêtes par distance croissante
    edges.sort((a, b) -> Double.compare(a.dist, b.dist));
    // 3. Système d'étiquettes pour détecter les cycles
    int[] label = new int[n];
    for (int i = 0; i < n; i++) label[i] = i;
    // 4. Liste des arêtes retenues
    ArrayList<Edge> solution = new ArrayList<>();
    for (Edge e : edges) {
      if (label[e.i] != label[e.j]) {
        solution.add(e);
        int oldLabel = label[e.j];
        int newLabel = label[e.i];
        for (int k = 0; k < n; k++) {
          if (label[k] == oldLabel) label[k] = newLabel;
        }
        if (solution.size() == n - 1) break;
      }
    }
    // 5. Construire l'arbre Tree2D à partir de la liste d'arêtes
    // On construit une structure d'adjacence
    ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
    for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    for (Edge e : solution) {
      adj.get(e.i).add(e.j);
      adj.get(e.j).add(e.i);
    }
    // On choisit le point 0 comme racine
    boolean[] visited = new boolean[n];
    for (int i = 0; i < n; i++) visited[i] = false;
    return buildTree2D(0, -1, points, adj, visited);
  }

  // Fonction récursive pour construire l'arbre Tree2D
  private static Tree2D buildTree2D(int u, int parent, ArrayList<Point> points, ArrayList<ArrayList<Integer>> adj, boolean[] visited) {
    visited[u] = true;
    ArrayList<Tree2D> sub = new ArrayList<>();
    for (int v : adj.get(u)) {
      if (v != parent && !visited[v]) {
        sub.add(buildTree2D(v, u, points, adj, visited));
      }
    }
    return new Tree2D(points.get(u), sub);
  }
}
