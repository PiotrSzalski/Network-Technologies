package lista2;

import java.util.ArrayList;
import java.util.Random;

public class Zad1 {
	
	private static ArrayList<Edge> edges = new ArrayList<>();
	private static boolean visited[] = new boolean[20];
	private static Random generator = new Random();
	
	public static void main(String[] args) {
		System.out.println("Linear: "+linearGraph());
		System.out.println("Circular: "+circularGraph());
		System.out.println("Two More Edge: "+twoMoreEdgeGraph());
		System.out.println("Four Random Edge: "+randomEdgeGraph());
		System.out.println("My Four Edge: "+myEdgeGraph());
	}
	
	private static double linearGraph() {
		int consistent = 0;
		for(int n = 0; n < 1000000; n++) {
			edges.clear();
			for(int i = 1; i < 20; i++) {
				if(generator.nextDouble() <= 0.95) {
					edges.add(new Edge(i,i+1));
				}	
			}
			for(int i = 0; i < 20;i++) {
				visited[i] = false;
			}
			explore(1);
			if(isConsistent()) {
				consistent++;
			}
		}
		return (double)consistent/1000000;
	}
	
	private static double circularGraph() {
		int consistent = 0;
		for(int n = 0; n < 1000000; n++) {
			edges.clear();
			for(int i = 1; i < 20; i++) {
				if(generator.nextDouble() <= 0.95) {
					edges.add(new Edge(i,i+1));
				}	
			}
			if(generator.nextDouble() <= 0.95) {
				edges.add(new Edge(1,20));
			}
			for(int i = 0; i < 20;i++) {
				visited[i] = false;
			}
			explore(1);
			if(isConsistent()) {
				consistent++;
			}
		}
		return (double)consistent/1000000;
	}
	
	private static double twoMoreEdgeGraph() {
		int consistent = 0;
		for(int n = 0; n < 1000000; n++) {
			edges.clear();
			for(int i = 1; i < 20; i++) {
				if(generator.nextDouble() <= 0.95) {
					edges.add(new Edge(i,i+1));
				}	
			}
			if(generator.nextDouble() <= 0.95) {
				edges.add(new Edge(1,20));
			}
			if(generator.nextDouble() <= 0.8) {
				edges.add(new Edge(1,10));
			}
			if(generator.nextDouble() <= 0.7) {
				edges.add(new Edge(5,15));
			}
			for(int i = 0; i < 20;i++) {
				visited[i] = false;
			}
			explore(1);
			if(isConsistent()) {
				consistent++;
			}
		}
		return (double)consistent/1000000;
	}
	
	private static double randomEdgeGraph() {
		int consistent = 0;
		for(int n = 0; n < 1000000; n++) {
			edges.clear();
			for(int i = 1; i < 20; i++) {
				if(generator.nextDouble() <= 0.95) {
					edges.add(new Edge(i,i+1));
				}
				if(i <= 4 && generator.nextDouble() <= 0.4) {
					int u = 0;
					int v = 0;
					while(u == v) {
						v = generator.nextInt(20) + 1;
						u = generator.nextInt(20) + 1;
					} 
					edges.add(new Edge(v,u));
				}
			}
			if(generator.nextDouble() <= 0.95) {
				edges.add(new Edge(1,20));
			}
			if(generator.nextDouble() <= 0.8) {
				edges.add(new Edge(1,10));
			}
			if(generator.nextDouble() <= 0.7) {
				edges.add(new Edge(5,15));
			}
			for(int i = 0; i < 20;i++) {
				visited[i] = false;
			}
			explore(1);
			if(isConsistent()) {
				consistent++;
			}
		}
		return (double)consistent/1000000;
	}
	
	private static double myEdgeGraph() {
		int consistent = 0;
		for(int n = 0; n < 1000000; n++) {
			edges.clear();
			for(int i = 1; i < 20; i++) {
				if(generator.nextDouble() <= 0.95) {
					edges.add(new Edge(i,i+1));
				}
			}
			if(generator.nextDouble() <= 0.95) {
				edges.add(new Edge(1,20));
			}
			if(generator.nextDouble() <= 0.8) {
				edges.add(new Edge(1,10));
			}
			if(generator.nextDouble() <= 0.7) {
				edges.add(new Edge(5,15));
			}
			
			if(generator.nextDouble() <= 0.4) {
				edges.add(new Edge(3,13));
			}
			if(generator.nextDouble() <= 0.4) {
				edges.add(new Edge(7,17));
			}
			if(generator.nextDouble() <= 0.4) {
				edges.add(new Edge(8,18));
			}
			if(generator.nextDouble() <= 0.4) {
				edges.add(new Edge(11,20));
			}
			for(int i = 0; i < 20;i++) {
				visited[i] = false;
			}
			explore(1);
			if(isConsistent()) {
				consistent++;
			}
		}
		return (double)consistent/1000000;
	}
 	
	private static void explore(int v) {
		visited[v-1] = true;
		for(int i=0; i < edges.size(); i++) {
			if(edges.get(i).getV() == v && !visited[edges.get(i).getU()-1]) {
				explore(edges.get(i).getU());
			} 
			if(edges.get(i).getU() == v && !visited[edges.get(i).getV()-1]) {
				explore(edges.get(i).getV());
			} 
		}
	}
	
	private static boolean isConsistent() {
		int n = 0;
		for(int i = 0; i < visited.length;i++) {
			if(visited[i]) {
				n++;
			}
		}
		return n == 20;
	}
	private static class Edge {
		private int u;
		private int v;
		
		public Edge(int v, int u) {
			this.u = u;
			this.v = v;
		}
		public int getU() {
			return u;
		}
		public int getV() {
			return v;
		}
	}
}