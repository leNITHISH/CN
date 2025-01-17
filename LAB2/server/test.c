#include <stdio.h>
#include <unistd.h>

int main() {
    for (int i = 0; i <= 100; i++) {
        printf("\rProgress: %d%%", i);
        fflush(stdout); // Ensure output is displayed immediately
        sleep(2);  // Sleep for 50ms
    }
    printf("\nDone!\n");
    return 0;
}

