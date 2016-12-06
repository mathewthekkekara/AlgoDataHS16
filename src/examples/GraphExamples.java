package examples;

import graphLib.Edge;
import graphLib.Graph;
import graphLib.IncidenceListGraph;
import graphLib.Vertex;
import graphTool.Algorithm;
import graphTool.Attribute;
import graphTool.GraphTool;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;


public class GraphExamples<V,E> {	 
	// annotations for Graph tool:
	// (Argumente:  Graph, GraphTool)
	// @Algorithm
	// (Argumente:  Graph, Vertex, GraphTool)
	// @Algorithm(vertex=true) 
	// (Argumente:  Graph, Vertex, Vertex, GraphTool)
	// @Algorithm(vertex=true,vertex2=true) 
		
	@Algorithm(vertex=true,vertex2=true)
	public void findPath(Graph<V,E> g, Vertex<V> from, Vertex<V> to, GraphTool<V, E> t){
		from.set(Attribute.VISITED,null);
		LinkedList<Vertex> qu = new LinkedList<>();
		qu.addLast(from);
		while ( ! qu.isEmpty()){
			Vertex w = qu.removeFirst();
			Iterator<Edge<E>> it = g.incidentEdges(w);
			Vertex u=null;
			while (it.hasNext()){
				Edge e = it.next();
				u = g.opposite(e,w);
				if ( ! u.has(Attribute.VISITED)){
					u.set(Attribute.DISCOVERY,e);
					u.set(Attribute.VISITED,null);
					qu.addLast(u);
				}
				if (u==to) break;
			}
			if (u==to) break;
		}
		while (to.has(Attribute.DISCOVERY)){
			to.set(Attribute.color,Color.green);
			Edge discov = (Edge) to.get(Attribute.DISCOVERY);
			discov.set(Attribute.color,Color.green);
			to = g.opposite(discov,to);
			t.show(g);
		}
	}

	
	@Algorithm(vertex=true)
	public void dfs(Graph<V,E> g, Vertex<V> s, GraphTool<V, E> t){
		// recursive depth first serach
		s.set(Attribute.VISITED,null);
		s.set(Attribute.color,Color.green);
		t.show(g);
		Iterator<Edge<E>> it = g.incidentEdges(s);
		while(it.hasNext()){
			Edge e = it.next();
			Vertex w = g.opposite(e,s);
			if (! w.has(Attribute.VISITED)){
				e.set(Attribute.color,Color.green);
				dfs(g, w, t);			
				t.show(g);
			}
		}
		
	}

	@Algorithm
	public void kruskal(Graph<V,E> g,  GraphTool<V, E> t){

		Iterator<Vertex<V>> it = g.vertices();
		while (it.hasNext()){
			Vertex v = it.next();
			ArrayList<Vertex> cluster = new ArrayList<>();
			cluster.add(v);
			v.set(Attribute.CLUSTER,cluster);
		}
		PriorityQueue<Double,Edge> pq = new MyPriorityQueue<>();
		Iterator<Edge<E>> eit = g.edges();
		while(eit.hasNext()){
			Edge e = eit.next();
			double weight = 1.0;
			if (e.has(Attribute.weight)) weight=(Double)e.get(Attribute.weight);
			pq.insert(weight, e);
		}
		while ( ! pq.isEmpty()){
			Edge e = pq.removeMin().element();
			Vertex[] endVertices = g.endVertices(e);
			Vertex v0 = endVertices[0];
			Vertex v1 = endVertices[1];
			ArrayList<Vertex> cluster0 =(ArrayList<Vertex>) v0.get(Attribute.CLUSTER);
			ArrayList<Vertex> cluster1 =(ArrayList<Vertex>) v1.get(Attribute.CLUSTER);
			if (cluster0 != cluster1){
				if (cluster0.size()<cluster1.size()){
					ArrayList<Vertex> tmp=cluster0;
					cluster0=cluster1;
					cluster1=tmp;
				}
				// now cluster 0 is the larger set
				// we copy cluster 1 to cluster 0
				// (and change the attribute!)
				for (Vertex v:cluster1){
					cluster0.add(v);
					v.set(Attribute.CLUSTER,cluster0);
				}
				e.set(Attribute.color,Color.green);
				t.show(g);
			}

//
//			ArrayList<Vertex> clu0 = (ArrayList<Vertex>) endVertices[0].get(Attribute.CLUSTER);
//			Iterator clu0It = clu0.iterator();
//			ArrayList<Vertex> clu1 = (ArrayList<Vertex>) endVertices[1].get(Attribute.CLUSTER);
//
//			if(clu0 != clu1){
//				//color the Nodes
//				colorClustercontent(clu0,Color.red);
//				colorClustercontent(clu1, Color.blue);
//				t.show(g);
//				if(clu0.size() > clu1.size()) shiftVertices(clu1,clu0);
//				else shiftVertices(clu0,clu1);
//			}
//			t.show(g);
		}
	}

	public ArrayList<Vertex> shiftVertices(ArrayList<Vertex> cluSource, ArrayList<Vertex> cluTarget){
		while(!cluSource.isEmpty()){
			Vertex temp = cluSource.remove(0);
			temp.set(Attribute.CLUSTER,cluTarget);
			cluTarget.add(cluSource.remove(0));
		}
		//color the biggest clusters content green
		colorClustercontent(cluTarget,Color.green);
		return cluTarget;
	}

	public void colorClustercontent(ArrayList<Vertex> clu, Color col){
		for (int i = 0;i<clu.size();i++){
			clu.get(0).set(Attribute.color,col);
		}
	}

	@Algorithm(vertex=true)
	public void dijkstra(Graph<V,E> g, Vertex<V> s, GraphTool<V, E> t){
		PriorityQueue<Double,Vertex> pq = new MyPriorityQueue<>();
		Iterator<Vertex<V>> it = g.vertices();
		while (it.hasNext()){
			Vertex v = it.next();
			v.set(Attribute.DISTANCE,Double.POSITIVE_INFINITY);
			v.set(Attribute.PQLOCATOR,pq.insert(Double.POSITIVE_INFINITY,v));
		}
		s.set(Attribute.DISTANCE,0);
		pq.replaceKey((Locator)s.get(Attribute.PQLOCATOR),0.0);
		while ( ! pq.isEmpty()){
			Vertex cur = pq.removeMin().element();
			cur.set(Attribute.color,Color.green);
			if (cur.has(Attribute.DISCOVERY)){
				((Edge)cur.get(Attribute.DISCOVERY)).set(Attribute.color,Color.green);
			}
			t.show(g);

		}
	}

	@Algorithm(vertex=true)
	public void bfs(Graph<V,E> g, Vertex<V> s, GraphTool<V, E> t){
		s.set(Attribute.VISITED,null);
		LinkedList<Vertex> qu = new LinkedList<>();
		qu.addLast(s);
		while ( ! qu.isEmpty()){
			Vertex w = qu.removeFirst();
			w.set(Attribute.color,Color.green);
			if (w.has(Attribute.DISCOVERY)){
				Edge discov = (Edge) w.get(Attribute.DISCOVERY);
				discov.set(Attribute.color,Color.green);
			}
			t.show(g);			
			Iterator<Edge<E>> it = g.incidentEdges(w);
			while (it.hasNext()){
				Edge e = it.next();
				Vertex u = g.opposite(e,w);
				if ( ! u.has(Attribute.VISITED)){
					u.set(Attribute.DISCOVERY,e);
					u.set(Attribute.VISITED,null);
					qu.addLast(u);
				}
			}
		}
	}

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {

		//		 make an undirected graph
		IncidenceListGraph<String,String> g =
				new IncidenceListGraph<>(false);
		GraphExamples<String,String> ge = new GraphExamples<>();
		Vertex vA = g.insertVertex("A");
		vA.set(Attribute.name,"A");
		Vertex vB = g.insertVertex("B");
		vB.set(Attribute.name,"B");
		Vertex vC = g.insertVertex("C");
		vC.set(Attribute.name,"C");
		Vertex vD = g.insertVertex("D");
		vD.set(Attribute.name,"D");
		Vertex vE = g.insertVertex("E");
		vE.set(Attribute.name,"E");
		Vertex vF = g.insertVertex("F");
		vF.set(Attribute.name,"F");
		Vertex vG = g.insertVertex("G");
		vG.set(Attribute.name,"G");

		Edge e_a = g.insertEdge(vA,vB,"AB");
		e_a.set(Attribute.weight,7.8);
		Edge e_b = g.insertEdge(vD,vC,"DC");
		e_b.set(Attribute.weight,9.4);
		Edge e_c = g.insertEdge(vD,vB,"DB");
		e_c.set(Attribute.weight,6.1);
		Edge e_d = g.insertEdge(vC,vB,"CB");
		e_d.set(Attribute.weight,2.4);
		Edge e_e = g.insertEdge(vC,vE,"CE");
		e_e.set(Attribute.weight,2.0);
		Edge e_f = g.insertEdge(vB,vE,"BE");
		e_f.set(Attribute.weight, 7.0);
		Edge e_g = g.insertEdge(vD,vE,"DE");

		Edge e_h = g.insertEdge(vE,vG,"EG");
		e_h.set(Attribute.weight,3.0);
		Edge e_i = g.insertEdge(vG,vF,"GF");
		Edge e_j = g.insertEdge(vF,vE,"FE");
		
		// use graph Tool
		
		GraphTool t = new GraphTool(g,ge);

		//    A__B     F
		//      /|\   /|
		//     / | \ / |
		//    C__D__E__G   
		//    \     /
		//     \___/
		//  
		System.out.println(g);
	}
}
