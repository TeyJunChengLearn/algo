#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <chrono>

using namespace std::chrono;
using namespace std;

struct Data
{
    int num;
    string word;
};
Data* readFile(const string& filename, int& size);
void mergeSort(Data arr[], int left, int right, int fullSize);
void merge(Data arr[], int leftIndex, int midIndex, int rightIndex);
void writeToFile(const string& filename,Data array[],int size);


int main(int argc, char* argv[]) {
    if (argc != 2) {
        cout << "Usage: merge_sort_step_array <filename>\n";
        return 1;
    }

    string filename = argv[1];
    int size;

    Data* data = readFile(filename, size);
    auto startTime = high_resolution_clock::now();
    mergeSort(data, 0, size - 1, size);
    auto endTime = high_resolution_clock::now();
    string outFile = "merge_sort_" +to_string(size)+ ".txt";
    double duration = duration_cast<milliseconds>(endTime - startTime).count();
    cout << "Merge sort completed in " << duration << " ms" << endl;
    writeToFile(outFile,data,size);
    
    delete[] data;

    return 0;
}

Data* readFile(const string& filename,int& size){
    ifstream file(filename);
    string line;
    int count = 0;

    // First pass: count lines
    while (getline(file, line)) {
        ++count;
    }

    file.clear();              // Clear EOF flag
    file.seekg(0, ios::beg);   // Reset to beginning

    // Allocate memory
    Data* array = new Data[count];
    int index = 0;

    // Second pass: read data
    while (getline(file, line)) {
        stringstream ss(line);
        string numString, word;
        getline(ss, numString, ',');
        getline(ss, word);
        array[index++] = { stoi(numString), word };
    }

    size = count;
    return array;
}
void mergeSort(Data arr[], int left, int right, int fullSize) {
    if (left < right) {
        int mid = (left + right) / 2;
        mergeSort(arr, left, mid, fullSize);
        mergeSort(arr, mid + 1, right, fullSize);
        merge(arr, left, mid, right);
    }
}

void merge(Data arr[], int leftIndex, int midIndex, int rightIndex) {
    int n1 = midIndex - leftIndex + 1;
    int n2 = rightIndex - midIndex;

    Data* leftArray = new Data[n1];
    Data* rightArray = new Data[n2];

    for (int i = 0; i < n1; ++i){
        leftArray[i] = arr[leftIndex + i];
    }
    for (int j = 0; j < n2; ++j) {
        rightArray[j] = arr[midIndex + 1 + j];
    }
    int i = 0, j = 0, k = leftIndex;

    while (i < n1 && j < n2) {
        if (leftArray[i].num <= rightArray[j].num){
            arr[k++] = leftArray[i++];
        }else{
            arr[k++] = rightArray[j++];
        }
            
    }

    while (i < n1) {
        arr[k++] = leftArray[i++];
    }
    while (j < n2){
        arr[k++] = rightArray[j++];
    } 

    delete[] leftArray;
    delete[] rightArray;
}

void writeToFile(const string& filename,Data array[],int size) {
    ofstream out(filename);
    for (int i = 0; i < size; ++i) {
        out << array[i].num << "," << array[i].word << "\n";
    }
    out.close();
    cout << "Sorted data written to: " << filename << endl;
}