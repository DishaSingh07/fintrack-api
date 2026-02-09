package com.disha.fintrack.util;

import java.util.List;

public class CategoryColorUtil {

    private static final List<String> COLORS = List.of(
            "bg-red-100 text-red-700",
            "bg-orange-100 text-orange-700",
            "bg-yellow-100 text-yellow-700",
            "bg-green-100 text-green-700",
            "bg-blue-100 text-blue-700",
            "bg-indigo-100 text-indigo-700",
            "bg-purple-100 text-purple-700",
            "bg-pink-100 text-pink-700"
    );

    public static String getColor(int index) {
        return COLORS.get(index % COLORS.size());
    }
}
