public class Basic {
    // 二分查找
    public static int binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1; // [left, right]
        while (left <= right) {
            int mid = left + (right - left) / 2; // 防止溢出
            if (arr[mid] == target) return mid;
            else if (arr[mid] < target) left = mid + 1; // target 在 [mid + 1, right] 中
            else right = mid - 1; // target 在 [left, mid - 1] 中
        }
        return -1;
    }


    // 冒泡排序
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (arr[j] > arr[j+1]) {
                    // swap arr[j+1] and arr[j]
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
    }

    // 选择排序
    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n-1; i++) {
            int minIndex = i;
            for (int j = i+1; j < n; j++)
                if (arr[j] < arr[minIndex]) // 寻找最小的数
                    minIndex = j; // 将最小数的索引保存
            // swap arr[i] and arr[minIndex]
            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }
}


