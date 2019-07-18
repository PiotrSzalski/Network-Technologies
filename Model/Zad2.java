package lista2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;


public class Zad2 {

	private static Random gen = new Random();
	private static boolean visited[] = new boolean[10];
	private static int[][] amatrix = new int[10][10];
	private static Scanner sc;
	private static int[][] maxintensity = new int[10][10];
	private static int[][] intensity = new int[10][10];
	private static ArrayList<Edge> edges = new ArrayList<>();
	private static ArrayList<Edge> e = new ArrayList<>();
	private static double p, maxt;
	
	public static void main(String[] args) throws FileNotFoundException {
		int n = 0;
		readGraph();
		for(int t = 0; t < 100000; t++) {
			edges = (ArrayList<Edge>) e.clone();
			for(int i = 0; i < edges.size(); i++) {
				if(gen.nextDouble() > p) {
					edges.remove(i);
				}
			}
			for(int i = 0; i < 10;i++) {
				visited[i] = false;
			}
			explore(1);
			if(!isConsistent()) {
				continue;
			}
			for(int i=0 ;i < 10; i++) {
				for(int j = 0; j < 10; j++) {
					amatrix[i][j] = 0;
				}
			}
			for(int i=0 ;i < 10; i++) {
				for(int j = i+1; j < 10; j++) {
					if(intensity[i][j] != 0 || intensity[j][i] != 0) {
						ArrayList<Edge> path = shortestPath(i+1,j+1);
						for(int k = 0; k < path.size(); k++) {
							amatrix[path.get(k).getU() - 1][path.get(k).getV() - 1] += intensity[i][j] + intensity[j][i];
							amatrix[path.get(k).getV() - 1][path.get(k).getU() - 1] += intensity[i][j] + intensity[j][i];
						}
					}
				}
			}
			if(!isIntensity()) {
				continue;
			}
			if(delay() > maxt) {
				continue;
			}
			n++;
		}
		System.out.println((double)n/100000);
	}
	
	private static void readGraph() throws FileNotFoundException {
		sc = new Scanner(new File("graph1.txt"));
		String[] line;
		sc.nextLine();
		p = Double.parseDouble(sc.nextLine());
		sc.nextLine();
		maxt = Double.parseDouble(sc.nextLine());
		sc.nextLine();
		line = sc.nextLine().split(" ");
		for(int i=0;i<line.length;i+=2) {
			e.add(new Edge(Integer.parseInt(line[i]),Integer.parseInt(line[i+1])));
		}
		sc.nextLine();
		for(int i=0;i<10;i++) {
			line = sc.nextLine().split(",");
			for(int k=0;k<10;k++) {
				maxintensity[i][k] = Integer.parseInt(line[k]);
			}
		}
		sc.nextLine();
		for(int i=0;i<10;i++) {
			line = sc.nextLine().split(",");
			for(int k=0;k<10;k++) {
				intensity[i][k] = Integer.parseInt(line[k]);
			}
		}
	}
	
	private static double delay() {
		int g = 0;
		double sum = 0;
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				g += intensity[i][j];
			}
		}
		for(int k=0 ; k < edges.size(); k++) {
			int i = edges.get(k).getU()-1;
			int j = edges.get(k).getV()-1;
			sum += amatrix[i][j] / ( maxintensity[i][j] / 500 - amatrix[i][j] );
		}
		return sum/(double)g;
	}
	
	private static ArrayList<Edge> shortestPath(int i, int j) {
		boolean vis[] = new boolean[10];
		int p[] = new int[10];
		Queue<Integer> q = new LinkedList<>();
		for(int k = 0; k < 10; k++) {
			p[k] = 0;
			vis[k] = false;
		}
		p[i-1] = -1;
		vis[i-1] = true;
		q.add(i);
		while(!q.isEmpty()) {
			Integer v = q.remove();
			if( v == j ) {
				ArrayList<Edge> e = new ArrayList<>();
				int k = j;
				while( p[k-1] != -1) {
					e.add(new Edge(k,p[k-1]));
					k = p[k-1];
				}
				return e;
			}
			for(int k = 0; k < edges.size(); k++) {
				if(edges.get(k).getV() == v && !vis[edges.get(k).getU() - 1]) {
					p[edges.get(k).getU() - 1] = v;
					q.add(edges.get(k).getU());
					vis[edges.get(k).getU() - 1] = true;
				} else if(edges.get(k).getU() == v && !vis[edges.get(k).getV() - 1]) {
					p[edges.get(k).getV() - 1] = v;
					q.add(edges.get(k).getV());
					vis[edges.get(k).getV() - 1] = true;
				}
			}
		}
		return null;
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
		return n == 10;
	}
	
	private static boolean isIntensity() {
		for(int i=0 ;i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				if(amatrix[i][j] != 0 && amatrix[i][j] * 500 >= maxintensity[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	private static class Edge {
		private int u;
		private int v;
		
		public Edge(int v, int u) {
			this.v = v;
			this.u = u;
			
		}
		public int getU() {
			return u;
		}
		public int getV() {
			return v;
		}
	}
}
