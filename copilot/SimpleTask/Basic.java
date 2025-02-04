<<<<<<< HEAD
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


=======
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
                if (arr[j] < arr[minIndex])
                    minIndex = j;
            // swap arr[i] and arr[minIndex]
            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }

    
    // "123456234"  中要搜索到 第一次出现的 "234"的位置，请用正则表达式实现
    public static int search(String str, String pattern) {
        int n = str.length(), m = pattern.length();
        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            for (; j < m; j++)
                if (str.charAt(i + j) != pattern.charAt(j))
                    break;
            if (j == m) return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        String str = "123456234";
        String pattern = "234";
        System.out.println(search(str, pattern));
    }

    // main函数

}


>>>>>>> a58e6cdcbd4761fb743efbf07e8c0286981c4bd0
