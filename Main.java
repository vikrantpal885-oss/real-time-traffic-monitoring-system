import java.util.*;

// Edge class
class Edge {
    int destination;
    int distance;
    int trafficLevel; // 1 = Low, 2 = Medium, 3 = High

    public Edge(int destination, int distance, int trafficLevel) {
        this.destination = destination;
        this.distance = distance;
        this.trafficLevel = trafficLevel;
    }

    public int getTravelTime() {
        return distance * trafficLevel;
    }
}

// Graph class
class Graph {
    int vertices;
    ArrayList<ArrayList<Edge>> adjList;

    public Graph(int vertices) {
        this.vertices = vertices;
        adjList = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    public void addRoad(int src, int dest, int distance, int trafficLevel) {
        adjList.get(src).add(new Edge(dest, distance, trafficLevel));
        adjList.get(dest).add(new Edge(src, distance, trafficLevel));
    }

    public ArrayList<Edge> getEdges(int node) {
        return adjList.get(node);
    }
}

// Dijkstra Algorithm class
class Dijkstra {

    public static void findFastestRoute(Graph graph, int source, int destination) {

        int[] time = new int[graph.vertices];
        int[] parent = new int[graph.vertices];

        Arrays.fill(time, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        PriorityQueue<int[]> pq =
                new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        time[source] = 0;
        pq.add(new int[]{source, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int node = current[0];

            for (Edge edge : graph.getEdges(node)) {
                int newTime = time[node] + edge.getTravelTime();

                if (newTime < time[edge.destination]) {
                    time[edge.destination] = newTime;
                    parent[edge.destination] = node;
                    pq.add(new int[]{edge.destination, newTime});
                }
            }
        }

        if (time[destination] == Integer.MAX_VALUE) {
            System.out.println("❌ No route available.");
            return;
        }

        printRoute(parent, destination);
        System.out.println("\n⏱ Total Travel Time: " + time[destination] + " minutes");
    }

    private static void printRoute(int[] parent, int dest) {
        Stack<Integer> path = new Stack<>();

        while (dest != -1) {
            path.push(dest);
            dest = parent[dest];
        }

        System.out.print("🚗 Fastest Route: ");
        while (!path.isEmpty()) {
            System.out.print(path.pop());
            if (!path.isEmpty()) System.out.print(" -> ");
        }
    }
}

// Traffic Manager class
class TrafficManager {

    Graph graph;
    Scanner sc = new Scanner(System.in);

    public TrafficManager(int junctions) {
        graph = new Graph(junctions);
    }

    public void addRoad() {
        System.out.print("Enter Source Junction: ");
        int src = sc.nextInt();

        System.out.print("Enter Destination Junction: ");
        int dest = sc.nextInt();

        System.out.print("Enter Distance (km): ");
        int distance = sc.nextInt();

        System.out.print("Traffic Level (1-Low, 2-Medium, 3-High): ");
        int traffic = sc.nextInt();

        graph.addRoad(src, dest, distance, traffic);
        System.out.println("✅ Road added successfully");
    }

    public void findRoute() {
        System.out.print("Enter Start Junction: ");
        int start = sc.nextInt();

        System.out.print("Enter End Junction: ");
        int end = sc.nextInt();

        Dijkstra.findFastestRoute(graph, start, end);
    }
}

// MAIN CLASS (only public class)
public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of junctions: ");
        int junctions = sc.nextInt();

        TrafficManager manager = new TrafficManager(junctions);

        while (true) {
            System.out.println("\n🚦 Real-Time Traffic Monitoring System");
            System.out.println("1. Add Road");
            System.out.println("2. Find Fastest Route");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    manager.addRoad();
                    break;
                case 2:
                    manager.findRoute();
                    break;
                case 3:
                    System.out.println("👋 Exiting application");
                    return;
                default:
                    System.out.println("❌ Invalid choice");
            }
        }
    }
}