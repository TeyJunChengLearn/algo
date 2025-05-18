#include <iostream>
#include <fstream>
#include <sstream>
#include <string>

using namespace std;

string* steps = nullptr;
int stepCount = 0;
int stepCapacity = 0;

struct Data
{
    int num;
    string word;
};
Data* readFile(const string& filename, int startIndex, int endIndex, int& size);
void mergeSort(Data arr[], int left, int right, int fullSize);
void merge(Data arr[], int leftIndex, int midIndex, int rightIndex);
void logStep(Data array[], int size);
void addStep(string step);
void writeStepsToFile(const string& filename);


int main(int argc, char* argv[]) {
    if (argc != 4) {
        cout << "Usage: merge_sort_step_array <filename> <startRow> <endRow>\n";
        return 1;
    }

    string filename = argv[1];
    int start = stoi(argv[2]);
    int end = stoi(argv[3]);
    int size;

    Data* data = readFile(filename, start, end, size);
    mergeSort(data, 0, size - 1, size);

    string outFile = "merge_sort_step_" + to_string(start) + "_" + to_string(end) + ".txt";
    writeStepsToFile(outFile);

    cout << "Steps written to: " << outFile << endl;

    delete[] data;
    delete[] steps;
    return 0;
}

Data* readFile(const string& filename,int startIndex, int endIndex,int& size){
    ifstream file(filename);
    string line;
    int i=0,idx=0;
    Data* array = new Data[endIndex - startIndex + 1];
    while(getline(file,line)){
        if(i>=startIndex && i<=endIndex){
            stringstream ss(line);
            string numString, word;
            getline(ss, numString, ',');
            getline(ss, word);
            array[idx++] = { stoi(numString), word };
        }

        if(i>endIndex){
            break;
        }

        i++;
    }

    size=idx;

    return array;
}
void mergeSort(Data arr[], int left, int right, int fullSize) {
    if (left < right) {
        int mid = (left + right) / 2;
        mergeSort(arr, left, mid, fullSize);
        mergeSort(arr, mid + 1, right, fullSize);
        merge(arr, left, mid, right);
        logStep(arr, fullSize);
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
void logStep(Data array[], int size) {
    string line;
    for (int i = 0; i < size; ++i) {
        line += to_string(array[i].num) + "/" + array[i].word + ", ";
    }
    if (!line.empty()) line = line.substr(0, line.size() - 2);
    addStep(line);
}
void addStep(string step){
    if(stepCount >=stepCapacity){
        stepCapacity = stepCapacity == 0 ? 10 : stepCapacity * 2;
        string* newSteps = new string[stepCapacity];
        for (int i = 0; i < stepCount; ++i)
            newSteps[i] = steps[i];
        delete[] steps;
        steps = newSteps;
    }
    steps[stepCount++] = step;
    
}

void writeStepsToFile(const string& filename) {
    ofstream out(filename);
    for (int i = 0; i < stepCount; ++i) {
        out <<"["<< steps[i] << "]\n";
    }
    out.close();
}