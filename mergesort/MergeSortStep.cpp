#include <iostream>
#include <fstream>
#include <sstream>
#include <string>

using namespace std;

struct Entry {
    int number;
    string word;
};

string* steps = nullptr;  // dynamic step log array
int stepCount = 0;
int stepCapacity = 0;

void addStep(string step) {
    if (stepCount >= stepCapacity) {
        // Resize step log if needed
        stepCapacity = stepCapacity == 0 ? 10 : stepCapacity * 2;
        string* newSteps = new string[stepCapacity];
        for (int i = 0; i < stepCount; ++i)
            newSteps[i] = steps[i];
        delete[] steps;
        steps = newSteps;
    }
    steps[stepCount++] = step;
}

Entry* readCSV(const string& filename, int start, int end, int& outSize) {
    ifstream file(filename);
    string line;
    int row = 0, count = 0;
    Entry* entries = new Entry[end - start + 1];

    while (getline(file, line)) {
        if (row >= start && row <= end) {
            stringstream ss(line);
            string numStr, word;
            getline(ss, numStr, ',');
            getline(ss, word);
            entries[count++] = { stoi(numStr), word };
        }
        if (row > end) break;
        row++;
    }
    outSize = count;
    return entries;
}

void logStep(Entry arr[], int size) {
    string line;
    for (int i = 0; i < size; ++i) {
        line += to_string(arr[i].number) + "/" + arr[i].word + ", ";
    }
    if (!line.empty()) line = line.substr(0, line.size() - 2);
    addStep(line);
}

void merge(Entry arr[], int left, int mid, int right) {
    int n1 = mid - left + 1;
    int n2 = right - mid;

    Entry* L = new Entry[n1];
    Entry* R = new Entry[n2];

    for (int i = 0; i < n1; ++i) L[i] = arr[left + i];
    for (int j = 0; j < n2; ++j) R[j] = arr[mid + 1 + j];

    int i = 0, j = 0, k = left;

    while (i < n1 && j < n2) {
        if (L[i].number <= R[j].number)
            arr[k++] = L[i++];
        else
            arr[k++] = R[j++];
    }

    while (i < n1) arr[k++] = L[i++];
    while (j < n2) arr[k++] = R[j++];

    delete[] L;
    delete[] R;
}

void mergeSort(Entry arr[], int left, int right, int fullSize) {
    if (left < right) {
        int mid = (left + right) / 2;
        mergeSort(arr, left, mid, fullSize);
        mergeSort(arr, mid + 1, right, fullSize);
        merge(arr, left, mid, right);
        logStep(arr, fullSize);
    }
}

void writeStepsToFile(const string& filename) {
    ofstream out(filename);
    for (int i = 0; i < stepCount; ++i) {
        out << steps[i] << "\n";
    }
    out.close();
}

int main(int argc, char* argv[]) {
    if (argc != 4) {
        cout << "Usage: ./merge_sort_step_array <filename> <startRow> <endRow>\n";
        return 1;
    }

    string filename = argv[1];
    int start = stoi(argv[2]);
    int end = stoi(argv[3]);
    int size;

    Entry* data = readCSV(filename, start, end, size);
    mergeSort(data, 0, size - 1, size);

    string outFile = "merge_sort_step_" + to_string(start) + "_" + to_string(end) + ".txt";
    writeStepsToFile(outFile);

    cout << "Steps written to: " << outFile << endl;

    delete[] data;
    delete[] steps;
    return 0;
}
