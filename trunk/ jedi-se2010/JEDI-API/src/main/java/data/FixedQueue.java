package data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class FixedQueue<T> {
	private Queue<T> queue;
	private int length;
	
	public FixedQueue(int length){
		this.queue = new LinkedList<T>();
		this.length = length;
	}
	
	public void add(T obj){
		
		//if the queue is full, remove the last item
		if(this.queue.size() == this.length){
			
			//get the iterator
			Iterator<T> iterator = this.queue.iterator();
			
			//remove the last item
			this.queue.remove(iterator.next());
		}
		
		//add the new axis
		this.queue.add(obj);
	}
	
	public Collection<T> getArray(){
		//convert to an array
//		Axis[] aux = (Axis[]) this.queue.toArray();
		
		if(this.queue.size() < this.length)
			return null;
		
		Collection<T> ans = new ArrayList<T>();
		
		//copy each elementO
//		for(int i = 0; i < aux.length; i++)
//			ans.add(aux[i]);
		
		//get the iterator
		Iterator<T> iterator = this.queue.iterator();
		
		//get the last item
		for(int i = 0; i < this.queue.size() - 1; i++)
			ans.add(iterator.next());
		
		return ans;
	}
	
	public int size(){
		return queue.size();
	}
	
	public T getLast(){
		//get the iterator
		Iterator<T> iterator = this.queue.iterator();
		
		//get the last item
		for(int i = 0; i < this.queue.size() - 1; i++)
			iterator.next();
		
		//get the last item
		return iterator.next();
	}
	
	@Override
	public String toString() {
		String ans = "[";
		
		for(T obj : this.queue){
			ans += obj.toString() + ";";
		}
		
		ans.substring(ans.length()-1).replace(';', ']');
		
		return ans;
	}
}
