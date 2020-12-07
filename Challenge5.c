//first 7 chars describe the row and are \in {F, B},
//eqivalent to {0, 1}
//FBFBBFF = 0101100 = 44, so row 44;
//last 3 chars describe the column and are \in {L, R},
//eqivalent to {0, 1}
//RLR = 101 = 5, so 5th column
// SEAT_ID = row# * 8 + column#

//What is the highest seat ID?

//TODO: functions that compute the row, the column and thenthe seat id; loop over all entries and compare

#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <limits.h>
#include <stdbool.h>
#include <stdlib.h>
#include <math.h>

//array is stolen
typedef struct {
  int *array;
  size_t used;
  size_t size;
} Array;

void initArray(Array *a, size_t initialSize) {
  a->array = malloc(initialSize * sizeof(int));
  a->used = 0;
  a->size = initialSize;
}

void insertArray(Array *a, int element) {
  // a->used is the number of used entries, because a->array[a->used++] updates a->used only *after* the array has been accessed.
  // Therefore a->used can go up to a->size
  if (a->used == a->size) {
    a->size *= 2;
    a->array = realloc(a->array, a->size * sizeof(int));
  }
  a->array[a->used++] = element;
}

void freeArray(Array *a) {
  free(a->array);
  a->array = NULL;
  a->used = a->size = 0;
}

//so is sorting
void swap(int* xp, int* yp)
{
    int temp = *xp;
    *xp = *yp;
    *yp = temp;
}

// Function to perform Selection Sort
void selectionSort(int arr[], int n)
{
    int i, j, min_idx;

    // One by one move boundary of unsorted subarray
    for (i = 0; i < n - 1; i++) {

        // Find the minimum element in unsorted array
        min_idx = i;
        for (j = i + 1; j < n; j++)
            if (arr[j] < arr[min_idx])
                min_idx = j;

        // Swap the found minimum element
        // with the first element
        swap(&arr[min_idx], &arr[i]);
    }
}

int compute_row(char * entry) {
    int result = 0;
    int digit = -1;
    for (int i = strlen(entry) - 4; i >= 0; i--) {
        if (entry[i] == 'F') {
            digit = 0;
        } else if (entry[i] == 'B'){
            digit = 1;
        } else {
            perror("wrong input");
        }
        result = result + digit * (int) pow(2.0, 6.0 - (double) i);
    }

    //printf("result is %d\n", result);
    return(result);
}

int compute_column(char * entry) {
    int result = 0;
    int digit = -1;
    for (int i = strlen(entry) - 1; i >= 7; i--) {
        //printf("i is %d and entry[%d] is %c\n", i, i, entry[i]);
        if (entry[i] == 'L') {
            digit = 0;
        } else if (entry[i] == 'R'){
            digit = 1;
        } else {
            printf("entry is %s, entry[%d] = %c\n", entry, i, entry[i]);
            perror("wrong input");
        }
        //printf("i = %d, 2^(9 - i) = %d, digit = %d and digit * power = %d\n", i, (int) pow(2.0, 9.0 - (double) i), digit, digit * (int) pow(2.0, 9.0 - (double) i));
        result = result + digit * (int) pow(2.0, 9.0 - (double) i);
    }
    //printf("result is %d\n", result);
    return(result);
}

int compute_sID(int * row, int * column) {
    return((* row) * 8 + (* column));
}

int size_of_array(int * array) {
    return sizeof(* array) / sizeof(array[0]);
}

int find_missing_value(Array seat_IDs) {
    for (int i = 0; i < seat_IDs.used; i ++) {
        if (seat_IDs.array[i] + 1 != seat_IDs.array[i + 1]) {
            return(seat_IDs.array[i] + 1);
        }
    }
    return(-1);
}

int main() {
    FILE *input;
    input = fopen("inputChallenge5.txt", "r");
    int row_length = 11;
    char entry[row_length];
    //since seat_id = row# * 8 + column# and row is \in [0, 127] and column is \in [0, 7], the largest possible seat_ID is 128 * 8 + 8
    int max = 0;
    Array seat_IDs;
    initArray(&seat_IDs, 1);

    while (fgets(entry, row_length, input) != NULL) {
        //NOTE: max is 11, taking the whole string and the '\0', and leaving the new line character; then there is an extra entry that is '\n', so we need to take care of that
        if (strcmp(entry, "\n") == 0){
            continue;
        }
        //printf("row is %s\n", entry);

        int row = compute_row(entry);
        int column = compute_column(entry);
        int seat_ID = compute_sID(&row, &column);
        insertArray(&seat_IDs, seat_ID);
        //printf("seat_ID %d\n", seat_ID);
        if (seat_ID > max) {
            max = seat_ID;
        }
    }

    printf("max is %d\n", max);
    //printf("%ld\n", seat_IDs.used);  // print number of elements

    selectionSort(seat_IDs.array, seat_IDs.used);

    int missing_value = find_missing_value(seat_IDs);
    printf("seat number is %d\n", missing_value);

    freeArray(&seat_IDs);
    return(0);
}
