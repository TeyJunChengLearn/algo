import java.io.*;
import java.util.*;

public class merge_sort{
    public static void main(String[] args){
        // see whether got 3 arguments
        if(args.length != 1){
            System.out.println("command line: java MergeSortWithStep <filename>");
            return;
        }

        // get all value from command line
        String fileName = args[0];

        try {
            List<String[]> list= readCSVFileToGetValue(fileName);
            long startTime = System.nanoTime();
            mergeSort(list,0,list.size()-1);
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;            
            System.out.println("Merge Sort Complete in "+durationMs+" ms");
            writeSortedListToFile("merge_sort_"+list.size()+".csv",list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> readCSVFileToGetValue(String fileName) throws IOException{
        // initialize all needed variable
        List<String[]> dataToReturn = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        // loop to see whether any next line is empty
        while((line=reader.readLine())!=null){
                //store the value by split with "," example "123,Elson" become {"123","Elson"} 
                String[] values = line.split(",");
                // add into data
                dataToReturn.add(values);

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

    public static void writeSortedListToFile(String filename,List<String[]> list) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for(String[] data:list){
            writer.write(data[0]+","+data[1]);
            writer.newLine();
        }
        writer.close();
        System.out.println("Sorted list written to: " + filename);
    }
}
