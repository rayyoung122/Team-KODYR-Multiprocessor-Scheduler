package scheduling;

import graph.TaskNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a processor, with a list of tasks
 */
public class Processor {

    private int procID;
    private List<TaskNode> tasks = new ArrayList<TaskNode>();
    private int bound;

    /**
     * The processor with a number
     * @param number
     */
    public Processor(int number) {
        this.procID = number;
    }

    /**
     * Adds a new task, with the node.
     * Called by TaskNode.
     * @param node The node to be added.
     */
    public void addTask(TaskNode node, int time) {
        tasks.add(node);
        node.schedule(time, this);
        bound = time + node.getWeight();
    }

    /**
     * Removes a task from this node.
     * @param node
     */
    public void removeTask(TaskNode node) {
        tasks.remove(node);
        node.deschedule();
    }

    /**
     * Returns the current bound of this node.
     * @return
     */
    public int getBound() {
        return bound;
    }

    /**
     * Returns the nodes that have been scheduled on this processor.
     * @return
     */
    public List<TaskNode> getTasks() {
    	
    	return tasks;
    	
    }
}


