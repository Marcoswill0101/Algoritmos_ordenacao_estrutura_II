import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int[] tamanhos = {1000, 10000};
        int repeticoes = 30;
        
        for (int tamanho : tamanhos) {
            System.out.println("\nExperimentos com " + tamanho + " elementos:");
            realizarExperimentos(tamanho, repeticoes);
        }
    }

    private static void realizarExperimentos(int tamanho, int repeticoes) {
        String[] algoritmos = {"BubbleSort", "InsertionSort", "SelectionSort", "QuickSort", "MergeSort"};
        String[] distribuicoes = {"Aleatória", "Crescente", "Decrescente"};
        
        for (String dist : distribuicoes) {
            System.out.println("\nDistribuição " + dist + ":");
            double[][] tempos = new double[algoritmos.length][repeticoes];
            
            for (int r = 0; r < repeticoes; r++) {
                int[] array = gerarArray(tamanho, dist);
                
                for (int a = 0; a < algoritmos.length; a++) {
                    int[] copyArray = array.clone();
                    long inicio = System.nanoTime();
                    
                    switch (algoritmos[a]) {
                        case "BubbleSort": bubbleSort(copyArray); break;
                        case "InsertionSort": insertionSort(copyArray); break;
                        case "SelectionSort": selectionSort(copyArray); break;
                        case "QuickSort": quickSort(copyArray, 0, copyArray.length - 1); break;
                        case "MergeSort": mergeSort(copyArray, 0, copyArray.length - 1); break;
                    }
                    
                    tempos[a][r] = (System.nanoTime() - inicio) / 1_000_000.0; // Convertendo para millisegundos
                }
            }
            
            // Calcular e exibir estatísticas
            for (int a = 0; a < algoritmos.length; a++) {
                double media = calcularMedia(tempos[a]);
                double desvioPadrao = calcularDesvioPadrao(tempos[a], media);
                System.out.printf("%s: Média = %.2f ms, Desvio Padrão = %.2f ms%n", 
                    algoritmos[a], media, desvioPadrao);
            }
        }
    }

    private static int[] gerarArray(int tamanho, String distribuicao) {
        int[] array = new int[tamanho];
        switch (distribuicao) {
            case "Aleatória":
                Random rand = new Random();
                for (int i = 0; i < tamanho; i++) {
                    array[i] = rand.nextInt(tamanho);
                }
                break;
            case "Crescente":
                for (int i = 0; i < tamanho; i++) {
                    array[i] = i;
                }
                break;
            case "Decrescente":
                for (int i = 0; i < tamanho; i++) {
                    array[i] = tamanho - i - 1;
                }
                break;
        }
        return array;
    }

    private static double calcularMedia(double[] valores) {
        return Arrays.stream(valores).average().orElse(0.0);
    }

    private static double calcularDesvioPadrao(double[] valores, double media) {
        double somaDiferencasQuadrado = 0;
        for (double valor : valores) {
            somaDiferencasQuadrado += Math.pow(valor - media, 2);
        }
        return Math.sqrt(somaDiferencasQuadrado / valores.length);
    }

    // Implementações dos algoritmos de ordenação
    private static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    private static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    private static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
    }

    private static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    private static void mergeSort(int[] arr, int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }

    private static void merge(int[] arr, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++) {
            L[i] = arr[l + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = arr[m + 1 + j];
        }

        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }
}
