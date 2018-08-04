package testCases;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.graphstream.graph.Graph;
import org.junit.Before;
import org.junit.Test;

import graph.TaskEdge;
import graph.TaskGraph;
import graph.TaskNode;
import io.GraphLoader;
import io.Output;
import scheduling.GreedyScheduler;
import scheduling.Processor;
import scheduling.Schedule;
import scheduling.SchedulerI;

public class AllTests extends testCases.CompareOutput {
	
	//Test Variables
    private static List<String> filePaths;
	private static int processorNum;

	@Before
	public void initialisePaths() {

		processorNum = 2;
		
		//file paths for all the tests
		filePaths = new ArrayList<String>();
		filePaths.addAll(Arrays.asList("src/main/resources/DotFiles/Nodes_7_OutTree.dot", "src/main/resources/DotFiles/Test1.dot",
				"src/main/resources/DotFiles/TestTwoParents.dot", "src/main/resources/DotFiles/Nodes_10_Random.dot",
				"src/main/resources/DotFiles/Nodes_9_SeriesParallel.dot", "src/main/resources/DotFiles/Nodes_11_OutTree.dot",
				"src/main/resources/DotFiles/Nodes_8_Random.dot"));
	}
	
	//Tests the greedy schedule on whether it gains a output based upon the greedy rule of weights
	@Test
	public void testGreedySchedule() {
		for (String filePath : filePaths) {
			greedySchedule(filePath);
		}
		//greedySchedule("src/main/resources/DotFiles/Test1.dot");
	}

	private void greedySchedule(String filePath) {

		GraphLoader loader = new GraphLoader();
		TaskGraph graph = loader.load(filePath);

		GreedyScheduler schedule = new GreedyScheduler(graph, processorNum);
		Schedule solution = schedule.createSchedule();

		int nodeNum = 0;
		for (Processor processor : solution.getProcessors()) {
			int totalWeight = 0;

			for (TaskNode node : processor.getTasks()) {

				for (TaskEdge edge : node.getIncomingEdges()) {
					TaskNode parentNode = edge.getStartNode();
					int parentStart = parentNode.getEndTime();
					int nodeStart = node.getEndTime();

					//System.out.println(parentNode.getName() + " " + parentStart + " " + node.getName() + " " + nodeStart);
					//Checks that the current node is scheduled later than its parents
					assertTrue(parentStart <= nodeStart);

				}
				nodeNum++;
			}

			//Checks each node is only schedule once, i.e. no duplicate nodes in any processor
			HashSet<TaskNode> set = new HashSet<TaskNode>(processor.getTasks());
			if(set.size() < processor.getTasks().size()){
				fail();
			}

		}

	}
	
	
	@Test
	public void testTest1Dot() throws IOException {
		GraphLoader loader = new GraphLoader();
		TaskGraph graph = loader.load("src/main/resources/DotFiles/Test1.dot");

		GreedyScheduler schedule = new GreedyScheduler(graph, 2);
		Schedule solution = schedule.createSchedule();

		Output.createOutput(solution.getProcessors(), graph, "greedySolution");



		Schedule correctSolution = new Schedule(2, graph);
		List<Processor> processes = correctSolution.getProcessors();

		Processor p1 = processes.get(0);
		Processor p2 = processes.get(1);

		p1.addTask(new TaskNode(3, "a"), 0);
		p1.addTask(new TaskNode(4, "b"), 3);
		p2.addTask(new TaskNode(6, "c"), 4);
		p2.addTask(new TaskNode(3, "d"), 10);

		Output.createOutput(processes, graph, "CorrectSolution");

		boolean same = compareTextFiles("src/main/resources/DotFiles/greedySolution-output.dot", "src/main/resources/DotFiles/CorrectSolution-output.dot");
		assertTrue(same);
	}

	@Test
	public void testTripleProcessorDot() throws IOException {
		GraphLoader loader = new GraphLoader();
		TaskGraph graph = loader.load("src/main/resources/DotFiles/TripleProcessor.dot");

		SchedulerI schedule = new GreedyScheduler(graph, 3);
		Schedule solution = schedule.createSchedule();

		Output.createOutput(solution.getProcessors(), graph, "greedySolutionTripleProcessor");



		Schedule correctSolution1 = new Schedule(3, graph);
		List<Processor> processes1 = correctSolution1.getProcessors();

		Processor p1 = processes1.get(0);
		Processor p2 = processes1.get(1);
		Processor p3 = processes1.get(2);

		p1.addTask(new TaskNode(2, "d"), 0);
		p2.addTask(new TaskNode(5, "a"), 0);
		p3.addTask(new TaskNode(8, "g"), 0);
		p2.addTask(new TaskNode(4, "b"), 5);
		p3.addTask(new TaskNode(6, "c"), 8);
		p3.addTask(new TaskNode(7, "f"), 14);
		p2.addTask(new TaskNode(4, "e"), 19);

		Output.createOutput(processes1, graph, "CorrectTripleProcessorS1");


		Schedule correctSolution2 = new Schedule(3, graph);
		List<Processor> processes2 = correctSolution2.getProcessors();

		Processor twop1 = processes2.get(0);
		Processor twop2 = processes2.get(1);
		Processor twop3 = processes2.get(2);

		twop1.addTask(new TaskNode(2, "d"), 0);
		twop2.addTask(new TaskNode(5, "a"), 0);
		twop3.addTask(new TaskNode(8, "g"), 0);
		twop2.addTask(new TaskNode(4, "b"), 5);
		twop3.addTask(new TaskNode(6, "c"), 8);
		twop2.addTask(new TaskNode(7, "f"), 16);
		twop3.addTask(new TaskNode(4, "e"), 14);

		Output.createOutput(processes2, graph, "CorrectTripleProcessorS2");


		Schedule correctSolution3 = new Schedule(3, graph);
		List<Processor> processes3 = correctSolution3.getProcessors();

		Processor threep1 = processes3.get(0);
		Processor threep2 = processes3.get(1);
		Processor threep3 = processes3.get(2);

		threep1.addTask(new TaskNode(2, "d"), 0);
		threep2.addTask(new TaskNode(5, "a"), 0);
		threep3.addTask(new TaskNode(8, "g"), 0);
		threep2.addTask(new TaskNode(4, "b"), 5);
		threep3.addTask(new TaskNode(6, "c"), 8);
		threep1.addTask(new TaskNode(7, "f"), 16);
		threep3.addTask(new TaskNode(4, "e"), 14);

		Output.createOutput(processes3, graph, "CorrectTripleProcessorS3");

		boolean s1 = compareTextFiles("src/main/resources/DotFiles/CorrectTripleProcessorS1-output.dot", "src/main/resources/DotFiles/greedySolutionTripleProcessor-output.dot");
		boolean s2 = compareTextFiles("src/main/resources/DotFiles/CorrectTripleProcessorS2-output.dot", "src/main/resources/DotFiles/greedySolutionTripleProcessor-output.dot");
		boolean s3 = compareTextFiles("src/main/resources/DotFiles/CorrectTripleProcessorS3-output.dot", "src/main/resources/DotFiles/greedySolutionTripleProcessor-output.dot");

		if (s1) {
			assertTrue(s1);
		}
		else if (s2) {	
			assertTrue(s2);
		}
		else if (s3) {
			assertTrue(s3);
		}
		else {
			fail();
		}

	}
	
	 @Test
	    public void testTwoEntryNodes() throws IOException {

	        // Put a unique file name (doesn't have to be the name of the graph), and the graphs file path.
		 	GraphLoader loader = new GraphLoader();
	        String outputFileName = "TwoParents";
	        TaskGraph graph = loader.load("src/main/resources/DotFiles/TestTwoParents.dot");
	        
	        SchedulerI scheduler = new GreedyScheduler(graph, 2);
	        Schedule solution = scheduler.createSchedule();

	        Output.createOutput(solution.getProcessors(), graph, outputFileName + "TestSolution");

	        Schedule correctSolution = new Schedule(2, graph);

	        List<Processor> processes = correctSolution.getProcessors();

	        Processor p1 = processes.get(0);
	        Processor p2 = processes.get(1);

	        p1.addTask(new TaskNode(7, "c"), 0);
	        p1.addTask(new TaskNode(3, "e"), 7);
	        p2.addTask(new TaskNode(100, "b"), 0);
	        p2.addTask(new TaskNode(12, "d"), 100);
	        p2.addTask(new TaskNode(13, "f"), 112);

	        Output.createOutput(processes, graph, outputFileName + "CorrectSolution");

	        boolean same = compareTextFiles("src/main/resources/DotFiles/" + outputFileName +"TestSolution-output.dot", "src/main/resources/DotFiles/" + outputFileName + "CorrectSolution-output.dot");
	        assertTrue(same);
	    }
	
	 
	 @Test
	    public void testThreeParents() throws Exception {
		 	GraphLoader loader = new GraphLoader();
	        String outputFileName = "outputThreeParents";
	        TaskGraph graph = loader.load("src/main/resources/DotFiles/threeParents.dot");

	        SchedulerI scheduler = new GreedyScheduler(graph, 2);
	        Schedule solution = scheduler.createSchedule();

	        Output.createOutput(solution.getProcessors(), graph, outputFileName + "TestSolution");

	        Schedule correctSolution = new Schedule(2, graph);

	        List<Processor> processes = correctSolution.getProcessors();

	        Processor p1 = processes.get(0);
	        Processor p2 = processes.get(1);

	        p1.addTask(new TaskNode(7, "a"), 0);
	        p1.addTask(new TaskNode(6, "c"), 7);
	        p2.addTask(new TaskNode(7, "b"), 9);
	        p1.addTask(new TaskNode(12, "d"), 13);
	        p1.addTask(new TaskNode(3, "e"), 25);

	        Output.createOutput(processes, graph, outputFileName + "CorrectSolution");

	        boolean same = compareTextFiles("src/main/resources/DotFiles/" + outputFileName +"TestSolution-output.dot", "src/main/resources/DotFiles/" + outputFileName + "CorrectSolution-output.dot");
	        System.out.println(same);
	        assertTrue(same);

	    }
	 
	 @Test
	    public void testNodes7() throws IOException {

	        // Put a unique file name (doesn't have to be the name of the graph), and the graphs file path.
		 	GraphLoader loader = new GraphLoader();
	        String outputFileName = "Nodes7";
	        TaskGraph graph = loader.load("src/main/resources/DotFiles/Nodes_7_OutTree.dot");
	        SchedulerI scheduler = new GreedyScheduler(graph, 2);
	        Schedule solution = scheduler.createSchedule();

	        Output.createOutput(solution.getProcessors(), graph, outputFileName + "TestSolution");

	        Schedule correctSolution = new Schedule(2, graph);

	        List<Processor> processes = correctSolution.getProcessors();

	        Processor p1 = processes.get(0);
	        Processor p2 = processes.get(1);

	        p1.addTask(new TaskNode(5, "0"), 0);
	        p1.addTask(new TaskNode(5, "2"), 5);
	        p1.addTask(new TaskNode(6, "3"), 10);
	        p1.addTask(new TaskNode(6, "1"), 16);
	        p1.addTask(new TaskNode(4, "4"), 22);
	        p1.addTask(new TaskNode(7, "5"), 26);
	        p1.addTask(new TaskNode(7, "6"), 33);

	        Output.createOutput(processes, graph, outputFileName + "CorrectSolution");

	        boolean same = compareTextFiles("src/main/resources/DotFiles/" + outputFileName +"TestSolution-output.dot", "src/main/resources/DotFiles/" + outputFileName + "CorrectSolution-output.dot");
	        assertTrue(same);
	    }
	


}