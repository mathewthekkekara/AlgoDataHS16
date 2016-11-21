package examples;

import com.sun.tools.doclint.HtmlTag;
import graphLib.*;
import graphTool.Algorithm;
import graphTool.Attribute;
import graphTool.GraphTool;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;


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
		Vertex knoten = qu.removeFirst();
		knoten.set(Attribute.VISITED,true);
		Vertex v = null;
		while(!knoten.equals(to) || qu.isEmpty()){
			knoten.set(Attribute.color,Color.red);
			t.show(g);
			Iterator<Edge<E>> it = g.incidentEdges(knoten);
			while(it.hasNext()){
				Edge<E> e = it.next();
				v = g.opposite(e,knoten);
				if(!v.has(Attribute.VISITED)){
					e.set(Attribute.color,Color.green);
					v.set(Attribute.DISCOVERY,e);
					v.set(Attribute.VISITED,true);
					v.set(Attribute.color,Color.green);
					if(v.equals(to))break;
					qu.addLast(v);
					t.show(g);
				}
			}
			if(qu.isEmpty()||v.equals(to)) break;
			knoten = qu.removeFirst();
		}
		v.set(Attribute.color,Color.BLUE);
		while(v.has(Attribute.DISCOVERY)){
			Edge e = (Edge) v.get(Attribute.DISCOVERY);
			e.set(Attribute.color,Color.BLUE);
			v = g.opposite(e,v);
			v.set(Attribute.color,Color.BLUE);
			t.show(g);
			if(v.equals(from)) break;
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

	@Algorithm(vertex=true)
	public void bfs(Graph<V,E> g, Vertex<V> s, GraphTool<V, E> t){
		// recursive depth first serach
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
		Edge e_b = g.insertEdge(vD,vC,"DC");
		Edge e_c = g.insertEdge(vD,vB,"DB");
		Edge e_d = g.insertEdge(vC,vB,"CB");
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
