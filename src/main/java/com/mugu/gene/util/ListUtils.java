package com.mugu.gene.util;

import java.util.List;

/**
 * @Author : zp
 * @Description :
 * @Date : 2019/10/18
 */
public class ListUtils {

    /**
     * 使用递归的二分查找
     * title:recursionBinarySearch
     *
     * @param arr 有序数组
     * @param key 待查找关键字
     * @return 找到的位置
     */
    public static Integer recursionBinarySearch(List<Integer> arr, int key, int low, int high) {
        if (key < arr.get(0) || key >= arr.get(high)) {
            return -1;
        }

        if (key >= arr.get(low) && key < arr.get(low + 1)) {
            return arr.get(low);
        }
        //初始中间位置
        int middle = (low + high) / 2;
        if (arr.get(middle) > key) {
            //比关键字大则关键字在左区域
            return recursionBinarySearch(arr, key, low, middle - 1);
        } else if (arr.get(middle) < key) {
            //比关键字小则关键字在右区域
            return recursionBinarySearch(arr, key, middle + 1, high);
        } else {
            return -1;
        }

    }
}
