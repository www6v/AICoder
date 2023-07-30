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
}


