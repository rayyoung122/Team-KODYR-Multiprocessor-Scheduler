package graph;

import scheduling.Processor;

import java.util.HashSet;

/**
 * created by Dweep on 29/7/2018
 */
public class TaskNode {
    private int weight;
    private String name;
    private boolean scheduled;
    private int startTime;
    private Processor processor;

    private TaskGraph graph; //Temporary. This is all I can think of atm, if there's a better way please change

    private int numberOfVisitedParents = 0;

    private HashSet<TaskEdge> incomingEdges;
    private HashSet<TaskEdge> outgoingEdges;

    public TaskNode(int weight, String name) {
        this.name = name;
        this.weight = weight;
        this.scheduled = false;
        this.startTime = -1;
        this.processor = null;
        incomingEdges = new HashSet<TaskEdge>();
        outgoingEdges = new HashSet<TaskEdge>();
    }

    /**
     * Visits the node
     */
    public void schedule(int startTime, Processor processor) {
        this.scheduled = true;
        this.startTime = startTime;
        this.processor = processor;
        for (TaskEdge edge : outgoingEdges) {
            edge.getEndNode().parentScheduled(); //Telling it's children that it's been scheduled
        }
    }

    public void deschedule() {
        scheduled = false;
        this.processor = null;
        this.startTime = -1;
        for (TaskEdge edge : outgoingEdges) {
            edge.getEndNode().parentNoLongerScheduled(); //Telling its children it's been unscheduled
        }
    }

    public void setGraph(TaskGraph graph) {
        this.graph = graph;
    }

    /**
     * This method indicates a parent has been scheduled.
     * It is asking this node to increase the number of visited parents
     * If this number is equal to the number of parents, then it will
     * be added to the taskgraph
     */
    private void parentScheduled() {
        numberOfVisitedParents++;
        if (numberOfVisitedParents == incomingEdges.size()) {
            graph.nodeMadeAvailable(this);
        }
    }


    /**
     * Indicates that a node is no longer available
     */
    private void parentNoLongerScheduled() {
        if (numberOfVisitedParents == incomingEdges.size()) {
            graph.nodeNoLongerAvailable(this);
        }
        numberOfVisitedParents--;
    }


    public void addIncomingEdge(TaskEdge edge) {
        incomingEdges.add(edge);
    }

    public void addOutgoingEdge(TaskEdge edge) {
        outgoingEdges.add(edge);
    }

    public HashSet<TaskEdge> getIncomingEdges() {
        return incomingEdges;
    }

    public HashSet<TaskEdge> getOutgoingEdges() {
        return outgoingEdges;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public String getName() {
        return name;
    }


    /*public TaskNode getUnvisitedChildNode() {
        if (this.children.containsValue(true)) {
            for (Map.Entry<TaskNode,Boolean> entry : this.children.entrySet()) {
                if () {
                    return ;
                }
            }
        }
    }
    */

}
