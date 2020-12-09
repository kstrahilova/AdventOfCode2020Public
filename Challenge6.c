#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <limits.h>
#include <stdbool.h>
#include <stdlib.h>
#include <math.h>
#include <ctype.h>

//3427
//array is stolen
typedef struct {
  char * array;
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
    a->array = realloc(a->array, a->size * sizeof(char));
  }
  a->array[a->used++] = element;
}

void freeArray(Array *a) {
  free(a->array);
  a->array = NULL;
  a->used = a->size = 0;
}

//from here on it is not
void removeArray(Array *a, int * position) {
    if (*position >= 0 && *position < a -> used) {
        for (int i = * position; i < a -> used; i++) {
            a -> array[i] = a -> array[i + 1];
        }
        a -> used = (a -> used) - 1;
    } else {
        perror("position is not valid\n");
    }
}

void print_Array(Array *a) {
    for (int i = 0; i < a -> used; i++) {
        printf("%c, ", a -> array[i]);
    }
    printf("\n");
}

bool is_value_in_Array(Array * array, char * value) {
    for (int i = 0; i < array -> used; i++) {
        if (array -> array[i] == *value) {
            return true;
        }
    }
    return false;
}

bool is_value_in_array(char * array, char * value) {
    for (int i = 0; i < strlen(array); i++) {
        if (array[i] == *value) {
            return true;
        }
    }
    return false;
}

void remove_illegal_symbols_from_Array(Array *a, char * alphabet) {
    int counter = 0;
    for (int i = 0; i < a -> used; i++) {
        if (a -> array[i] == NULL || !is_value_in_array(alphabet, &(a -> array[i]))) {
            counter = counter + 1;
            for (int j = i; j < a -> used; j++) {
                a -> array[j] = a -> array[j + 1];
            }
            if (a -> used == 1 && is_value_in_array(alphabet, &(a -> array[0]))) {
                insertArray(a, '\0');
                break;
            } else {
                a -> used = (a -> used) - counter;
            }
        }
    }
}

void process_row_any(char * entry, Array * positive_questions_any) {
    for (int i = 0; i < strlen(entry) - 1; i++) {
        if (!is_value_in_Array(positive_questions_any, &entry[i])) {
            insertArray(positive_questions_any, entry[i]);
        }
    }
}

void process_row_all(char * entry, Array * positive_questions_all, bool * new, char * alphabet) {
    if (*new) {
        for (int i = 0; i < strlen(entry) - 1; i++) {
            if (is_value_in_array(alphabet, &entry[i])) {
                insertArray(positive_questions_all, entry[i]);
            }
        }
    } else {
        for (int i = (positive_questions_all -> used) - 1; i >= 0; i--) {
            if (!is_value_in_array(entry, &positive_questions_all -> array[i])){
                positive_questions_all -> array[i] = NULL;
                remove_illegal_symbols_from_Array(positive_questions_all, alphabet);
            }
        }
    }
    printf("List of current positively answered questions: ");
    print_Array(positive_questions_all);

}

int main() {
    FILE *input;
    input = fopen("inputChallenge6.txt", "r");
    //input = fopen("exampleInputC6.txt", "r");
    //input = fopen("input2C6.txt", "r");
    int max_row_length = 10000;
    char entry[max_row_length];
    Array positive_questions_any;
    Array positive_questions_all;
    initArray(&positive_questions_any, 1);
    initArray(&positive_questions_all, 1);
    int result_any = 0;
    int result_all = 0;
    bool new = true;

    char alphabet[27];
    int i = 0;
    for (char c = 'a'; c <= 'z'; c++) {
        alphabet[i] = c;
        i++;
    }
    alphabet[26] = '\0';

    while (fgets(entry, max_row_length, input) != NULL) {
        //NOTE: max is 11, taking the whole string and the '\0', and leaving the new line character; then there is an extra entry that is '\n', so we need to take care of that
        if (strcmp(entry, "\n") == 0){
            result_any = result_any + positive_questions_any.used;
            result_all = result_all + positive_questions_all.used;
            printf("intermediate result_all is %d\n", result_all);
            printf("\n");
            printf("new group\n");
            freeArray(&positive_questions_any);
            freeArray(&positive_questions_all);
            initArray(&positive_questions_any, 1);
            initArray(&positive_questions_all, 1);
            new = true;
            continue;
        }
        printf("\n");
        printf("new person, entry is %s\n", entry);
        process_row_any(entry, &positive_questions_any);
        process_row_all(entry, &positive_questions_all, &new, alphabet);
        new = false;
    }

    printf("result for Part 1 is %d\n", result_any);
    printf("result for Part 2 is %d\n", result_all);

    return(0);
}
