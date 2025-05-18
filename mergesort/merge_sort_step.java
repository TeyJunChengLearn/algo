import java.io.*;
import java.util.*;

public class merge_sort_step {
    public static List<String> steps= new ArrayList<>();
    public static void main(String[] args){
        // see whether got 3 arguments
        if(args.length != 3){
            System.out.println("command line: java MergeSortWithStep <filename> <start> <end>");
            return;
        }

        // get all value from command line
        String fileName = args[0];
        int start = Integer.parseInt(args[1]);//assign as integer value from a string
        int end = Integer.parseInt(args[2]);
        try {
            List<String[]> list= readCSVFileToGetValue(fileName, start, end);
            mergeSort(list,0,list.size()-1);
            writeStepsToFile("merge_sort_step_"+start+"_"+end+".txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> readCSVFileToGetValue(String fileName,int start,int end) throws IOException{
        // initialize all needed variable
        List<String[]> dataToReturn = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        int i=0;
        // loop to see whether any next line is empty
        while((line=reader.readLine())!=null){
            // if index is in range
            if((i>=start) && (i<=end)){
                //store the value by split with "," example "123,Elson" become {"123","Elson"} 
                String[] values = line.split(",");
                // add into data
                dataToReturn.add(values);
            }
            if(i>end){
                //stop if index more than end
                break;
            }
            i++;
        }
        reader.close();
        //return data
        return dataToReturn;

    }

    public static void mergeSort(List<String[]> list, int leftIndex, int rightIndex){
        if(leftIndex<rightIndex){
            // get mid index value
            int midIndex=(leftIndex+rightIndex)/2;
            //recursion
            mergeSort(list,leftIndex,midIndex);
            mergeSort(list, midIndex+1, rightIndex);
            //merge function call to sort after cannot recur any more
            merge(list,leftIndex,midIndex,rightIndex);
            // pass the current list after merge
            logCurrentStep(list);
        }
    }

    public static void merge(List<String[]> list,int leftIndex, int midIndex, int rightIndex){
        //left part
        List<String[]> leftList= new ArrayList<>(list.subList(leftIndex, midIndex+1));
        //right part
        List<String[]> rightList = new ArrayList<>(list.subList(midIndex+1, rightIndex+1));
        // initialize needed variable
        int i = 0, j = 0, k = leftIndex;
        // if both side i left and j right still lower than size of
        while (i < leftList.size() && j < rightList.size()) {
            // get left and right available int value
            int val1 = Integer.parseInt(leftList.get(i)[0]);
            int val2 = Integer.parseInt(rightList.get(j)[0]);
            // if left <= right will store left to the list else will store right into list
            if (val1 <= val2) {
                list.set(k++, leftList.get(i++));
            } else {
                list.set(k++, rightList.get(j++));
            }
        }
        // if left or right either one no more to store will straight set into the list, start from left first then right
        while (i < leftList.size()) {
            list.set(k++, leftList.get(i++));
        }
        while (j < rightList.size()) {
            list.set(k++, rightList.get(j++));
        }
    }

    public static void logCurrentStep(List<String[]> list) {
        StringBuilder sb = new StringBuilder();
        for (String[] entry : list) {
            // add all the entry to a line of string with <int value>/<string value>,
            sb.append(entry[0]).append("/").append(entry[1]).append(", ");
        }
        // Remove last comma and space
        if (sb.length() >= 2) sb.setLength(sb.length() - 2);
        //store into steps
        steps.add(sb.toString());
    }

    public static void writeStepsToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (String step : steps) {
            writer.write("["+step+"]");
            writer.newLine();
        }
        writer.close();
        System.out.println("Steps written to: " + filename);
    }
}
